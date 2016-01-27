package server.net.login;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import server.Config;
import server.Connection;
import server.Server;
import server.core.GameEngine;
import server.core.PlayerHandler;
import server.core.World;
import server.core.task.Task;
import server.model.players.Client;
import server.model.players.PlayerSave;
import server.net.PacketBuilder;
import server.util.ISAACCipher;
import server.util.Misc;

public class RS2LoginProtocolDecoder extends FrameDecoder {

	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;
	private static int uid;
	public static String UUID;
	public static String MAC;

	private static final BigInteger RSA_MODULUS = new BigInteger(
			"106275508111934841104485182175070489771606944350689197131564237103161242938076102371827367937469958095740432295410533677008059085041337202781803071905626509012599664922823611099945234455710609482108293538067045447369205556626140278246510996742614564001663550655399963925871731489184419274336369096105713229491");

	private static final BigInteger RSA_EXPONENT = new BigInteger(
			"66284963754572967407524548065797661472209674798644607198221153788376321258782043129084570422998031754909225483424645232180011644722060812989745981158954305607606388674770201134426289520298229572828443836235208460304999386078852166171736724692187261474807468041091130006727471146127151290019390495106347867809");

	private static final String MAC_EXPR = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
	
	private static final Pattern ADDRESS_PATTERN = Pattern.compile(MAC_EXPR);

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (!channel.isConnected()) {
			return null;
		}
		switch (state) {
		case CONNECTED:
			if (buffer.readableBytes() < 2)
				return null;
			int request = buffer.readUnsignedByte();
			if (request != 14) {
				System.out.println("Invalid login request: " + request);
				channel.close();
				return null;
			}
			buffer.readUnsignedByte();
			channel.write(new PacketBuilder().putLong(0).put((byte) 0)
					.putLong(new SecureRandom().nextLong()).toPacket());
			state = LOGGING_IN;
			return null;
		case LOGGING_IN:
			@SuppressWarnings("unused")
			int loginType = -1,
			loginPacketSize = -1,
			loginEncryptPacketSize = -1;
			if (2 <= buffer.capacity()) {
				loginType = buffer.readByte() & 0xff; // should be 16 or 18
				loginPacketSize = buffer.readByte() & 0xff;
				loginEncryptPacketSize = loginPacketSize - (36 + 1 + 1 + 2);
				if (loginPacketSize <= 0 || loginEncryptPacketSize <= 0) {
					System.out.println("Zero or negative login size.");
					channel.close();
					return false;
				}
			}

			/**
			 * Read the magic id.
			 */
			if (loginPacketSize <= buffer.capacity()) {
				int magic = buffer.readByte() & 0xff;
				int version = buffer.readUnsignedShort();
				if (magic != 255) {
					System.out.println("Wrong magic id.");
					channel.close();
					return false;
				}
				@SuppressWarnings("unused")
				int lowMem = buffer.readByte() & 0xff;

				/**
				 * Pass the CRC keys.
				 */
				for (int i = 0; i < 9; i++) {
					buffer.readInt();
				}
				loginEncryptPacketSize--;
				if (loginEncryptPacketSize != (buffer.readByte() & 0xff)) {
					System.out.println("Encrypted size mismatch.");
					channel.close();
					return false;
				}

				/**
				 * Our RSA components.
				 */
				ChannelBuffer rsaBuffer = buffer
						.readBytes(loginEncryptPacketSize);

				BigInteger bigInteger = new BigInteger(rsaBuffer.array());
				bigInteger = bigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);
				rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger
						.toByteArray());
				if ((rsaBuffer.readByte() & 0xff) != 10) {
					System.out.println("Encrypted id != 10.");
					channel.close();
					return false;
				}
				final long clientHalf = rsaBuffer.readLong();
				final long serverHalf = rsaBuffer.readLong();

				uid = rsaBuffer.readInt();

				UUID = Misc.rot47(Misc.getRS2String(rsaBuffer));

				if (uid == 69696969 || UUID == null) {
					channel.close();
					return false;
				}
				final String name = Misc.formatPlayerName(Misc
						.getRS2String(rsaBuffer));

				final String pass = Misc.getRS2String(rsaBuffer);
				MAC = Misc.rot47(Misc.getRS2String(rsaBuffer));
				final int[] isaacSeed = { (int) (clientHalf >> 32),
						(int) clientHalf, (int) (serverHalf >> 32),
						(int) serverHalf };
				final ISAACCipher inCipher = new ISAACCipher(isaacSeed);
				for (int i = 0; i < isaacSeed.length; i++)
					isaacSeed[i] += 50;
				final ISAACCipher outCipher = new ISAACCipher(isaacSeed);

				// final int version = buffer.readInt();
				channel.getPipeline().replace("decoder", "decoder", new RS2ProtocolDecoder(inCipher));
				return login(channel, inCipher, outCipher, version, name, pass, UUID);
			}
		}
		return null;

	}

	private static Client login(Channel channel, ISAACCipher inCipher,
			ISAACCipher outCipher, int version, String name, String pass, String UUID) {
		int returnCode = 2;
		if (!name.matches("[A-Za-z0-9 ]+")) {
			returnCode = 4;
		}
		if (name.length() > 12) {
			returnCode = 8;
		}
		if (name.endsWith(" ") || name.startsWith(" ")) {
			returnCode = 4;
		}
		Client cl = new Client(channel, -1);
		cl.playerName = name;
		cl.playerName2 = cl.playerName;
		cl.playerPass = pass;
		cl.outStream.packetEncryption = outCipher;
		cl.saveCharacter = false;
		cl.isActive = true;
		if (Connection.isIpBanned(((InetSocketAddress) channel
				.getRemoteAddress()).getAddress().getHostAddress().toString())) {
			returnCode = 4;
		}

		if (Connection.isNamedBanned(cl.playerName)) {
			returnCode = 4;
		}
		if(Connection.isUidBanned(UUID)) {
			returnCode = 22;
		}
		if(!isValidAddress(MAC) || Connection.isMacBanned(MAC)) {
			returnCode = 23;
		}
		if (!channel.getRemoteAddress().toString().startsWith("/127.0.0.1") && uid != 665412) {
			returnCode = 6;
		}
		if (PlayerHandler.isPlayerOn(name)) {
			returnCode = 5;
		}
		if (PlayerHandler.getPlayerCount() >= Config.MAX_PLAYERS) {
			returnCode = 7;
		}
		if (PlayerHandler.updateRunning && System.currentTimeMillis() - PlayerHandler.updateStartTime > (PlayerHandler.updateSeconds * 1000) - 10000) {
			returnCode = 14;
		}
		/*
		 * if (Server.playerInWorld(cl.playerName)) { returnCode = 5; }
		 */
		if (returnCode == 2) {
			int load = PlayerSave.loadGame(cl, cl.playerName, cl.playerPass);
			if (load == 0)
				cl.addStarter = true;
			if (load == 3) {
				returnCode = 3;
				cl.saveFile = false;
			} else {
				for (int i = 0; i < cl.playerEquipment.length; i++) {
					if (cl.playerEquipment[i] == 0) {
						cl.playerEquipment[i] = -1;
						cl.playerEquipmentN[i] = 0;
					}
				}
				if (!Server.playerHandler.newPlayerClient(cl)) {
					returnCode = 7;
					cl.saveFile = false;
				} else {
					cl.saveFile = true;
				}
			}
		}
		if (returnCode == 2) {
			cl.saveCharacter = true;
			cl.packetType = -1;
			cl.packetSize = 0;
			final PacketBuilder bldr = new PacketBuilder();
			bldr.put((byte) 2);
			if (cl.playerRights == 3) {
				bldr.put((byte) 2);
			} else {
				bldr.put((byte) cl.playerRights);
			}
			bldr.put((byte) 0);
			channel.write(bldr.toPacket());
		} else {
			sendReturnCode(channel, returnCode);
			return null;
		}
		World.getWorld().submit(new Task() {
			public void execute(GameEngine context) {
				cl.initialize();
				cl.initialized = true;
			}
		});
		return cl;
	}
	
	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket()).addListener(ChannelFutureListener.CLOSE);
	}
	
	private static boolean isValidAddress(String mac){
		return ADDRESS_PATTERN.matcher(mac).matches();             
	}

}

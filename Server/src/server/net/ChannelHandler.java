package server.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import server.model.players.Client;

public class ChannelHandler extends SimpleChannelHandler {

	private Session session = null;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (e.getMessage() instanceof Client) {
			session.setClient((Client) e.getMessage());
		} else if (e.getMessage() instanceof Packet) {
			if (session.getClient() != null) {
				// Packet packet = (Packet) e.getMessage();
				/*
				 * if (packet.getOpcode() == 41) {
				 * session.getClient().timeOutCounter = 0;
				 * session.getClient().wearId = packet.getShort();
				 * session.getClient().wearSlot = packet.getShortA();
				 * session.getClient().interfaceId = packet.getShortA();
				 * session.
				 * getClient().getItems().wearItem(session.getClient().wearId,
				 * session.getClient().wearSlot); } else {
				 */
				session.getClient().queueMessage((Packet) e.getMessage());
				// }
			}
		}
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (session == null)
			session = new Session(ctx.getChannel());
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		if (session != null) {
			Client client = session.getClient();
			if (client != null) {
				client.disconnected = true;
			}
			session = null;
		}
	}

}

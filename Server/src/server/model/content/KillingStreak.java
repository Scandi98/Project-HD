package server.model.content;
 
import server.model.players.Client;
import server.Server;
import server.core.PlayerHandler; //wheres your playerHandler?
import server.model.players.Player;
 
public class KillingStreak {
 
                Client c;
       
                public KillingStreak(Client c) {
                        this.c = c;
                }
       
                /**
                * Sends the message throughout the server
                */
               
                public void yell(String msg) {
                        for (int j = 0; j < PlayerHandler.players.length; j++) {
                                if (PlayerHandler.players[j] != null) {
                                Client c2 = (Client)PlayerHandler.players[j];
                                c2.sendMessage(msg);
                                }
                        }      
                }
               
                /**
                * Checks the player if they have
                * a killstreak of 2 or more
                * can add on
                *
                */
               
                public void checkKillStreak() {
                        switch (c.killStreak){
                                case 10:
                                        yell("@red@PvP System:<shad=6667> "+c.playerName+", is on a killing streak of 10!");
                                        break;
                                case 20:
                                        yell("@red@PvP System:<shad=6667> "+c.playerName+", is on a killing streak of 20!");
                                        break;
                                case 30:
                                        yell("@red@PvP System:<shad=6667> "+c.playerName+", is on a killing streak of 30!");
                                        break;
                                case 40:
                                        yell("@red@PvP System:<shad=6667> "+c.playerName+", is on a killing streak of 40! Try and end the streak!");
                                        break;
                                case 50:
                                        yell("@red@PvP System:<shad=6667> "+c.playerName+", is on a killing streak of 50! He's on @red@fire.");
                                        break;
                                }              
                        }
 
                /**
                * Checks if the player with the killstreak
                * died add items to the killer
                *
                */     
                       
                public void killedPlayer() {
                        Client o = (Client) PlayerHandler.players[c.killerId];
                                if (o.killStreak >= 2 || c.killerId != o.playerId) {
                                        yell("@red@PvP System:@bla@ "+c.playerName+" has ended "+o.playerName+" killing streak of "+o.killStreak+".");
                                                }
                                  }      
                        }
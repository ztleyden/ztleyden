/**
 * Created by ZT Leyden
 * 
 * BlackJack project01
 * 
 * this program must connect to a dealer with a wifi connection.
 * it will play blackjack with the dealer and others.
 * it will count cards and play basic strategy.
 * 
 */


import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.IOException; 
import java.io.DataInputStream; 
import java.io.DataOutputStream;
public class Blackjack {
    private static Socket socket;
    private static DataInputStream  dis;
    private static DataOutputStream dos;
    int num;//sets String card value to an int
   
    //Socket
	public Blackjack(String IpAddress, String IpPort) {
		num = 0;
		try {
			socket = new Socket(IpAddress, Integer.valueOf(IpPort));
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		

	}
	//writes to dealer
	private void write(String s) throws IOException { 
		
		dos.writeUTF(s.toLowerCase());
		dos.flush(); 
	}
	//reads from dealer
	private String read() throws IOException {
		return dis.readUTF();
		//return "hi";
		
	}
	//gets the count for each card
	public double cardCount(String value) {
		double cardCounter = 0;
		
		String theCardValue = value;

		switch(theCardValue) {
		case "2": 
			cardCounter += 0.22;
		break;
		case "3": 
			cardCounter += 0.2;	
		break;
		case "4": 
			cardCounter += 0.18;		
		break;
		case "5": 
			cardCounter += 0.16;	
		break;
		case "6": 
			cardCounter += 0.14;		
		break;
		case "7": 
			cardCounter += 0.0175;		
		break;
		case "8": 
			cardCounter -= 0.0175;		
		break;
		case "9": 
			cardCounter -= 0.035;
		break;
		case "10": 
			cardCounter -= 0.14;			
		break;
		case "J": 
			cardCounter -= 0.14;			
		break;
		case "Q": 
			cardCounter -= 0.14;
		break;
		case "K": 
			cardCounter -= 0.14;
		break;
		case "A": 
			cardCounter -= 0.30;
			
		break;
		
		
		}
		
		
		return cardCounter;
		
	}
	//gets int from String card value
	public int cardValue(String value) {
		
		
		String theCardValue = value;
		switch(theCardValue) {
		case "2": 
			num = 2;	
		break;
		case "3": 
			num = 3;	
		break;
		case "4": 
			num = 4;		
		break;
		case "5": 
			num = 5;	
		break;
		case "6": 
			num = 6;		
		break;
		case "7": 
			num = 7;		
		break;
		case "8": 
			num = 8;			
		break;
		case "9": 
			num = 9;
		break;
		case "10": 
			num = 10;			
		break;
		case "J": 
			num = 10;			
		break;
		case "Q": 
			num = 10;
		break;
		case "K": 
			num = 10;
		break;
		case "A": 
			num = 11;
			
		break;
		
		
		}
		
		
		return num;
		
	}
	//main
	public static void main(String[] args) {
		
		
		try {
			String IpAddress = args[0];
			String IpPort = args[1];
			Blackjack blackjack = new Blackjack(IpAddress, IpPort);
			boolean split = false;//initializing a boolean that will allow a split if environment is right
			int aceCounter = 0;//initializing an int that counts aces that have been played
			int money = 500;//initializing the starting money
			String value = null;//initializing value of cards in player hand
			String totalValue = null;//initializing total value of all cards that have been played
			char suit;//when suit char is split from the value of card, this holds the suit
			double cardCounter = 0;//initializing double for card counter
			int deckCounter = 0;//initializing int for deck counter
			System.out.println("Time to play");// shows that the program is running
			
			int roundCount = 0;//initializing int for the count of rounds played
			
			
			while(money > 0) {

				
				String command = blackjack.read();		
				command.toLowerCase();
				
//				 ********** login ********** 
				if(command.length() > 4 && command.substring(0,5).equals("login")) {
					String login = "ztleyden:Leyden";
					blackjack.write(login);
					
					
				} // close of login if statement

//				********** Bet, card count, basic strategy ********** 
				if(command.length() > 2 && command.substring(0,3).equals("bet")){
					roundCount++;
					if (deckCounter > 355) {
						deckCounter = 0;
						cardCounter = 0;
						
					}//close of deckCounter if statement
					
					System.out.println("number of rounds: " + roundCount);//Prints number of rounds played
					
					String[] tokens = command.split(":");
					money = Integer.parseInt(tokens[1]);
					System.out.println("Current bankroll: " + money);
					
					
					
					
//holds value of all cards that have been played in all players hands including the dealer
					for(int i = 3; i < tokens.length; i++) {
						String card = tokens[i];
						deckCounter++;
						
						if(card.length() > 2) {
							totalValue = "10" ;
							suit = card.charAt(2);
							
							cardCounter += blackjack.cardCount(card.substring(0,1));
							if(cardCounter > 50 || cardCounter < -50) {
								cardCounter = 0;
							}
							
						} else {
							totalValue = card.substring(0,1);
							suit = card.charAt(1);
							cardCounter += blackjack.cardCount(card.substring(0,1));
							if(cardCounter > 50 || cardCounter < -50) {
								cardCounter = 0;
							}
							
							
						}
					}//close of "value of all cards" for loop
					
//					******** Card Counter ********
					if(money < 200) {
						if(cardCounter < 1.0) {
							blackjack.write("bet:1");
							System.out.println("bet:1");
							}else if(cardCounter >= 1.0 && cardCounter < 2 && money > 2) {
								blackjack.write("bet:2");
								System.out.println("bet:2");
							}else if(cardCounter >= 2.0 && cardCounter < 5 && money > 3) {
								blackjack.write("bet:3");
								System.out.println("bet:3");
							}else {
								if(money >= 4) {
									blackjack.write("bet:4");
									System.out.println("bet:4");
								}else if(money >= 2) {
									blackjack.write("bet:2");
									System.out.println("bet:2");
								}else {
									blackjack.write("bet:1");
									System.out.println("bet:1");
								}
							}
					}
					else if(money >= 200 && money < 400) {
						if(cardCounter < 1.0) {
						blackjack.write("bet:1");
						System.out.println("bet:1");
						}else if(cardCounter >= 1.0 && cardCounter < 2.0) {
							blackjack.write("bet:4");
							System.out.println("bet:4");
						}else if(cardCounter >= 2.0 && cardCounter < 3.0) {
							blackjack.write("bet:5");
							System.out.println("bet:5");
						}else if(cardCounter >= 3.0 && cardCounter < 4.0) {
							blackjack.write("bet:6");
							System.out.println("bet:6");
						}else if(cardCounter >= 4.0 && cardCounter < 5.0) {
							blackjack.write("bet:7");
							System.out.println("bet:7");
						}else {
							blackjack.write("bet:8");
							System.out.println("bet:8");
						}
							
						
					}
					else {
						if(cardCounter < 1.0) {
							blackjack.write("bet:1");
							System.out.println("bet:1");
							}else if(cardCounter >= 1.0 && cardCounter < 2.0) {
								blackjack.write("bet:5");
								System.out.println("bet:5");
							}else if(cardCounter >= 2.0 && cardCounter < 3.0) {
								blackjack.write("bet:11");
								System.out.println("bet:11");
							}else if(cardCounter >= 3.0 && cardCounter < 4.0) {
								blackjack.write("bet:13");
								System.out.println("bet:13");
							}else if(cardCounter >= 4.0 && cardCounter < 5.0) {
								blackjack.write("bet:15");
								System.out.println("bet:15");
							}else {
								blackjack.write("bet:17");
								System.out.println("bet:17");
							}
					}
				
				} //close of "bet" if statement
//				 ********** play, holds card values ********** 

				if(command.length() > 3 && command.substring(0,4).equals("play")) {
					
					
					String[] tokens = command.split(":");
					int dealerCardInt = 0;
					String dealerCard = tokens[2];//holds dealers face up card value
					if(dealerCard.length() > 2) {
						value = "10" ;
						suit = dealerCard.charAt(2);
						dealerCardInt = blackjack.cardValue(dealerCard.substring(0,2));
					} else {
						value = dealerCard.substring(0,1);
						suit = dealerCard.charAt(1);
						dealerCardInt = blackjack.cardValue(dealerCard.substring(0,1));
					}
					
					int total = 0;
					System.out.println("The dealers face up card: " + dealerCard);
					boolean doubleOrSplit = tokens.length == 2;
//					 ********** holds the value of cards in my hand ********** 
					ArrayList<String> playercard = new ArrayList<String>();
					for(int i = 4; i < tokens.length; i++) { 
						playercard.add(tokens[i]);
						String card = tokens[i];
						int j = i + 1;
						if(j < tokens.length) {
						int card1 = blackjack.cardValue(tokens[i].substring(0,1));
						int card2 = blackjack.cardValue(tokens[j].substring(0,1));
						if(card1 == card2) {
							split = true;
						}
						}
						System.out.println("Player Card: " + card);
						
						if(card.length() > 2) {
							value = "10" ;
							suit = card.charAt(2);
							total += blackjack.cardValue(card.substring(0,2));
						} else {
							value = card.substring(0,1);
							suit = card.charAt(1);
							if(suit == 'a' || suit == 'A') {
								aceCounter++;
							}
							total += blackjack.cardValue(card.substring(0,1));
						}

					
					}//closes for loop of cards
					
					
					if(aceCounter == 2) {
						if(doubleOrSplit) {
						blackjack.write("split");
						System.out.println("split");
					}else{
						aceCounter = 1;
					}
						
					}
						if(aceCounter == 0) {
							if(dealerCardInt == 2) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("stand");
									System.out.println("stand");
									}
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("stand");
									System.out.println("stand");
									}
								}else if(total == 15) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 14) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 13) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 12) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 9) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 3) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("stand");
									System.out.println("stand");
									}
								}else if(total == 15) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 14) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 13) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 12) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 9) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 4) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("stand");
									System.out.println("stand");
									}
								}else if(total == 15) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 14) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 13) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 12) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 9) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 5) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("stand");
									System.out.println("stand");
									}
								}else if(total == 15) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 14) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 13) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 12) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 9) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 6) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("stand");
									System.out.println("stand");
									}
								}else if(total == 15) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 14) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 13) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 12) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 9) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 7) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("Hit");
									System.out.println("Hit");
									}
								}else if(total == 15) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 14) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 13) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 12) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 9) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 8) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("Hit");
									}
								}else if(total == 15) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 14) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 13) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 12) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 9) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 9) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("Hit");
									System.out.println("Hit");
									}
								}else if(total == 15) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 14) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 13) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 12) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 9) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 10) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("Hit");
									System.out.println("Hit");
									}
								}else if(total == 15) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 14) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 13) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 12) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 9) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							if(dealerCardInt == 11) {
								if(total == 21) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 20) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 19) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 18) {
									blackjack.write("stand");
									System.out.println("stand");
								}else if(total == 17) {
									blackjack.write("stand");
								 	System.out.println("stand");
								}else if(total == 16) {
									if(doubleOrSplit && split) {
										blackjack.write("split");
										System.out.println("split");
									}else {
									blackjack.write("Hit");
									System.out.println("Hit");
									}
								}else if(total == 15) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 14) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 13) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 12) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 11) {
									if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
								}else if(total == 10) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 9) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 8) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 7) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 6) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 5) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 4) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 3) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else if(total == 2) {
									blackjack.write("Hit");
									System.out.println("Hit");
								}else {
									blackjack.write("stand");
									System.out.println("stand");
								}
							}
							
							
						}
							if(aceCounter == 1) {
								if(dealerCardInt == 2) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										blackjack.write("Double");
										System.out.println("Double");
									}else if(total == 17) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 16) {
										if(doubleOrSplit && split) {
											blackjack.write("split");
											System.out.println("split");
										}else {
										blackjack.write("Hit");
										System.out.println("Hit");
										}
									}else if(total == 15) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 14) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 13) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 9) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 3) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 17) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 16) {
										if(doubleOrSplit && split) {
											blackjack.write("split");
											System.out.println("split");
										}else {
										blackjack.write("Hit");
										System.out.println("Hit");
										}
									}else if(total == 15) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 14) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 13) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 9) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 4) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 17) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 16) {
										if(doubleOrSplit && !split) {
											blackjack.write("Double");
											System.out.println("Double");
											}else if (doubleOrSplit && split) {
												blackjack.write("split");
												System.out.println("split");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 15) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 14) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 13) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 9) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 5) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 17) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 16) {
										if(doubleOrSplit && !split) {
											blackjack.write("Double");
											System.out.println("Double");
											}else if(doubleOrSplit && split) {
												blackjack.write("split");
												System.out.println("split");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 15) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 14) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 13) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 9) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 6) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 18) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 17) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 16) {
										if(doubleOrSplit && !split) {
											blackjack.write("Double");
											System.out.println("Double");
											}else if(doubleOrSplit && split) {
												blackjack.write("split");
												System.out.println("split");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 15) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 14) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 13) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 9) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 7) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 17) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 16) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 15) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 14) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 13) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 9) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 8) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 17) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 16) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 15) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 14) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 13) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 9) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 9) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 17) {
										blackjack.write("Hit");
									 	System.out.println("Hit");
									}else if(total == 16) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 15) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 14) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 13) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 9) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 10) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 17) {
										blackjack.write("Hit");
									 	System.out.println("Hit");
									}else if(total == 16) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 15) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 14) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 13) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
											blackjack.write("Double");
											System.out.println("Double");
											}else {
												blackjack.write("hit");
												System.out.println("Hit");
											}
									}else if(total == 10) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 9) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
								
								if(dealerCardInt == 11) {
									if(total == 21) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 20) {
										blackjack.write("stand");
									 	System.out.println("stand");
									}else if(total == 19) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 18) {
										blackjack.write("stand");
										System.out.println("stand");
									}else if(total == 17) {
										blackjack.write("Hit");
									 	System.out.println("Hit");
									}else if(total == 16) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 15) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 14) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 13) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 12) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 11) {
										if(doubleOrSplit) {
										blackjack.write("Double");
										System.out.println("Double");
										}else {
											blackjack.write("hit");
											System.out.println("Hit");
										}
									}else if(total == 10) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 9) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 8) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 7) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 6) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 5) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 4) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 3) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else if(total == 2) {
										blackjack.write("Hit");
										System.out.println("Hit");
									}else {
										blackjack.write("stand");
										System.out.println("stand");
									}
								}
							}
							

					
				}//closes if statement containing "play"
				
				if(command.length() > 5 && command.substring(0,6).equals("status")){
					
					System.out.println(command);
					
				} 
				
				if(command.length() > 3 && command.substring(0,4).equals("done")) {
					String[] tokens = command.split(":");
					String game_Over = tokens[1];
					System.err.println("Done: " + game_Over);
					break;
				}
				
				
				
				
			}//closes money while loop
		} catch (NumberFormatException | IOException e) {
			System.err.println("ERRor" + e);
			e.printStackTrace();
		}
		
		
		
	}

}

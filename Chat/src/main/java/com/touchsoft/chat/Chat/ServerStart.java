package com.touchsoft.chat.Chat;

public class ServerStart {
	 public static void main( String[] args )
	    {if (ConnactionD_B.getDBConnection()) {
	new Server(8555);}
}
}

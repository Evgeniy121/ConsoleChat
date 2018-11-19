package com.touchsoft.chat.Chat;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class App 
{
    public static void main( String[] args )
    {
    	if (ConnactionD_B.getDBConnection()) {
    		 System.out.println("Соединение с базой данных установленно");
    		 System.out.println("Зарегистрируйтесь");
    		 Scanner sc=new Scanner(System.in);
    		 String comand =sc.nextLine();
    		 if (comand.startsWith("/register")) {
    			 if (comand.contains("agent")) {
    				 	ConnactionD_B.WriteDBAgent("agent",comand.substring(16),"online",Const.Port);				
    					int id =ConnactionD_B.getLastID();
    					new Client(comand.substring(16),"agent",8555, id,"");
    				 }
    				 else 
    			 if (comand.contains("client")) {
    				 ConnactionD_B.WriteDBClient("Client",comand.substring(17),"online");
    				  while(true) {
    					  String firstmassage= sc.nextLine();
    					  if(firstmassage.equals("/exit"))System.exit(0);
    				 int idd=ConnactionD_B.selectFromOnline();
    				 if (idd==-1) {
    					 System.out.println("Извините, все агенты заняты, ожидайте подключение агента ");
    					 while (idd==-1) {
    						idd=ConnactionD_B.selectFromOnline();
    					 }
    				 }
    				 ConnactionD_B.setWork(idd,"work");
    				 new Client(comand.substring(17),"client",Const.Port,idd,firstmassage);
    				} 
			    }
    	
    		 }
    		 System.out.println("Неизвестная команда ");
    	}
    }
}


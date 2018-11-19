package com.touchsoft.chat.Chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;




public class Client {
  private BufferedReader in;
  private PrintWriter out;
  private Socket socket;
  private static final Logger log = Logger.getLogger(Client.class);
  private String role="";
  public Client(String name,String role,int port,int id,String firstmasasge) {
    Scanner scan = new Scanner(System.in);
    this.role=role;
   

    String ip = "127.0.0.1";

    try {
      
      socket = new Socket(ip,port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);

    
      out.println(name);
      System.out.println("Имя в чате "+name);
      out.println(id);
      out.println(firstmasasge);
      log.info(name+" connecting   port:"+port+"  ip:"+ip );
      Resender resend = new Resender();
      resend.start();
      String str = "";
      while (!str.equals("/leave")) {
        if (str.equals("/exit")) { resend.setStop();
        	 if (role.equals("client"))ConnactionD_B.setWork(id,"online");
        	 if (role.equals("agent"))ConnactionD_B.delete(id);
        	 out.println(str);
        	
        	 close();
        	 log.info(name+"close   port:"+port+"  ip:"+ip );
        	 System.exit(0);
        }
        
    	  str = scan.nextLine();
        out.println(str);
       
      }resend.setStop();
      log.info(name+"close   port:"+port+"  ip:"+ip );
      if (role.equals("client")) ConnactionD_B.setWork(id,"online");
      if (role.equals("agent"))ConnactionD_B.delete(id);
      
      close();
    } catch (Exception e) {
     log.error(e);
    } 
    }

  
  private void close() {
    try {
    	
      in.close();
      out.close();
      socket.close();
     
    } catch (Exception e) {
     
    }
  }


  private class Resender extends Thread {

    private boolean stoped;
    
    
    public void setStop() {
      stoped = true;
    }

    
    @Override
    public void run() {
      try {
        while (!stoped) {
          String str = in.readLine();
          System.out.println(str);
        } 
      } catch (IOException e) {
       
        log.error(e);
      }
    }
  }

}
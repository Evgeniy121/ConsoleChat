package com.touchsoft.chat.Chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class Server   {

	private static final Logger log = Logger.getLogger(Server.class);
	private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
	private ServerSocket server;

	public Server(int port) {
		try {
			ConnactionD_B.clearTable();
			server = new ServerSocket(port);
			System.out.println("Срвер запущен");
			while (true) {
				Socket socket = server.accept();

				Connection con = new Connection(socket);
				connections.add(con);
				con.start();
			}
		} catch (IOException e) {
			log.error(e);

		} finally {
			ConnactionD_B.clearTable();
			closeAll();
		}
	}

	private void closeAll() {
		try {
			server.close();

			synchronized (connections) {
				Iterator<Connection> iter = connections.iterator();
				while (iter.hasNext()) {
					((Connection) iter.next()).close();
				}
			}
		} catch (Exception e) {
			ConnactionD_B.clearTable();
		}
	}

	private class Connection extends Thread {
		private BufferedReader in;
		private PrintWriter out;
		private Socket socket;
		private int id;
		private String name = "";
		private String firstmassage = "";

		public Connection(Socket socket) {
			this.socket = socket;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

			} catch (IOException e) {
				log.error(e);
				close();
			}
		}

		@Override
		public void run() {
			try {
				name = in.readLine();
				this.id = in.read();
				firstmassage = in.readLine();
				synchronized (connections) {
					Iterator<Connection> iter = connections.iterator();
					while (iter.hasNext()) {
						Connection i = iter.next();
						if ((id == i.id) && (this != i)) {
							(i).out.println(name + " присоеденился к чату");
							if (!firstmassage.equals(""))
								(i).out.println(name + ":  " + firstmassage);
						}
					}
				}
				String str = "";
				while (true) {
					str = in.readLine();
					if ((str.contains("/exit")) || (str.contains("/leave")))
						break;

					synchronized (connections) {
						Iterator<Connection> iter = connections.iterator();
						while (iter.hasNext()) {
							Connection i = iter.next();
							if ((id == i.id)&& (this != i))
								(i).out.println(name + ": " + str);
						}
					}
				}

				synchronized (connections) {
					Iterator<Connection> iter = connections.iterator();
					while (iter.hasNext()) {
						Connection i = iter.next();
						if ((id == i.id) && (this != i))
							(i).out.println(name+" покинул чат");

					}
				}
			} catch (IOException e) {
				log.error(e);
			} finally {
				close();
			}
		}

		public void close() {
			try {

				in.close();
				out.close();
				socket.close();

				connections.remove(this);

			} catch (Exception e) {
				ConnactionD_B.clearTable();

			}
		}
	}
}
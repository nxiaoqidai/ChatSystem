package server;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		NewServer newServer = new NewServer();
		try {
			newServer.startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

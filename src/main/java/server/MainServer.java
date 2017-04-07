package server;

public class MainServer {

	public static void main(String[] args) {
		Server _server = Server.getInstance();
		_server.start();
	}
}

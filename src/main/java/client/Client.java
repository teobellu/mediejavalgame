package client;

import java.util.logging.Level;
import java.util.logging.Logger;

import util.Constants;
import util.IOHandler;

public class Client extends Thread {

	public Client() {
		_ioHandler = new IOHandler();
	}
	
	@Override
	public void run(){
		//Get UI
		_ioHandler.write("Select your User Interface");
		
		_ioHandler.writeList(Constants.UI_TYPES);
	
		int selection = _ioHandler.readNumberWithinInterval(Constants.UI_TYPES.size() - 1);
		_ui = UIFactory.getUserInterface(selection);
		
		if (_ui == null) {
			_log.log(Level.SEVERE, "Can't get a UserInterface. What's goign on?");
			this.shutdown();
		}
		
		new Thread(_ui).start();
	}
	
//	private void doGame() throws RemoteException {
//		/*TODO se primo player, chiedi se vuole mettere suo file config
//		 * se si, cerca il file
//		 * se lo trovi, manda file al server
//		 * */
//		
//		if(_connectionHandler.addMeToGame()){
//			do {
//				String path = _ui.askForConfigFile();
//				if(!path.isEmpty()){
//					FileReader customConfig = null;
//					try {
//						customConfig = new FileReader(path);
//						
//						StringBuilder sb = new StringBuilder();
//						BufferedReader br = new BufferedReader(customConfig);
//						String line;
//						while((line = br.readLine() ) != null) {
//			                sb.append(line);
//			            }
//						
//						br.close();
//						
//						_connectionHandler.sendConfigFile(sb.toString());
//					} catch (FileNotFoundException e) {
//						_ui.write("File not found.");
//					} catch(IOException e){
//						_ui.write("Invalid file. Using default settings...");
//						//TODO ho tolto un break che c'era qua
//					} finally {
//						if (customConfig != null)
//							try {
//								customConfig.close();
//							} catch (IOException e) {
//								_ui.write("I can't close file!");
//							}
//					}
//				} else{
//					break;
//				}
//			} while (true);
//			//TODO send file
//		}
//		
//		//TODO
//	}
//	
	private void shutdown() {
		_ioHandler.shutdown();
//		_connectionHandler.shutdown();
	}
//
	private final IOHandler _ioHandler;
//	private ConnectionServerHandler _connectionHandler;
	private Logger _log = Logger.getLogger(Client.class.getName());
	private UI _ui;
}

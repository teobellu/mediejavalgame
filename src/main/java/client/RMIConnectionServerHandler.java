package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import misc.ServerRemote;
import util.Constants;

public class RMIConnectionServerHandler extends ConnectionServerHandler {

	@Override
	public void run() {
		try {
			Registry _registry = LocateRegistry.getRegistry();
			String[] str = _registry.list();
			for(String s : str){
				System.out.println(s);
			}
			ServerRemote _serverRMI = (ServerRemote) _registry.lookup(Constants.RMI);
			
			System.out.println("Connessione rmi ok");
			_IS_RUNNING = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String prova(){
		return _serverRMI.prova();
	}
	
	private ServerRemote _serverRMI;
	private Registry _registry;
}

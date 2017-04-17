package client.network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import misc.ServerRemote;
import util.Constants;

public class RMIConnectionServerHandler extends ConnectionServerHandler {

	public RMIConnectionServerHandler(String host, int port) {
		super(host, port);
	}
	
	@Override
	public void run() {
		try {
			Registry _registry = LocateRegistry.getRegistry(_host, _port);
			String[] str = _registry.list();
			for(String s : str){
				System.out.println(s);
			}
			ServerRemote _serverRMI = (ServerRemote) _registry.lookup(Constants.RMI);
			
			_log.info("RMIConnection is up");
			
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
	private final Logger _log = Logger.getLogger(RMIConnectionServerHandler.class.getName());
}

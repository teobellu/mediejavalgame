package misc;

import java.rmi.Remote;

public interface ServerRemote extends Remote {

	public void onConnect();
}

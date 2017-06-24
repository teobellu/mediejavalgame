package client.cli;

import util.IOHandler;

public class RequestHandler extends Thread {
	
	private final IOHandler _ioHandler = new IOHandler();;
	
	public String answer;
	
    @Override
    public void run() {
    	while(true){
    		answer = _ioHandler.readLine(false);
    		_ioHandler.write(answer.toUpperCase());
    	}
    }

    public int check(int max){
    	do{
    		int x;
    		try{
    			x = Integer.parseInt(answer);
    			if (x >= 0 && x <= max){
    				return x;
    			}
    		}catch (Exception e){
    			//System.out.println("NOOOOO");
    		}
    	} while(true);
    }
    
}

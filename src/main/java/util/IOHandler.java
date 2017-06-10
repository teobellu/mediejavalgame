package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jacopo
 *
 */
public class IOHandler {

	public IOHandler() {
		_reader = new BufferedReader(new InputStreamReader(System.in));
		_writer = new PrintWriter(System.out);
	}
	
	
	public String readLine(boolean isEmptyAllowed){
		do {
			try {
				String str = _reader.readLine();
				if(!str.isEmpty()){
					return str;
				} else {
					if(isEmptyAllowed){
						return "";
					} else {
						write("You must write something. An empty line will be rejected");
					}
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, "Something's wrong with readLine(). What's happening?\n", e);
			}
		} while (true);
	}
	
	public void write(String str){
		_writer.println(str);
		_writer.flush();
	}
	
	public int readNumber(){
		do {
			try {
				String str = readLine(false);
				Integer i = Integer.decode(str);
				return i;
			} catch (NumberFormatException e) {
				write("You have to insert a number");
			}
		} while (true);
	}
	
	public int readNumberWithinInterval(int startingPoint, int endingPoint){
		int i;
		do {
			i = readNumber();
			if(i<startingPoint || i>endingPoint){
				write("You must choose between "+startingPoint+" and "+endingPoint);
			} else {
				break;
			}
			
		} while (true);
		
		return i;
	}
	
	public int readNumberWithinInterval(int endingPoint){
		return readNumberWithinInterval(0, endingPoint);
	}
	
	public void shutdown(){
		try {
			_reader.close();
			_writer.close();
		} catch (IOException e) {
			_log.log(Level.WARNING, "Cannot close reader or writer. What's going on?\n", e);
		}
		
	}
	
	private final BufferedReader _reader;
	private final PrintWriter _writer;
	private Logger _log = Logger.getLogger(IOHandler.class.getName());
}

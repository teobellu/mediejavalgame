package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IOHandler {

	public IOHandler() {
		_reader = new BufferedReader(new InputStreamReader(System.in));
		_writer = new PrintWriter(System.out);
	}
	
	/**Read a NON EMPTY line
	 * @return a string
	 */
	public String readLine(){
		do {
			try {
				String str = _reader.readLine();
				if(!str.isEmpty()){
					return str;
				} else {
					write("You must write something. An empty line will be rejected");
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, "Something's wrong with readLine(). What's happening?\n"+e.getStackTrace(), e);
			}
		} while (true);
	}
	
	public void write(String str){
		_writer.println(str);
	}
	
	public int readNumber(){
		do {
			try {
				String str = _reader.readLine();
				Integer i = Integer.decode(str);
				return i;
			} catch (IOException e) {
				_log.log(Level.SEVERE, "Cannot ride a line. What's happening?\n"+e.getStackTrace(), e);
			} catch (NumberFormatException e) {
				write("You have to insert a number");
			}
		} while (true);
	}
	
	private final BufferedReader _reader;
	private final PrintWriter _writer;
	private Logger _log = Logger.getLogger(IOHandler.class.getName());
}

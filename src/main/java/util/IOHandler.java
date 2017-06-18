package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jacopo
 *
 */
public class IOHandler {
	
	private final BufferedReader reader;
	private final PrintWriter writer;
	private Logger log = Logger.getLogger(IOHandler.class.getName());

	public IOHandler() {
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new PrintWriter(System.out);
	}
	
	
	public String readLine(boolean isEmptyAllowed){
		do {
			try {
				String str = reader.readLine();
				if(!str.isEmpty())
					return str;
				else if(isEmptyAllowed)
					return "";
				else
					write("You must write something. An empty line will be rejected");
			} catch (Exception e) {
				log.log(Level.SEVERE, "Something's wrong with readLine(). What's happening?\n", e);
			}
		} while (true);
	}
	
	public void write(String str){
		writer.println(str);
		writer.flush();
	}
	
	/**
	 * Prints a list and associates to each element a number from 0 to n = size() - 1
	 * @param list List to print
	 */
	public void writeList(List<String> list){
		list.forEach(item -> write(list.indexOf(item) + ") " + item));
	}
	
	public int readNumber(){
		do {
			try {
				String str = readLine(false);
				return Integer.decode(str);
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
			reader.close();
			writer.close();
		} catch (IOException e) {
			log.log(Level.WARNING, "Cannot close reader or writer. What's going on?\n", e);
		}
		
	}
}
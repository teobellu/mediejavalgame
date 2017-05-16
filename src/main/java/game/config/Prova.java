package game.config;

import java.io.FileInputStream;
import java.util.Properties;

public class Prova {

	public static void main(String[] args) throws Exception {
		// create and load default properties
		Properties defaultProps = new Properties();
		FileInputStream in = new FileInputStream("defaultProperties");
		defaultProps.load(in);
		in.close();

		// create application properties with default
		Properties applicationProps = new Properties(defaultProps);

		// now load properties 
		// from last invocation
		in = new FileInputStream("appProperties");
		applicationProps.load(in);
		in.close();
	}	
}

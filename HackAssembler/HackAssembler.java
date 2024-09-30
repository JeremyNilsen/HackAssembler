
import java.io.File;


public class HackAssembler {

	// As I've never had to utilize reading and writing files before in Java, I used the following
	// website and javadoc to help me do so;
	// https://www.w3schools.com/java/java_files_create.asp
	// https://www.w3schools.com/java/java_files_read.asp
	// https://docs.oracle.com/javase/8/docs/api/java/io/Writer.html
	public static void main(String[] args) throws Exception {
		
		// Loads the file that was used as an argument
		File file = new File(args[0]);
		
		// Gives the file to be parsed, line by line, then output a similar named .hack file
		Parser parser = new Parser(file);
		
		System.out.println("Assembly Completed");

	}
	
	
	
	

}

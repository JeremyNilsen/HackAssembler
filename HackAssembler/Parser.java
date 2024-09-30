

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class Parser {
	
	private Scanner scanner;
	String curInst;
	SymbolTable table;
	int curVar = 16;
	
	public static String A_INSTRUCTION = "A";
	public static String C_INSTRUCTION = "C";
	public static String L_INSTRUCTION = "L";
	
	
	// The parser follows the API provided by chapter 6.4 of the textbook as well as the chapter
	// 6.3.2 guide to handling symbols.
	public Parser(File file) throws Exception {
		
		// Gets the name of the file being run before the .asm and concatenates the .hack at the end
		String fileName[] = file.getName().split("\\.");
		File newFile = new File(fileName[0] + ".hack");
		
		FileWriter write = new FileWriter(newFile);
		
		table = new SymbolTable();
		
		String instType;
		
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("No file inputted");
			e.printStackTrace();
		}
		
		
		// ---First Pass---
		int lineNumber = -1;
		
		// This loop takes all the instructions and only if it is a label it adds the label address
		// to the symbol table
		while (hasMoreLines()) {
			advance();
			instType = instructionType();
			
			if (instType == A_INSTRUCTION) {
				lineNumber++;
			} else if (instType == C_INSTRUCTION) {
				lineNumber++;
			} else if (instType == L_INSTRUCTION){
				table.addEntry(Symbol(), lineNumber + 1);
			}
		}
		
		
		// In trying to implement the Symbols tables as per the 6.3.2 recommendation, I added
		// the first pass of the parser and then had to reset the scanner to start the second pass.
		// An error that I had for a while after this was that the .hack would return blank and
		// after debugging with system prints at nearly every step, I noticed that no print in
		// the 2nd pass while loop would print and my original usage of the method, 
		// scanner.reset(), was the issue.  As it turns out, reseting the scanner this way does not
		// operate as I expected and re-declaring the scanner fixed this issue.
		scanner = new Scanner(file);
		
		// ---Second Pass---
		while (hasMoreLines()) {
			advance();
			instType = instructionType();
			
			if (instType == A_INSTRUCTION) {
				
				curInst = addressToBinary(curInst);
				write.write(curInst);
				write.write("\r\n");
				
			} else if (instType == C_INSTRUCTION) {
				Code code = new Code(dest(), comp(), jump());
				
				write.write(code.getCode());
				write.write("\r\n");
				
			}
		}

		write.close();
	}
	
	
	
	public boolean hasMoreLines() {
		return scanner.hasNextLine();
	}
	
	// Following the API provided by chapter 6.4 of the textbook the following methods are implemented
	// to said specifications.
	public void advance() {
		if (hasMoreLines()) {
			curInst = scanner.nextLine();
			curInst = clean(curInst);
			
		}
		
	}
	
	public String instructionType() {
		if (curInst.startsWith("@")) {
			return A_INSTRUCTION;
		} else if (curInst.indexOf("=") != -1 || curInst.indexOf(";") != -1 ) {
			return C_INSTRUCTION;
		} else if (curInst.length() != 0){
			return L_INSTRUCTION;
		} else {
			return null;
		}
		
	}
	
	public String Symbol() {
		
		if (instructionType() == A_INSTRUCTION) {
			return addressToBinary(curInst);
		} else {
			return labelTrim(curInst);
		}
		
		
	}
	
	// As per the specifications in figure 6.2, if the dest is omitted, then so is '='
	// So that means that if there is equal sign, then there is no dest
	public String dest() {
		int destI = curInst.indexOf("=");
		
		if (destI == -1) {
			return "null";
		}
		
		return curInst.substring(0, destI);	
	}
	
	// Initially I thought that the comp was always between the '=' and ';' so
	// I was originally skipping all the jumps that don't have a dest.  Releasing this
	// through system prints, I reread the figure 6.2 specifications and now have compBeg
	// start at the start of the instruction if there is no dest.
	public String comp() {
		int compBeg = curInst.indexOf("=");
		if (compBeg == -1) {
			compBeg = 0;
		} else {
			compBeg++;
		}
		
		int compEnd;
		if (curInst.indexOf(";") == -1) {
			compEnd = curInst.length();
		} else {
			compEnd = curInst.indexOf(";");
		}
		
		
		return curInst.substring(compBeg, compEnd);
	}
	
	// Like above, following the figure 6.2 definition of where in the instruction jump
	// is located, it's after the ';' and if there's not a semicolon then there is no jump
	public String jump() {
		int jumpStart = curInst.indexOf(";");
		
		if (jumpStart == -1) {
			return "null";
		} else {
			jumpStart++;
		}
		return curInst.substring(jumpStart);
	}
	
	
	
	
	
	// To ignore white spaces and comments of next Instruction
	// Used the following to learn how to remove spaces from the instruction
	// https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java
	
	// For ignoring comments I initially had the following:
	/*
	 * int comment = inst.indexOf("//");
	 * if (comment == -1) {
	 * 	  comment = 0;
	 * }
	 */
	// However the issue with this implementation that I eventually found was that
	// if there was no comment in the line, the substring I made would be from index 0 to 0.
	// To solve this I found a better String method from the javadocs and used the following
	// guide to properly implement the split method.
	// https://www.geeksforgeeks.org/split-string-java-examples/#
	
	public String clean(String inst) {
		String[] cleanInst;
		
		inst.replaceAll("\\s","");
		cleanInst = inst.split("//");
		
		String trimInst = cleanInst[0].trim();
				
		return trimInst;
	}
	
	
	// https://www.baeldung.com/java-check-string-number
	public String addressToBinary(String inst) {
		String[] dec = inst.split("@");
		String target = dec[1];
		int address;
		
		
		// I was unsure of how to determine whether a string was made up of integers or not so
		// after following this guide:
		// https://www.baeldung.com/java-check-string-number
		// I arrived at the following try-catch.  Essentially it is an if-else statement but rather
		// than if (isInt) else, it assumes the string is an int and if there is an error, rather than 
		// stopping the program, it moves on to the catch.  If the instruction is an address, it will
		// go strait to turning it to binary, else if it is a label, it will first check if it is already
		// in the Symbol Table and will either get the address or add it to the table if it's not there.
		try {
	        address = Integer.parseInt(target);
	    } catch (NumberFormatException e) {
	    	
	    	if (!table.contains(labelTrim(target))) {
	    		table.addEntry(target, curVar);
	    		curVar++;
	    	}
	    	
	        address = table.getAddress(target);
	    }
		
		inst = Integer.toBinaryString(address);
		
		// I didn't know of any simple methods of making the binary extent to 16 bits so instead
		// I just concatenated 0's to the front of the binary until the length was 16-bits
		while (inst.length() != 16) {
			inst = "0" + inst;
		}
		
		return inst;
		
	}
	
	// Helper method that trims off the left and right parentheses from the label
	public String labelTrim(String label) {
		int start = label.indexOf("(") + 1;
		int end = label.indexOf(")");
		
		if (start != -1 && end != -1) {
			label = label.substring(start, end);
		}
		
		return label;
	}	
	
}
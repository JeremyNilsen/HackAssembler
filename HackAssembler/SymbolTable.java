
import java.util.Hashtable;


// Following the API given in chapter 6.4 of the textbook, the Symbol table stores all the labels and
// variables found during the first pass of the .asm file and assigns them an integer number depicting
// their address in RAM/ROM
public class SymbolTable {
	
	Hashtable<String, Integer> table;
	
	// Intializer that adheres to the chapter 6.4 Symbol Table API specifications
	// Creates a new hash table and populates it with Hack's pre-defined Symbols
	
	// Originally, I chose the dictionary data structure because after viewing the javadocs for
	// Hash table, map, and dictionary, it appeared to be the most simple.  However in the javadocs, it
	// states that the dictionary is obsolete so I instead moved on and implemented using a hash table
	// instead because I knew that it was a child class of dictionary so it would have the same methods.
	public SymbolTable() {
		table = new Hashtable<>();
		populate();
	}
	
	// Method that adheres to the chapter 6.4 Symbol Table API specifications
	// Adds the symbol and address to the hash table
	public void addEntry(String symbol, int address) {
		table.put(symbol, address);
	}
	
	// Method that adheres to the chapter 6.4 Symbol Table API specifications
	// Returns whether or not the given symbol is already in the hash table or not
	public boolean contains(String symbol) {
		return table.containsKey(symbol);
	}
	
	// Method that adheres to the chapter 6.4 Symbol Table API specifications
	// Returns the integer value associated with the Symbol argument
	public int getAddress (String symbol) {
		return table.get(symbol);
	}
	
	
	// Method that pre-populates the Symbol table with Hack's pre-defined symbols as stated
	// in chapter 4.2.4 of the textbook
	public void populate() {
		for (int i = 0; i < 16; i++) {
			addEntry("R" + i,i);
		}
		addEntry("SCREEN", 16384);
		addEntry("KBD", 24576);
		addEntry("SP", 0);
		addEntry("LCL", 1);
		addEntry("ARG", 2);
		addEntry("THIS", 3);
		addEntry("THAT", 4);
		
	}
	
	
}

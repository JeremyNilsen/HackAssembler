
public class Code {
	
	private String code;
	
	// Concatenates the groups of bits together to form the 16-bit C-instruction, a issue that
	// I had here was that I concatenated the bits in the order of: leading bits, dest, comp, jump.
	// This was overlooked from my personal tests until I used the Hack assembler to compare my .hack files.
	// Whence I saw that my strings of bits were correct but misordered I looked here first and as it turned
	// out my dest and comp bits were in eachother's place.  Swapping them passed the comparison tests.
	
	// The Code class's methods were made according to the API provided in chapter 6.4 of the textbook
	public Code(String dest, String comp, String jump) {
		String finalCode = "111" + comp(comp) + dest(dest) + jump(jump);
		this.code = finalCode;
	}
	
	
	// From class, it was shown that an alternative of a large if-else statement was a switch
	// After researching how to use a switch, I followed the following link to learn how to use a
	// switch for selecting the proper bit output
	// https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
	
	//Mapped instructions to bits according to figure 6.2's specifications
	public String dest(String dest) {
		
		// In PongL.asm, there is a case of the dest being MD rather than DM that caused a comparison
		// failure against the Hack's Assembler, to prevent this from causing further incorrections, I
		// added every order that each dest can be input in while keeping the same output bits which solved
		// this issue
		switch (dest) {
		case "M":
			return "001";
		case "D":
			return "010";
		case "DM":
			return "011";
		case "A":
			return "100";
		case "AM":
			return "101";
		case "AD":
			return "110";
		case "ADM":
			return "111";
		case "MD":
			return "011";
		case "MA":
			return "101";
		case "DA":
			return "110";
		case "AMD":
			return "111";
		case "MAD":
			return "111";
		case "MDA":
			return "111";
		case "DAM":
			return "111";
		case "DMA":
			return "111";
		default:
			return "000";
		}

	} 
	
	// Like above, as opposed to a large if-else statement, I used switch and mapped
	// according to figure 6.2's specifications
	public String comp(String comp) {
		
		switch (comp) {
		case "0":
			return "0101010";
		case "1":
			return "0111111";
		case "-1":
			return "0111010";
		case "D":
			return "0001100";
		case "A":
			return "0110000";
		case "!D":
			return "0001101";
		case "!A":
			return "0110001";
		case "-D":
			return "0001111";
		case "-A":
			return "0110011";
		case "D+1":
			return "0011111";
		case "A+1":
			return "0110111";
		case "D-1":
			return "0001110";
		case "A-1":
			return "0110010";
		case "D+A":
			return "0000010";
		case "D-A":
			return "0010011";
		case "A-D":
			return "0000111";
		case "D&A":
			return "0000000";
		case "D|A":
			return "0010101";
		case "M":
			return "1110000";
		case "!M":
			return "1110001";
		case "-M":
			return "1110011";
		case "M+1":
			return "1110111";
		case "M-1":
			return "1110010";
		case "D+M":
			return "1000010";
		case "D-M":
			return "1010011";
		case "M-D":
			return "1000111";
		case "D&M":
			return "1000000";
		case "D|M":
			return "1010101";
		default:
			return null;
		}
		
	}
	
	// Like above, as opposed to a large if-else statement, I used switch and mapped
	// according to figure 6.2's specifications
	public String jump(String jump) {
		
		switch (jump) {
		case "JGT":
			return "001";
		case "JEQ":
			return "010";
		case "JGE":
			return "011";
		case "JLT":
			return "100";
		case "JNE":
			return "101";
		case "JLE":
			return "110";
		case "JMP":
			return "111";
		default:
			return "000";
		
		}
		
	}
	
	public String getCode() {
		return this.code;
	}
	
	
}

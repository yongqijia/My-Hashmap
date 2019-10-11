/**
 * @author Yongqi Jia
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The Class cryptograms.
 */
public class cryptograms {
	
	/**
	 * The main method. 
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		String target = getquote(); 
        ArrayMap<Character, Character> cryptogram = new ArrayMap<Character, Character>();
        String encrypted_quotes = new String();
        cryptogram = random_key();
        encrypted_quotes = encryption(cryptogram, target);
        decryption(encrypted_quotes, cryptogram, target);
	}
	
	/**
	 * Gets the quote.
	 * Get the quote from the input file.
	 *
	 * @return the quote
	 */
	public static String getquote() {
		Scanner input = null;
		try {
            input = new Scanner(new File("quotes.txt"));
		}catch(FileNotFoundException ex) {
            System.out.println("ERROR: File not found.");
            System.exit(1);
		}
        ArrayList<String> quotes_array = new ArrayList<>();
        while (input.hasNextLine()) {
            String quotes = input.nextLine();
            quotes_array.add(quotes);
        }
        Random rand = new Random();
        if(quotes_array.size() == 0) {
        	System.out.println("Error: File is empty!");
        	System.exit(1);
        }
        int n = rand.nextInt(quotes_array.size());
        String target = new String();
        target = quotes_array.get(n).toUpperCase();
        input.close();
		return target;
	}

	/**
	 * decryption is using additional ArrayMap to store the user guesses and mapping to their ArrayMap
	 * and show them their progress by using the ArrayMap to replace the encrypted characters with their
	 * guess ones.
	 * 
	 * @param encrypted_quotes the string that only contain punctuation
	 * 
	 * @param cryptogram the ArrayMap that contain the correct the mapping.
	 * 
	 * @param target the quotes that has already been encrypted.
	 */
	public static void decryption(String encrypted_quotes, ArrayMap<Character, Character> cryptogram, String target) {
		ArrayList<Character> guess_str = new ArrayList<Character>();
		for(int i = 0; i < encrypted_quotes.length(); i++) {
			if(Pattern.matches("\\p{Punct}", Character.toString(encrypted_quotes.charAt(i)))) {
				guess_str.add(i, encrypted_quotes.charAt(i));
			}else {
				guess_str.add(i, ' ');
			}
		}
		// Create a string to store every guess and show the progress.
		
		Boolean check = true;
		format_print(guess_str, encrypted_quotes);
		while(check) {
			Character replace = null;
			Character replacement = null;
			Scanner input = new Scanner(System.in);
		    System.out.print("Enter a command (help to see commands): ");
		    String[] command = input.nextLine().toUpperCase().trim().split(" ");
		    if(command[0].equals("REPLACE") && command[2].equals("BY") && command.length == 4) {
		    	check_valid(command[1], command[3]);
		    	replace = command[1].charAt(0);
		    	replacement = command[3].charAt(0);
		    }else if(command.length == 3) {
		    	check_valid(command[0], command[2]);
		    	replace = command[0].charAt(0);
		    	replacement = command[2].charAt(0);
		    }else if(command.length == 1 && command[0].equals("FREQ")) {
		    	System.out.println();
		    	print_freq(encrypted_quotes);
		    	System.out.println();
		    	format_print(guess_str, encrypted_quotes);;
		    	continue;
		    }else if(command.length == 1 && command[0].equals("HINT")) {
		    	System.out.println();
		    	hint(cryptogram, encrypted_quotes, guess_str, target);		    	
		    	continue;
		    }else if(command.length == 1 && command[0].equals("EXIT")) {
		    	System.out.println("\nGame is over.\n");
		    	System.exit(1);
		    }else if(command.length == 1 && command[0].equals("HELP")) {
		    	help();
		    	
		    	continue;
		    }else {
		    	System.out.println("\nError: This is not an options. Please enter \"help\" for all vaild commands.\n");
		    	continue;
		    }	
		    
		    // Update the ArrayMap which is used to store the user guesses.
		    for(int i = 0; i < encrypted_quotes.length(); i++) {
		    	if(encrypted_quotes.charAt(i) == replace) {
		    		guess_str.set(i, replacement);
		    	}
		    }
	
		    System.out.println();
		    String temp = "";
		    for(int i = 0; i < guess_str.size(); i++) {
		    	temp += guess_str.get(i);
		    }
		    format_print(guess_str,encrypted_quotes);
		    
		    // check whether user complete the decryption.
		    if(temp.equals(target)) {
		    	check = false;
		    }     
		}
		System.out.println(encrypted_quotes + "\n");
		System.out.println("You got it!");
	}

	/**
	 * print all valid command.
	 */
	public static void help() {
		System.out.print("\n1. replace X by Y – replace letter X by letter Y in our attempted solution\n");
    	System.out.print("   X = Y          – a shortcut for this same command\n");
    	System.out.print("2. freq           – Display the letter frequencies in the encrypted quotation\n");
    	System.out.print("3. hint           – display one correct mapping that has not yet been guessed\n");
    	System.out.print("4. exit           – Ends the game early\n");
    	System.out.print("5. help           – List these commands\n");
    	System.out.println();	
	}

	/**
	 * hint function is to display one correct mapping that has not yet been guessed.
	 * 
	 * First find one character that has not been guessed, and then, using cryptogram to get the correct
	 * mapping and display. Finally, check whether user is get the whole mapping correct.
	 * 
	 * @param cryptogram the ArrayMap that contain the correct the mapping.
	 * 
	 * @param encrypted_quotes the string that only contain punctuation
	 * 
	 * @param guess_str the list store the user guess
	 * 
	 * @param target the quotes that has already been encrypted.
	 */
	public static void hint(ArrayMap<Character, Character> cryptogram, String encrypted_quotes, 
			ArrayList<Character> guess_str, String target) {
		char need_replace = ' ';
		for(int i = 0; i < encrypted_quotes.length(); i++) {
			if('A' <= encrypted_quotes.charAt(i) && 'Z' >= encrypted_quotes.charAt(i)) {
				if(guess_str.get(i) == ' ') {
					need_replace = encrypted_quotes.charAt(i);
					break;
				}
			}
		}
		for(int i = 0; i < guess_str.size(); i++) {
			if(encrypted_quotes.charAt(i) == need_replace) {
				for(Character letter : cryptogram.keySet()) {
	    			if(cryptogram.get(letter) == need_replace) {		    				
	    				guess_str.set(i, letter);
	    			}
	    		}
			}
		}
		String temp = "";
	    for(int i = 0; i < guess_str.size(); i++) {
	    	temp += guess_str.get(i);
	    }
	    format_print(guess_str, encrypted_quotes);
		if(temp.equals(target)) {
			System.out.println("\nYou got it!");
			System.exit(1);
	    } 
	}
	
	/**
	 * format_print function is to support arbitrarily long quotes. 
	 * If the quotes or guess are more than 80 characters, first print the 80 characters per line splits by
	 * whitespace or punctuation. And then, print the rest.
	 * 
	 * @param guess_str the list store the user guess
	 * 
	 * @param encrypted_quotes the string that only contain punctuation
	 */
	private static void format_print(ArrayList<Character> guess_str, String encrypted_quotes) {
		String temp_guess_str = "";
		for(int i = 0; i < guess_str.size(); i++) {
			temp_guess_str += guess_str.get(i);
		}
		String temp_encrypted_quotes = encrypted_quotes;	
		while(temp_encrypted_quotes.length() > 80) {
			int check = 80;
			while(temp_encrypted_quotes.charAt(check) != ' ') {
				check -= 1;
			}
			System.out.println(temp_guess_str.substring(0, check + 1));
			System.out.println(temp_encrypted_quotes.substring(0, check + 1) + "\n");
			temp_guess_str = temp_guess_str.substring(check + 1);
			temp_encrypted_quotes = temp_encrypted_quotes.substring(check + 1);	
		}
		System.out.println(temp_guess_str);
		System.out.println(temp_encrypted_quotes);	
	}
	
	/**
	 * print_freq is to display the letter frequencies in the encrypted quotation.
	 * First create an additional ArrayMap to store the frequency. Then loop the encrypted_quotes
	 * to find the frequency. Finally, print the frequency 7 per line for 4 lines.
	 * 
	 * @param encrypted_quotes the string that has already been encrypted.
	 */
	public static void print_freq(String encrypted_quotes) {
		List<Character> temp = new ArrayList<Character>();
		ArrayMap<Character, Integer> freq = new ArrayMap<Character, Integer>();
		for(int i = 0; i < 26; i++){
			temp.add((char)(65 + i));
		}
		for(int i = 0; i < temp.size(); i++) {
			freq.put(temp.get(i), 0);
		}
		for(char letter : freq.keySet()) {
			for(int j= 0; j < encrypted_quotes.length(); j++) {
				if(!Pattern.matches("\\p{Punct}", encrypted_quotes.substring(j, j+1))) {
					if(Character.toString(letter).equals(encrypted_quotes.substring(j, j+1))) {
						freq.put(letter, freq.get(letter)+1);
					}
				}
			}
		}
		int size = 1;
		for(Character letter : freq.keySet()) {
			System.out.print(letter + ": " + freq.get(letter) + " ");
			if(size % 7 == 0) {	
				System.out.println();
			}
			size +=  1;
		}
		System.out.println();
	}

	/**
	 * check_valid function is to check user's input whether or not is valid.
	 * 
	 * @param which	replace letter
	 * 
	 * @param replacement replacement letter
	 */
	public static void check_valid(String which, String replacement) {
		if(which.length() != 1 || replacement.length() != 1) {
	    	System.out.println("\nError: Can't replce multi letters at on time. Only one letter is allowed.\n");	
	    }
	    if(Pattern.matches("\\p{Punct}", replacement) || Pattern.matches("\\p{Punct}", which)) {
	    	System.out.println("\nError: Punctuation is not allowed.\n");
    	}
		
	}

	/**
	 * Encrypt the quotes that randomly chosen.
	 * 
	 * Create a new string to store encrypted quotes. Keep the same punctuation and only encrypt letter.
	 * 
	 * @param cryptogram the ArrayMap the contain the correct mapping.
	 * 
	 * @param target the quotes that randomly chosen.
	 * 
	 * @return the encrypted string 
	 */
	public static String encryption(ArrayMap<Character, Character> cryptogram, String target) {
		String encrypted = new String();
		for(int i = 0; i < target.length(); i++) {
			if(target.charAt(i) == ' ') {
				encrypted += ' ';
			}else if(Pattern.matches("\\p{Punct}", Character.toString(target.charAt(i)))){
				encrypted += Character.toString(target.charAt(i));
			}else {
				encrypted += cryptogram.get(target.charAt(i));
			}
		}
		return encrypted;
	}
	
	/**
	 * Create the random encryption key and store in a ArrayMap.
	 * 
	 * First create a ArrayMap that contain alphabetical order.
	 * Using shuffle to create mappings.
	 * 
	 * @return a ArrayMap contain correct mapping
	 */
	private static ArrayMap<Character, Character> random_key() {
		List<Character> alpha = new ArrayList<Character>(); 
		List<Character> temp = new ArrayList<Character>();
		for(int i = 0; i < 26; i++){
			temp.add((char)(65 + i));
			alpha.add((char)(65 + i));
		}
		ArrayMap<Character, Character> substitution_cypher = new ArrayMap<Character, Character>();
		int count = 0;
		while(count < 26) {
			count = 0;
			Collections.shuffle(alpha);
			for(int i = 0; i < 26; i++) {
				if(alpha.get(i) != temp.get(i)) {
					count += 1;
				}
			}
		}
		for(int i = 0; i < temp.size(); i++) {
			substitution_cypher.put(temp.get(i), alpha.get(i));
		}	
		return substitution_cypher;	
	}
}
package org.example;


import java.nio.file.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;



  //==========//
 // Main App //
//==========//

public class App {
    public static void main(String[] args) {
		
		// JSON de- and encoder
		ObjectMapper jsonMapper = new ObjectMapper();

		//
		Scanner scanner = new Scanner(System.in);

		// make ~/.local/share/Questboard/ if neccessary
		Path gameData = Path.of(System.getProperty("user.home"), ".local", "share", "Questboard");
		String[] folders = { "saveData" };
		try {
			for( String i : folders ) {
				Files.createDirectories(gameData.resolve(i));
			}
		} catch(IOException e) { e.printStackTrace(); }

		// create the save file index if needed
		Path indexFile = gameData.resolve(folders[0]).resolve("index.json");
		saveFileIndex index = new saveFileIndex();
		try{
			if(!Files.isRegularFile(indexFile)) {
				jsonMapper.writeValue(indexFile.toFile(), index);
			} else {
				index = jsonMapper.readValue(indexFile.toFile(), saveFileIndex.class);
			}
		} catch( Exception e ) { e.printStackTrace(); }



 		// taking the args and turning them into commands
		try {
			switch ( args[0] ) {
				case("new"):
					System.out.println("currently not implementex ;P"); //save do $XDG_DATA_HOME/Questboard/save_files later on

					break;
				case("help"):
					printHelp();
					break;
				default:
					System.out.print("""
						Argument not recognised
						Following arguments are available:


						""");
					printHelp();
			}
		}
		catch( ArrayIndexOutOfBoundsException e ) {
			System.out.print("""
				No arguments passed
				Following arguments are available:

				""");
			printHelp();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}

    }



	// Help screen
	static void printHelp() {
		System.out.print("""
			help	prints this help screen
			new		create new save; not functional rn ;P
			
			testJson
			""");
	}
	
	// taking input with a prompt
	static String userInput(String prompt, Scanner scan){
		System.out.print( "[" + prompt + "]> " );
		return scan.nextLine();
	}
	//taking input with fallback prompt
	static String userInput(Scanner scan){ System.out.print("[Awaiting input]> "); return scan.nextLine(); }
}


// Class to Index the amount of saveFiles present, their names, aswell as their file paths
class saveFileIndex {

	//array holding saveFile objects with information about the save files
	public ArrayList<saveFile> index = new ArrayList<saveFile>();
	
	// saveFile class
	public static class saveFile {
		public String name;
		public final int id;
		public final String file;

		// constructor
		private saveFile(String fileName, int ID) {
			this.name = fileName;
			this.id = ID;
			this.file = "Save_" + id;

		}

		// constructor for Jackson, to propperly map all json values
		@JsonCreator private saveFile(@JsonProperty("name") String name, @JsonProperty("id") int id, @JsonProperty("file") String file){
			this.name = name;
			this.id = id;
			this.file = file;
		}

	}

	// method to find the first Free ID
	private int findFreeID() {
		for (int n = 1; ; n++) {
			int candidate = n;
			if (index.stream().noneMatch(s -> s.id == candidate)) {
				return candidate;
			}
		}
	}

}

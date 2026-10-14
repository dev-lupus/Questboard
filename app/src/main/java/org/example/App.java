package org.example;


import java.nio.file.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;


public class App {

	// JSON de- and encoder
	public static ObjectMapper jsonMapper = new ObjectMapper();

	// user input reader
	public static Scanner scanner = new Scanner(System.in);

	// game data folder
	public static Path gameData = Path.of(System.getProperty("user.home"), ".local", "share", "Questboard");
	public static String[] folders = { "saveData" };

	// save data handling
	public static Path indexFile = gameData.resolve(folders[0]).resolve("index.json");
	public static saveFileIndex index = new saveFileIndex();



    public static void main(String[] args) {

		// make ~/.local/share/Questboard/ if neccessary
		try {
			for( String i : folders ) {
				Files.createDirectories(gameData.resolve(i));
			}
		} catch(IOException e) { e.printStackTrace(); }

		// create the save file index if needed
		try{
			if(!Files.isRegularFile(indexFile)) {
				jsonMapper.writeValue(indexFile.toFile(), index);
			} else {
				index = jsonMapper.readValue(indexFile.toFile(), saveFileIndex.class);
			}
		} catch( Exception e ) { e.printStackTrace(); }



 		// taking the args and turning them into commands
		try { switch ( args[0] ) {
			case("new"):
				newSave();
				break;
			case("list"):
				for(saveFileIndex.saveFile s : index.index) { System.out.println( "=={ " + s.id + " - " + s.name + " }==" ); }
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
		} } catch( ArrayIndexOutOfBoundsException e ) {
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
			new		create new save
			list	list save files
			""");
	}
	
	// taking input with a prompt
	static String userInput(String prompt){
		System.out.print( "[" + prompt + "]> " );
        return scanner.nextLine();
	}
	// taking input with fallback prompt
	static String userInput(){ System.out.print("[Awaiting input]> "); return scanner.nextLine(); }

	// making a new save File
	static void newSave() {
		index.index.add(new saveFileIndex.saveFile(userInput("input name for new save"), index.findFreeID()));
		try{
			jsonMapper.writeValue(indexFile.toFile(), index);
			Files.createDirectories(gameData.resolve(folders[0]).resolve(index.index.getLast().file));
		} catch(Exception e){ e.printStackTrace(); }
	}
}



// Class to Index the amount of saveFiles present, their names, as well as their file paths
class saveFileIndex {

	//array holding saveFile objects with information about the save files
	public ArrayList<saveFile> index = new ArrayList<saveFile>();



	// saveFile class
	public static class saveFile {
		public String name;
		public final int id;
		public final String file;


		// constructor
		public saveFile(String fileName, int ID) {
			this.name = fileName;
			this.id = ID;
			this.file = "Save_" + id;

		}
		// constructor for Jackson, to properly map all json values
		@JsonCreator private saveFile(@JsonProperty("name") String name, @JsonProperty("id") int id, @JsonProperty("file") String file){
			this.name = name;
			this.id = id;
			this.file = file;
		}
	}



	// method to find the first Free ID
	public int findFreeID() {
		for (int n = 1; ; n++) {
			int candidate = n;
			if (index.stream().noneMatch(s -> s.id == candidate)) {
				return candidate;
			}
		}
	}

}

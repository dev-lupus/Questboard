package org.example;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) {

		ObjectMapper jsonMapper = new ObjectMapper();

		try {
 			// taking the args and turning them into commands
			switch ( args[0] ) {
				case("testJson"):
					try{
						test tester = jsonMapper.readValue(new File("/home/lupus/test.json"), test.class);
						System.out.println(tester.message());
						System.out.println(tester.number());
					} catch (Exception e){
						System.out.println("Error 69 420: Jason not found");
						e.printStackTrace();
					}
					break;
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

    }

	static void printHelp() {
		System.out.print("""
			help	prints this help screen
			new		create new save; not functional rn ;P
			
			testJson
			""");
	}

}

record test( String message, int number) {}

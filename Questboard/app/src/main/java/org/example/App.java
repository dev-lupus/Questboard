package org.example;

public class App {
    public static void main(String[] args) {
		
		try {
 			// taking the args and turning them into commands
			switch ( args[0] ) {
				case("new"):
					System.out.println("currently saving to ~/QuestboardTest"); //save do $XDG_DATA_HOME/Questboard/save_files later on
				case("help"):
					printHelp();
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
			""");
	}

}

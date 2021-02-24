package dbms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public enum MiniDBMS {
	INSTANCE;

	private final Scanner scanner = new Scanner(System.in);

	public void open(DataBaseManagement dataBaseManager) 
			throws FileNotFoundException, ClassNotFoundException, IOException {
		
		while (true) {
			System.out.print("miniDBMS > ");
			String userInput = readLine();

			if (userInput.equals("exit"))
				close(dataBaseManager);
			else {
				Parser parser = Parser.of(extractQueryName(userInput));
				parser.parse(userInput, dataBaseManager);
			}
		}

	}
	
	private String extractQueryName(String userInput) {
		return userInput.split(" ")[0];
	}

	private void close(DataBaseManagement dataBaseManager) 
			throws FileNotFoundException, ClassNotFoundException, IOException {
		
		dataBaseManager.finish();
		scanner.close();
		System.exit(0);
	}

	private String readLine() {
		return scanner.nextLine();
	}

}

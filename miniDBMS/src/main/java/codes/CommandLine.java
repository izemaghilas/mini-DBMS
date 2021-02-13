package codes;

import java.util.Scanner;


public enum CommandLine {
	INSTANCE;

	private final Scanner scanner = new Scanner(System.in);

	/**
	 * start a prompt command line
	 */
	public void openPrompt(DataBaseManagement dataBaseManager) {
		while (true) {
			System.out.print("miniDBMS > ");
			String command = readLine();

			if (command.equals("exit"))
				closePrompt(dataBaseManager);
			else {
				Parser parser = Parser.of(extractCommandName(command));
				parser.parse(command, dataBaseManager);
			}
		}

	}
	
	private String extractCommandName(String command) {
		return command.split(" ")[0];
	}

	private void closePrompt(DataBaseManagement dataBaseManager) {
		dataBaseManager.finish();
		scanner.close();
		System.exit(0);
	}

	private String readLine() {
		return scanner.nextLine();
	}

}

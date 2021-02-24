package dbms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer; 

public class Parser {
	private String queryName;
	private BiConsumer<String, DataBaseManagement> parse;

	private Parser(String queryName, BiConsumer<String, DataBaseManagement> parse) {
		this.queryName = queryName;
		this.parse = parse;
	}
	
	public void parse(String query, DataBaseManagement dataBaseManager) {
		parse.accept(query, dataBaseManager);
	}
	
	private static List<String> extractQueryArguments(String query) {
		String[] args = query.split(" ");
		List<String> array = new ArrayList<>();
		for (int i = 1; i < args.length; i++)
			array.add(args[i]);
		return array;
	}
	
	
	private final static Parser CREATE_PARSER = new Parser(
			"create", 
			(query, dataBaseManager) -> {
				List<String> queryArguments = extractQueryArguments(query);
				int numberOfRelations = Integer.parseInt(queryArguments.get(1));
				
				try {
					
					dataBaseManager.createRelation(
							queryArguments.get(0), 
							numberOfRelations, 
							queryArguments.subList(2, queryArguments.size())
						);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	
	private final static Parser INSERT_PARSER = new Parser(
			"insert", 
			(query, dataBaseManager)->{
				
				List<String> queryArguments = extractQueryArguments(query);
				
				try {
					
					dataBaseManager.insertRecord(
							queryArguments.get(0), 
							queryArguments.subList(1, queryArguments.size())
						);
						
				} catch(FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			});
	
	private final static Parser CLEAN_PARSER = new Parser(
			"clean", 
			(query, dataBaseManager)->{
				try {
				
					dataBaseManager.clean();
				
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	
	private final static Parser FILL_PARSER = new Parser(
			"fill",
			(query, dataBaseManager)->{
				
				List<String> queryArguments = extractQueryArguments(query);
				String relationName = queryArguments.get(0);
				String csvFile = queryArguments.get(1);
				
				try(BufferedReader csvFileReader = new BufferedReader(new FileReader(csvFile))){
					String values="";
					while( (values=csvFileReader.readLine() ) != null ) {
						List<String> recordValues = List.of(values.split(","));
						dataBaseManager.insertRecord(relationName, recordValues);
					}
					
					csvFileReader.close();
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			});
	
	private final static Parser SELECTALL_PARSER = new Parser(
			"selectall",
			(query, dataBaseManager)->{
				List<String> queryArguments = extractQueryArguments(query);
				String relationName = queryArguments.get(0);
				try {
					
					String output = dataBaseManager.getRecords(relationName);
					System.out.println(output);
					
				} catch(FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	
	private final static Parser SELECT_PARSER = new Parser(
			"select",
			(query, dataBaseManager)->{
				List<String> queryArguments = extractQueryArguments(query);
				String relationName = queryArguments.get(0);
				int columnIndex = Integer.parseInt(queryArguments.get(1))-1;
				String value = queryArguments.get(2);
				
				try {
					
					String output = dataBaseManager.getFilteredRecords(relationName, columnIndex, value);
					System.out.println(output);
	
				} catch(FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			});
	
	
	private final static List<Parser> parsers=List.of(
			CREATE_PARSER, 
			INSERT_PARSER, 
			CLEAN_PARSER, 
			FILL_PARSER,
			SELECTALL_PARSER,
			SELECT_PARSER
			);
	
	public static Parser of(String commandName) {
		return parsers.stream().filter(command -> commandName.equals(command.queryName)).findAny().orElseThrow();
	}
	
}

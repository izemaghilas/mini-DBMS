package codes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import core.Constants;
import core.files_management_layer.Record;
import core.files_management_layer.RelationDefinition;
import core.util.ColumnsTypes;

public class Parser {
	private String commandName;
	private BiConsumer<String, DataBaseManagement> parse;

	private Parser(String name, BiConsumer<String, DataBaseManagement> parse) {
		this.commandName = name;
		this.parse = parse;
	}
	
	public void parse(String command, DataBaseManagement dataBaseManager) {
		parse.accept(command, dataBaseManager);
	}
	
	private static String[] extractCommandArguments(String command) {
		String[] args = command.split(" ");
		List<String> array = new ArrayList<>();
		for (int i = 1; i < args.length; i++)
			array.add(args[i]);
		return array.toArray(new String[args.length - 1]);
	}
	
	private static RelationDefinition createRelation(String[] arguments, DataBaseManagement dataBaseManager) {
		List<String> columnsTypes = new ArrayList<>();
		int recordSize=0;
		for (int i = 2; i < arguments.length; i++) {
			ColumnsTypes ct = ColumnsTypes.of(arguments[i]);
			recordSize+=ct.getTypeSize(arguments[i]);
			columnsTypes.add(arguments[i]);
		}
		
		RelationDefinition.Builder builder = new RelationDefinition.Builder();
		RelationDefinition relation = 
				builder.withRelationName(arguments[0])
				.withRelationNumberOfColumns(arguments[1])
				.withRelationColumnsTypes(columnsTypes)
				.withRelationRecordSize(recordSize)
				.withRelationSlotCount(Constants.PAGE_SIZE/recordSize)
				.withRelationFileIndex(dataBaseManager.getDataBaseDefinition().getNumberOfRelations())
				.build();
		return relation;
	}
	
	private static Record createRecord(String[] arguments, int offset) {
		Record record = new Record();
		List<String> values = new ArrayList<>();
		for (int i = offset; i < arguments.length; i++) {
			values.add(arguments[i]);
		}
		record.setValues(values);
		
		return record;
	}
	
	
	
	private final static Parser CREATE_PARSER = new Parser(
			"create", 
			(command, dataBaseManager) -> {
				
				RelationDefinition relation = createRelation(extractCommandArguments(command), dataBaseManager);
				
				dataBaseManager.getDataBaseDefinition().addRelation(relation);
				
				try {
					
					dataBaseManager.getFileManager().createNewHeapFile(relation);
				
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	
	private final static Parser INSERT_PARSER = new Parser(
			"insert", 
			(command, dataBaseManager)->{
				try {
					
					dataBaseManager.getFileManager().insertRecordInRelation(
								command.split(" ")[1], 
								createRecord(extractCommandArguments(command), 2)
							);
					
				} catch(FileNotFoundException e) {
				
					e.printStackTrace();
				
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	
	private final static Parser CLEAN_PARSER = new Parser(
			"clean", 
			(command, dataBaseManager)->{
				try {
				
					dataBaseManager.clean();
				
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	
	private final static Parser FILL_PARSER = new Parser(
			"fill",
			(command, dataBaseManager)->{
				
				String[] commandArguments = extractCommandArguments(command);
				String relationName = commandArguments[1];
				String csvFile = commandArguments[2];
				
				try(BufferedReader csvFileReader = new BufferedReader(new FileReader(csvFile))){
					
					String recordValues="";
					while( (recordValues=csvFileReader.readLine() ) != null ) {
						dataBaseManager.getFileManager()
							.insertRecordInRelation(
									relationName, 
									createRecord(recordValues.split(","), 0)
								);
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
			(command, dataBaseManager)->{
				try {
					dataBaseManager.getFileManager().getAllRecords(extractCommandArguments(command)[0]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	
	private final static Parser SELECT_PARSER = new Parser(
			"select",
			(command, dataBaseManager)->{
				String[] commandArguments = extractCommandArguments(command);
				try {
					dataBaseManager.getFileManager().getAllRecordsWithFilter(
							commandArguments[0], 
							Integer.parseInt(commandArguments[1]), 
							commandArguments[2]);
				} catch (NumberFormatException | IOException e) {
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
		return parsers.stream().filter(command -> commandName.equals(command.commandName)).findAny().orElseThrow();
	}
	
}

package dbms;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


public class Main {
	
	public static void main(String[] args) 
			throws JsonParseException, IOException, ClassNotFoundException, DataBaseManagerNotInitializedException {
		
		JsonFactory jsonFactory = new JsonFactory();
		File configurationFile = new File(Main.class.getResource("/miniDBMS-config.json").getFile());
		JsonParser jsonParser = jsonFactory.createParser(configurationFile);
		
		DataBaseManagement dataBaseManager = setDataBaseManagement(jsonParser);
		
		if(dataBaseManager == null)
			throw new DataBaseManagerNotInitializedException();
		
		injectDependeciesToDataBaseManagement(jsonParser, dataBaseManager);
		
		dataBaseManager.init();
		MiniDBMS.INSTANCE.open(dataBaseManager);
		
	}

	private static void injectDependeciesToDataBaseManagement(JsonParser jsonParser, DataBaseManagement dataBaseManager)
			throws IOException, JsonParseException, ClassNotFoundException {
		
		while(jsonParser.nextToken() != null) {
			String name = jsonParser.getCurrentName();
			if("DataBaseDefinition".equals(name)) {
				jsonParser.nextToken();
				dataBaseManager.setDataBaseDefintion( (DataBaseDefinition) getObject(jsonParser.getText()) );
				continue;
			}
			
			if("BufferManager".equals(name)) {
				jsonParser.nextToken();
				dataBaseManager.setBufferManagement(
						(BufferManagement) getObject(jsonParser.getText())
					);
				continue;
			}
			
			if("BufferPool".equals(name)) {
				jsonParser.nextToken();
				dataBaseManager
					.getBufferManager()
					.setBufferPool( getObject(jsonParser.getText()) );
				continue;
			}
			
			if("ReplacementPolicy".equals(name)) {
				jsonParser.nextToken();
				dataBaseManager
					.getBufferManager()
					.setReplacementPolicy( getObject(jsonParser.getText()) );
				jsonParser.nextToken();
			}
			
			if("DiskManager".equals(name)) {
				jsonParser.nextToken();
				dataBaseManager.setDiskSpaceManagement(
							(DiskSpaceManagement) getObject(jsonParser.getText())
						);
				continue;
			}
			
			if("FileManager".equals(name)) {
				jsonParser.nextToken();
				dataBaseManager.setFileManagement(
							(FileManagement) getObject(jsonParser.getText())
						);
				continue;
			}
			
		}
	}

	private static DataBaseManagement setDataBaseManagement(JsonParser jsonParser)
			throws IOException, JsonParseException, ClassNotFoundException {
		
		while(jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String name = jsonParser.getCurrentName();
			if( "DataBaseManager".equals(name) ) {
				jsonParser.nextToken();
				return (DataBaseManagement) getObject(jsonParser.getText());
			}
		}
		return null;
	}
	
	private static Object getObject(String className) throws ClassNotFoundException {
		Class<?> objectClass = Class.forName(className);
		Object[] enumConstants = objectClass.getEnumConstants();
		return enumConstants[0];
	}

}

package codes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import core.files_management_layer.Record;
import core.files_management_layer.RelationDefinition;

public interface FileManagement {
	
	public void init(DataBaseManagement dataBaseManager);
	
	public void reset();
	
	public void createNewHeapFile(RelationDefinition relation) throws IOException;
	
	public void insertRecordInRelation(String relationName, Record record) 
			throws FileNotFoundException, IOException;
	
	public List<Record> getAllRecords(String relationName) 
			throws FileNotFoundException, IOException;
	
	public List<Record> getAllRecordsWithFilter(String relationName, int columnIndex, String value) 
			throws FileNotFoundException, IOException;
	
	
}

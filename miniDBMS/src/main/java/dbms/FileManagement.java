package dbms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dbms.core.database_management_layer.RelationDefinition;
import dbms.core.files_management_layer.Record;

public interface FileManagement {
	
	public void init(DataBaseDefinition dataBaseDefinition);
	
	public void reset();
	
	public void createNewHeapFile(RelationDefinition relation, DataBaseManagement dataBaseManager) throws IOException;
	
	public void insertRecordInRelation(String relationName, Record record, DataBaseManagement dataBaseManager) 
			throws FileNotFoundException, IOException;
	
	public List<Record> getAllRecords(String relationName, DataBaseManagement dataBaseManager) 
			throws FileNotFoundException, IOException;
	
	public List<Record> getAllRecordsWithFilter(List<Record> records, int columnIndex, String value) 
			throws FileNotFoundException, IOException;
	
	
}

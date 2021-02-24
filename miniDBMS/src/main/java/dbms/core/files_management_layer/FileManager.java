package dbms.core.files_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dbms.DataBaseDefinition;
import dbms.DataBaseManagement;
import dbms.FileManagement;
import dbms.core.database_management_layer.RelationDefinition;

public enum FileManager implements FileManagement {
	INSTANCE;
	
	private List<HeapFile> heapFiles;

	@Override
	public void init(DataBaseDefinition dataBaseDefinition) {
		
		heapFiles=new ArrayList<>();
		
		dataBaseDefinition.getRelations().forEach(
					relation->{
						HeapFile heapFile = new HeapFile();
						heapFile.setRelation(relation);
						heapFiles.add(heapFile);
					}
				);
	}
	
	@Override
	public void createNewHeapFile(RelationDefinition relation, DataBaseManagement dataBaseManager) 
			throws IOException {
		
		HeapFile heapFile = new HeapFile();
		heapFile.setRelation(relation);
		heapFiles.add(heapFile);
		
		heapFile.createNewOnDisk(dataBaseManager);
	}

	@Override
	public void reset() {
		heapFiles.clear();
	}

	@Override
	public void insertRecordInRelation(String relationName, Record record, DataBaseManagement dataBaseManager) 
			throws FileNotFoundException, IOException {
		getHeapFile(relationName).insertRecord(record, dataBaseManager);
	}

	@Override
	public List<Record> getAllRecords(String relationName, DataBaseManagement dataBaseManager) 
			throws FileNotFoundException, IOException {
		HeapFile heapFile = getHeapFile(relationName);
		
		return heapFile.getAllRecords(dataBaseManager); 
	}

	@Override
	public List<Record> getAllRecordsWithFilter(List<Record> records, int columnIndex, String value) 
			throws FileNotFoundException, IOException {
				
		List<Record> filteredRecords = new ArrayList<>();
		records.forEach(
				record -> {
					if(record.getValues().get(columnIndex).equals(value))
						filteredRecords.add(record);
				}
			);
		
		return filteredRecords;
				
	}
	
	private HeapFile getHeapFile(String relationName) {
		return heapFiles.stream().filter(heapfile-> heapfile.getRelation().getName().equals(relationName))
				.findAny()
				.orElseThrow();
	}
	
}

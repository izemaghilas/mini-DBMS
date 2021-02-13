package core.files_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import codes.DataBaseManagement;
import codes.FileManagement;

public enum FileManager implements FileManagement {
	INSTANCE;
	
	private List<HeapFile> heapFiles;
	private DataBaseManagement dataBaseManager;

	@Override
	public void init(DataBaseManagement dataBaseManager) {
		heapFiles=new ArrayList<>();
		dataBaseManager.getDataBaseDefinition().getRelations().forEach(
					relation->{
						HeapFile heapFile = new HeapFile();
						heapFile.setRelation(relation);
						heapFiles.add(heapFile);
					}
				);
		this.dataBaseManager = dataBaseManager;
	}
	
	@Override
	public void createNewHeapFile(RelationDefinition relation) throws IOException {
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
	public void insertRecordInRelation(String relationName, Record record) 
			throws FileNotFoundException, IOException {
		getHeapFile(relationName).insertRecord(record, dataBaseManager);
	}

	@Override
	public List<Record> getAllRecords(String relationName) 
			throws FileNotFoundException, IOException {
		HeapFile heapFile = getHeapFile(relationName);
		
		return heapFile.getAllRecords(dataBaseManager); 
	}

	@Override
	public List<Record> getAllRecordsWithFilter(String relationName, int columnIndex, String value) 
			throws FileNotFoundException, IOException {
		
		List<Record> filteredRecords = new ArrayList<>();
		getAllRecords(relationName).forEach(
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

package core.files_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import codes.DataBaseManagement;
import core.buffer_management_layer.PageIdentifier;
import core.files_management_layer.util.RecordReader;
import core.files_management_layer.util.RecordWriter;

//TODO Dependency injection (Record Writer Reader)

public class HeapFile {
	private RelationDefinition relation;

	public HeapFile() {
	}
	
	public RelationDefinition getRelation() {
		return relation;
	}

	public void setRelation(RelationDefinition relation) {
		this.relation = relation;
	}
	
	public void createNewOnDisk(DataBaseManagement dataBaseManager ) throws IOException {
		int fileIndex = relation.getFileIndex();
		dataBaseManager.getDiskManager().createFile(fileIndex);
		
		PageIdentifier headerPageId = new PageIdentifier();
		headerPageId.setFileIndex(fileIndex);
		headerPageId.setPageIndex(0);
		
		dataBaseManager.getDiskManager().addPage(fileIndex, headerPageId);
		
		HeaderPageInformation headerPageInfo = new HeaderPageInformation();
		
		byte[] headerPageBuffer = dataBaseManager.getBufferManager().getPage(headerPageId, dataBaseManager.getDiskManager());
		
		headerPageInfo.writeToBuffer(headerPageBuffer);
		
		dataBaseManager.getBufferManager().freePage(headerPageId, true);
	}
	

	public RecordIdentifier insertRecord(Record record, DataBaseManagement dataBaseManager) 
			throws FileNotFoundException, IOException {
		
		return RecordWriter.INSTANCE.insertRecord(record, relation, dataBaseManager);
	}
	
	public List<Record> getAllRecords(DataBaseManagement dataBaseManager) 
			throws FileNotFoundException, IOException {
		
		return RecordReader.INSTANCE.getAllRecords(relation, dataBaseManager);
	}
	
	public List<Record> getAllRecordsWithFilter(List<Record> records, int columnIndex, String value){
		List<Record> filteredRecords = new ArrayList<>();
		
		records.forEach(record->{
			if( record.getValues().get(columnIndex).equals(value) )
				filteredRecords.add(record);
		});
		
		return filteredRecords;
	}
}

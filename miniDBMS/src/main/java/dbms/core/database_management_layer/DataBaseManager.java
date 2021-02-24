package dbms.core.database_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dbms.BufferManagement;
import dbms.DataBaseDefinition;
import dbms.DataBaseManagement;
import dbms.DiskSpaceManagement;
import dbms.FileManagement;
import dbms.core.files_management_layer.Record;
import dbms.core.util.ColumnTypeManager;

public enum DataBaseManager implements DataBaseManagement {
	INSTANCE;
	
	private DataBaseDefinition dataBaseDefinition;
	private BufferManagement bufferManager;
	private DiskSpaceManagement diskManager;
	private FileManagement fileManager;
	
	@Override
	public void init() throws FileNotFoundException, ClassNotFoundException, IOException {
		dataBaseDefinition.init();
		fileManager.init(dataBaseDefinition);
		bufferManager.init();
	}

	@Override
	public void clean() throws IOException {
		fileManager.reset();
		dataBaseDefinition.reset();
		bufferManager.reset();
		diskManager.reset();
	}
	
	@Override
	public void createRelation(String relationName, int numberOfColumns, List<String> columns) throws IOException {
		int recordSize = calculateRecordSize(columns);
		
		RelationDefinition.Builder builder = new RelationDefinition.Builder();
		RelationDefinition relation = 
				builder.withRelationName(relationName)
				.withRelationNumberOfColumns(numberOfColumns)
				.withRelationColumnsTypes(columns)
				.withRelationRecordSize(recordSize)
				.withRelationSlotCount(recordSize)
				.withRelationFileIndex(dataBaseDefinition.getNumberOfRelations())
				.build();
		dataBaseDefinition.addRelation(relation);
		fileManager.createNewHeapFile(relation, this);
	}
	
	private int calculateRecordSize(List<String> types) {
		int recordSize=0;
		for(String type : types) {
			ColumnTypeManager columnTypeManager = ColumnTypeManager.of(type);
			recordSize += columnTypeManager.getSize();
		}
		
		return recordSize;
	}
	
	
	@Override
	public void insertRecord(String relationName, List<String> recordValues) 
			throws FileNotFoundException, IOException {
		
		Record record = new Record();
		record.setValues(recordValues);
		fileManager.insertRecordInRelation(relationName, record, this);
	}
	
	private String recordsToString(List<Record> records) {
		String str="";
		for(Record record: records)
			str+=record.toString()+"\n";
		str+="Total: "+records.size();
		
		return str;
	}
	
	@Override
	public String getRecords(String relationName) throws FileNotFoundException, IOException {
	
		return recordsToString(fileManager.getAllRecords(relationName, this));
		
	}
	
	@Override
	public String getFilteredRecords(String relationName, int columnIndex, String value) 
			throws FileNotFoundException, IOException {
		
		List<Record> records = fileManager.getAllRecords(relationName, this);
		
		return recordsToString(
					fileManager.getAllRecordsWithFilter(
						records, 
						columnIndex, 
						value
					)
				);
	}
	
	@Override
	public DataBaseDefinition getDataBaseDefinition() {
		return dataBaseDefinition;
	}

	@Override
	public FileManagement getFileManager() {
		return fileManager;
	}

	@Override
	public BufferManagement getBufferManager() {
		return bufferManager;
	}

	@Override
	public DiskSpaceManagement getDiskManager() {
		return diskManager;
	}

	@Override
	public void finish() throws FileNotFoundException, ClassNotFoundException, IOException {
		dataBaseDefinition.finish();
	}

	@Override
	public void setDataBaseDefintion(DataBaseDefinition dataBaseDefintion) {
		this.dataBaseDefinition = (DataBase) dataBaseDefintion;
	}

	@Override
	public void setFileManagement(FileManagement fileManagement) {
		this.fileManager = fileManagement;
	}

	@Override
	public void setBufferManagement(BufferManagement bufferManagement) {
		this.bufferManager = bufferManagement;
	}

	@Override
	public void setDiskSpaceManagement(DiskSpaceManagement diskSpaceManagement) {
		this.diskManager = diskSpaceManagement;
	}

}

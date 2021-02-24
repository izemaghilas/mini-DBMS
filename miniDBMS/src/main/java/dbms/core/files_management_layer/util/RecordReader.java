package dbms.core.files_management_layer.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dbms.DataBaseManagement;
import dbms.core.buffer_management_layer.PageIdentifier;
import dbms.core.database_management_layer.RelationDefinition;
import dbms.core.files_management_layer.HeaderPageInformation;
import dbms.core.files_management_layer.Record;
import dbms.core.util.ColumnTypeManager;

public enum RecordReader {
	INSTANCE;
	
	public List<Record> getAllRecords(
			RelationDefinition relation, 
			DataBaseManagement dataBaseManager
		) throws FileNotFoundException, IOException {
		
		List<Record> records = new ArrayList<>();
		for(PageIdentifier dataPageId : getDataPagesIds(relation, dataBaseManager))
			records.addAll(getRecordsInDataPage(dataPageId, relation, dataBaseManager));
		
		return records;
	}
	
	private void initHeaderPageIdentifire(
			PageIdentifier headerPageId, 
			RelationDefinition relation
		) {
		
		headerPageId.setPageIndex(0);
		headerPageId.setFileIndex(relation.getFileIndex());
	}
	
	private void initHeaderPageInfo(
			HeaderPageInformation headerPageInfo,
			PageIdentifier headerPageId,
			DataBaseManagement dataBaseManager
		) throws FileNotFoundException, IOException {
		
		byte[] buffer = dataBaseManager.getBufferManager().getPage(headerPageId, dataBaseManager.getDiskManager());
		headerPageInfo.readFromBuffer(buffer);
	}
	
	private List<Record> getRecordsInDataPage(
			PageIdentifier dataPageId,
			RelationDefinition relation,
			DataBaseManagement dataBseManager
		) throws FileNotFoundException, IOException {
		
		byte[] buffer = dataBseManager.getBufferManager().getPage(dataPageId, dataBseManager.getDiskManager());
		List<Record> records = new ArrayList<>();
		for(int i=0; i<relation.getSlotCount(); i++) {
			if(buffer[i] == (byte)1)
				records.add(readRecordFromBuffer(relation, buffer, i));
		}
		
		dataBseManager.getBufferManager().freePage(dataPageId, false);
			
		return records;
	}

	Record readRecordFromBuffer(RelationDefinition relation, byte[] buffer, int slotIndex) {
		ColumnTypeManager.setOffset(
				relation.getSlotCount() + ( slotIndex*relation.getRecordSize() )
				);
		List<String> recordValues = new ArrayList<>();
		
		relation.getColumnsTypes().forEach(
					columnType -> {
						ColumnTypeManager columnTypeManager = ColumnTypeManager.of(columnType);
						recordValues.add( columnTypeManager.readRecordValueFromBuffer(buffer) );
					}
				);
		
		Record record = new Record();
		record.setValues(recordValues);
		return record;
	}

	private List<PageIdentifier> getDataPagesIds(
			RelationDefinition relation, 
			DataBaseManagement dataBaseManager
		) throws FileNotFoundException, IOException {
		
		PageIdentifier headerPageId = new PageIdentifier();
		initHeaderPageIdentifire(headerPageId, relation);
		
		HeaderPageInformation headerPageInfo = new HeaderPageInformation();
		initHeaderPageInfo(headerPageInfo, headerPageId, dataBaseManager);
		
		List<PageIdentifier> dataPagesIds = new ArrayList<>();
		headerPageInfo.getDataPages().forEach(
				dataPage->{
					if(dataPage.getFreeSlots()>=0) {
						PageIdentifier pageId = new PageIdentifier();
						pageId.setFileIndex(relation.getFileIndex());
						pageId.setPageIndex(dataPage.getDataPageIndex());
						dataPagesIds.add(pageId);
					}
				}
			);
		
		dataBaseManager.getBufferManager().freePage(headerPageId, false);
		
		return dataPagesIds;
	}
}

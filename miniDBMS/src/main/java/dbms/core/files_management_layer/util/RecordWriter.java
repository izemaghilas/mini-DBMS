package dbms.core.files_management_layer.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import dbms.DataBaseManagement;
import dbms.core.Constants;
import dbms.core.buffer_management_layer.PageIdentifier;
import dbms.core.database_management_layer.RelationDefinition;
import dbms.core.files_management_layer.HeaderPageInformation;
import dbms.core.files_management_layer.Record;
import dbms.core.files_management_layer.RecordIdentifier;
import dbms.core.util.ColumnTypeManager;

public enum RecordWriter {
	INSTANCE;

	public RecordIdentifier insertRecord(
			Record record, 
			RelationDefinition relation, 
			DataBaseManagement dataBaseManager
		) throws FileNotFoundException, IOException {
		
		PageIdentifier pageId = new PageIdentifier();
		getFreePageId(pageId, relation, dataBaseManager);
		
		byte[] pageBuffer = getPageBuffer(pageId, dataBaseManager);
		int slotIndex = getSlotIndex(relation, pageBuffer);
		int offset = relation.getSlotCount() + ( slotIndex * relation.getRecordSize() );
		
		ColumnTypeManager.setOffset(offset);
		writeRecordInBuffer(relation, record, pageBuffer);
		updateHeaderPageWithTakenSlot(pageId, relation, dataBaseManager);
		
		RecordIdentifier recordIdentifier = new RecordIdentifier();
		pageBuffer[slotIndex] = (byte)1;
		recordIdentifier.setPageId(pageId);
		recordIdentifier.setSlotIndex(slotIndex);
		
		dataBaseManager.getBufferManager().freePage(pageId, true);
		
		return recordIdentifier;
	}
	
	private byte[] getPageBuffer(
			PageIdentifier pageId, 
			DataBaseManagement dataBaseManager
		) throws FileNotFoundException, IOException {
		
		return dataBaseManager.getBufferManager().getPage(pageId, dataBaseManager.getDiskManager());
	}
	
	private void getFreePageId(
			PageIdentifier pageId, 
			RelationDefinition relation, 
			DataBaseManagement dataBaseManager
		) throws FileNotFoundException, IOException {		
		
		PageIdentifier headerPageId = new PageIdentifier();
		HeaderPageInformation headerPageInfo = new HeaderPageInformation();
		
		byte[] headerPageBuffer = initHeaderPage(relation, dataBaseManager, headerPageId, headerPageInfo);
		
		try {
			
			DataPage dataPage = findDataPageWithFreeSlots(headerPageInfo);
			
			pageId.setPageIndex(dataPage.getDataPageIndex());
			pageId.setFileIndex(relation.getFileIndex());
			
			dataBaseManager.getBufferManager().freePage(headerPageId, false);
			
		} catch(NoSuchElementException e) {
			
			dataBaseManager.getDiskManager().addPage(relation.getFileIndex(), pageId);
			
			DataPage dataPage = new DataPage();
			dataPage.setDataPageIndex((int) pageId.getPageIndex());
			//dataPage : slotCount bytes are "bytemap"
			dataPage.setFreeSlots( (Constants.PAGE_SIZE-relation.getSlotCount())/relation.getRecordSize() );
			
			headerPageInfo.getDataPages().add(dataPage);
			headerPageInfo.setDataPageCount(headerPageInfo.getDataPageCount()+1);
			headerPageInfo.writeToBuffer(headerPageBuffer);
			
			dataBaseManager.getBufferManager().freePage(headerPageId, true);
			
		}
		
	}
	
	private DataPage findDataPageWithFreeSlots(HeaderPageInformation headerPageInfo) {
		return headerPageInfo.getDataPages().stream()
				.filter(dataPage->dataPage.getFreeSlots()>0)
				.findAny()
				.orElseThrow();
	}
	
	void updateHeaderPageWithTakenSlot(
			PageIdentifier pageId,
			RelationDefinition relation, 
			DataBaseManagement dataBaseManager
		) throws FileNotFoundException, IOException {
		
		PageIdentifier headerPageId = new PageIdentifier();
		HeaderPageInformation headerPageInfo = new HeaderPageInformation();
		byte[] headerPageBuffer = initHeaderPage(relation, dataBaseManager, headerPageId, headerPageInfo);
		
		DataPage dataPage =  headerPageInfo.getDataPages().stream()
			.filter( datapage -> datapage.getDataPageIndex() == pageId.getPageIndex())
			.findFirst()
			.get();
		
		dataPage.setFreeSlots(dataPage.getFreeSlots()-1);
		headerPageInfo.writeToBuffer(headerPageBuffer);
		
		dataBaseManager.getBufferManager().freePage(headerPageId, true);
	}


	void writeRecordInBuffer(RelationDefinition relation, Record record, byte[] pageBuffer) {		
		
		List<String> recordValues = record.getValues();
		List<String> columnsTypes = relation.getColumnsTypes();
		int i=0;
		while(i < columnsTypes.size() ) {
			ColumnTypeManager columnTypeManager = ColumnTypeManager.of(columnsTypes.get(i));
			columnTypeManager.writeRecordValueToBuffer(recordValues.get(i), pageBuffer);
			i+=1;
		}
	}
	
	private int getSlotIndex(RelationDefinition relation, byte[] pageBuffer) {
		//as the slotCount bytes represent the "BYTEMAP"
		for(int i=0; i<relation.getSlotCount(); i++) {
			if(pageBuffer[i] == (byte)0) {
				return i;
			}
		}
		return -1;
	}


	private byte[] initHeaderPage(
			RelationDefinition relation, 
			DataBaseManagement dataBaseManager,
			PageIdentifier headerPageId, 
			HeaderPageInformation headerPageInfo
		) throws FileNotFoundException, IOException {
		
		headerPageId.setPageIndex(0);
		headerPageId.setFileIndex(relation.getFileIndex());
		
		byte[] headerPageBuffer = dataBaseManager.getBufferManager().getPage(headerPageId, dataBaseManager.getDiskManager());
		
		headerPageInfo.readFromBuffer(headerPageBuffer);
		return headerPageBuffer;
	}
	
	
}

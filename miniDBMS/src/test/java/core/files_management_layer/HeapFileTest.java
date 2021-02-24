package core.files_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dbms.core.Constants;
import dbms.core.buffer_management_layer.BufferManager;
import dbms.core.buffer_management_layer.PageIdentifier;
import dbms.core.buffer_management_layer.util.BufferPool;
import dbms.core.database_management_layer.DataBaseManager;
import dbms.core.database_management_layer.RelationDefinition;
import dbms.core.files_management_layer.HeaderPageInformation;
import dbms.core.files_management_layer.HeapFile;
import dbms.core.files_management_layer.Record;
import dbms.core.files_management_layer.RecordIdentifier;
import setup.Setup;


class HeapFileTest {
	private HeapFile heapfile;
	private RelationDefinition relation; 

	private void init() {
		this.heapfile = new HeapFile();
		this.relation = new RelationDefinition();
		
		relation.setFileIndex(0);
		relation.setName("agent");
		relation.setColumnsTypes(List.of("int", "float", "string20"));
		relation.setNumberOfColumns(3);
		relation.setRecordSize(48);
		relation.setSlotCount(Constants.PAGE_SIZE/relation.getRecordSize());
				
		this.heapfile.setRelation(relation);
		
		BufferPool.INSTANCE.init();
		//TODO : set buffer manager pool and replacement policy
		BufferManager.INSTANCE.init();
	}
	

	@Test
	void testCreateNewOnDisk() throws IOException {
		//Setup
		init();
		
		//Given
		HeaderPageInformation headerPageInfo = new HeaderPageInformation();
		PageIdentifier PageId = new PageIdentifier();
		PageId.setFileIndex(0);
		PageId.setPageIndex(0);
		
		//When
		heapfile.createNewOnDisk(DataBaseManager.INSTANCE);
		byte[] PageBuffer = DataBaseManager.INSTANCE.getBufferManager().getPage(PageId, DataBaseManager.INSTANCE.getDiskManager());
		headerPageInfo.readFromBuffer(PageBuffer);
		
		boolean DB_created = Files.exists(Paths.get(Constants.DB_DIRECTORY+"/DATA_"+PageId.getFileIndex()+".rf"));
		
		Setup.deleteDB();
		
		//Then
		Assertions.assertTrue(DB_created);
		Assertions.assertEquals(0, headerPageInfo.getDataPageCount());
		Assertions.assertEquals(0, headerPageInfo.getDataPages().size());
	}

	@Test
	void testInsertRecord() throws FileNotFoundException, IOException {
		//Setup
		init();
		Setup.initDB(DataBaseManager.INSTANCE, relation);
		
		//Given
		Record record = new Record();
		record.setValues(Setup.getFirstLineFromDataSet());
		
		RecordIdentifier recordId = new RecordIdentifier();
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(0);
		pageId.setPageIndex(1);
		recordId.setPageId(pageId);
		recordId.setSlotIndex(0);
		
		//When
		RecordIdentifier rid =  heapfile.insertRecord(record, DataBaseManager.INSTANCE);
		
		Setup.deleteDB();
		
		//Then
		Assertions.assertEquals(recordId.getSlotIndex(), rid.getSlotIndex());
		Assertions.assertEquals(recordId.getPageId(), rid.getPageId());
		
	}

	@Test
	void testGetAllRecords() throws IOException {
		//Setup
		init();
		Setup.initDB(DataBaseManager.INSTANCE, relation);
		
		//When		
		List<Record> records = heapfile.getAllRecords(DataBaseManager.INSTANCE);
		Setup.resetBufferManager(DataBaseManager.INSTANCE);
		
		Setup.fillDB(DataBaseManager.INSTANCE, relation);
		
		List<Record> records1 = heapfile.getAllRecords(DataBaseManager.INSTANCE);
		
		Setup.deleteDB();
		
		//Then
		Assertions.assertEquals(0, records.size());	
		Assertions.assertEquals(70, records1.size());//70 size of dummyData "data_set_get_all_records.csv"
		
		
	}

	@Test
	void testGetAllRecordsWithFilter() {
	}

}

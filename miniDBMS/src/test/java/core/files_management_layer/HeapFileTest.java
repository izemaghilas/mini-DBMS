package core.files_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import codes.DataBaseManager;
import codes.DiskSpaceManagement;
import core.Constants;
import core.buffer_management_layer.BufferManager;
import core.buffer_management_layer.PageIdentifier;
import core.buffer_management_layer.util.BufferPool;
import core.buffer_management_layer.util.LRU;
import setup.ForHeapFileTest;

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
		
		BufferManager.INSTANCE.init(BufferPool.INSTANCE, LRU.INSTANCE);
	}
	

	@Test
	void testCreateNewOnDisk() throws IOException {
		//Given
		init();
		HeaderPageInformation headerPageInfo = new HeaderPageInformation();
		
		PageIdentifier PageId = new PageIdentifier();
		PageId.setFileIndex(0);
		PageId.setPageIndex(0);
		
		//When
		heapfile.createNewOnDisk(DataBaseManager.INSTANCE);
		byte[] PageBuffer = DataBaseManager.INSTANCE.getBufferManager().getPage(PageId, DataBaseManager.INSTANCE.getDiskManager());
		headerPageInfo.readFromBuffer(PageBuffer);
		
		//Then
		Assertions.assertTrue(Files.exists(Paths.get(Constants.DB_DIRECTORY+"/DATA_"+PageId.getFileIndex()+".rf")));
		Assertions.assertEquals(0, headerPageInfo.getDataPageCount());
		Assertions.assertEquals(0, headerPageInfo.getDataPages().size());
		
		ForHeapFileTest.deleteDB();
	}

	@Test
	void testInsertRecord() throws FileNotFoundException, IOException {
		//Given
		init();
		DiskSpaceManagement diskManager = DataBaseManager.INSTANCE.getDiskManager();
		diskManager.createFile(0);
		diskManager.addPage(0, new PageIdentifier());
		
		Record record = new Record();
		record.setValues(List.of("24", "1.85", "agent_Maitrise"));
		RecordIdentifier recordId = new RecordIdentifier();
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(0);
		pageId.setPageIndex(1);
		recordId.setPageId(pageId);
		recordId.setSlotIndex(0);
		
		//When
		RecordIdentifier rid =  heapfile.insertRecord(record, DataBaseManager.INSTANCE);
		
		//Then
		Assertions.assertEquals(recordId.getSlotIndex(), rid.getSlotIndex());
		Assertions.assertEquals(recordId.getPageId(), rid.getPageId());
		
		ForHeapFileTest.deleteDB();
	}

	@Test
	void testGetAllRecords() throws IOException {
		//Given
		init();
		
		DiskSpaceManagement diskManager = DataBaseManager.INSTANCE.getDiskManager();
		diskManager.createFile(0);
		
		//When
		List<Record> records = heapfile.getAllRecords(DataBaseManager.INSTANCE);
		
		ForHeapFileTest.initDB();
		DataBaseManager.INSTANCE.getBufferManager().reset();
		List<Record> records1 = heapfile.getAllRecords(DataBaseManager.INSTANCE);
		
		//Then
		Assertions.assertEquals(0, records.size());
		Assertions.assertEquals(70, records1.size());//70 size of dummyData "data_set_get_all_records.csv"
		
		ForHeapFileTest.deleteDB();
	}

	@Test
	void testGetAllRecordsWithFilter() {
	}

}

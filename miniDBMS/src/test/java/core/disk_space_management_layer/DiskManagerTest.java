package core.disk_space_management_layer;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dbms.core.Constants;
import dbms.core.buffer_management_layer.PageIdentifier;
import dbms.core.database_management_layer.DataBaseManager;

class DiskManagerTest {
	
	@AfterEach
	private void finish() throws IOException{
		for(File file: Path.of(Constants.DB_DIRECTORY).toFile().listFiles()) {
			file.delete();
		}
		Files.deleteIfExists(Path.of(Constants.DB_DIRECTORY));
		
	}

	
	@Test
	void testCreateFile() throws IOException {
		//Given 
		long fileIndex=0;
		long fileIndex1=1;
		
		//When
		DataBaseManager.INSTANCE.getDiskManager().createFile(fileIndex);
		DataBaseManager.INSTANCE.getDiskManager().createFile(fileIndex1);
		
		//Then
		Assertions.assertTrue(Files.exists(Paths.get(Constants.DB_DIRECTORY)));
		Assertions.assertTrue(Files.exists(Paths.get(Constants.DB_DIRECTORY+"/DATA_"+fileIndex+".rf")));
		Assertions.assertTrue(Files.exists(Paths.get(Constants.DB_DIRECTORY+"/DATA_"+fileIndex1+".rf")));
		
		finish();
	}
	

	
	@Test
	void testAddPage() throws IOException {
		//Given
		PageIdentifier pageId = new PageIdentifier();
		long fileIndex = 0;
		
		if(Files.notExists(Paths.get(Constants.DB_DIRECTORY))) {
			Files.createDirectory(Paths.get(Constants.DB_DIRECTORY));
		}
		Files.createFile(Paths.get(Constants.DB_DIRECTORY+"/DATA_"+fileIndex+".rf"));
		
		//When
		DataBaseManager.INSTANCE.getDiskManager().addPage(fileIndex, pageId);
		
		//Then
		Assertions.assertEquals(fileIndex, pageId.getFileIndex());
		Assertions.assertEquals(0, pageId.getPageIndex());
		
		finish();
	}
	

	@Test
	void testReadPage() throws IOException{
		//Given
		long fileIndex=0;
		String filePath = Constants.DB_DIRECTORY+"/DATA_"+fileIndex+".rf";
		
		byte[] pageBuffer = new byte[Constants.PAGE_SIZE];
		byte[] buffer = new byte[Constants.PAGE_SIZE];
		
		pageBuffer[1] = 2;
		pageBuffer[2] = 12;
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(fileIndex);
		pageId.setPageIndex(0);
		
		if(Files.notExists(Paths.get(Constants.DB_DIRECTORY))) {
			Files.createDirectory(Paths.get(Constants.DB_DIRECTORY));
		}
		Files.createFile(Paths.get(Constants.DB_DIRECTORY+"/DATA_"+fileIndex+".rf"));
		
		try(RandomAccessFile raf = new RandomAccessFile(Path.of(filePath).toFile(), "rw")){
			raf.seek(pageId.getPageIndex()*Constants.PAGE_SIZE);
			raf.write(pageBuffer);
			
			raf.close();
		}
		
		//When
		DataBaseManager.INSTANCE.getDiskManager().readPage(pageId, buffer);
		
		//Then
		Assertions.assertEquals(2, buffer[1]);
		Assertions.assertEquals(12, buffer[2]);
		
		finish();
	}

	@Test
	void testWritePage() throws IOException {
		//Given
		long fileIndex=0;
		String filePath = Constants.DB_DIRECTORY+"/DATA_"+fileIndex+".rf";
		
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(fileIndex);
		pageId.setPageIndex(0);
		
		byte[] pageBuffer = new byte[Constants.PAGE_SIZE];
		byte[] buffer = new byte[Constants.PAGE_SIZE];
		pageBuffer[1] = 2;
		pageBuffer[2] = 12;
		
		if(Files.notExists(Paths.get(Constants.DB_DIRECTORY))) {
			Files.createDirectory(Paths.get(Constants.DB_DIRECTORY));
		}
		Files.createFile(Paths.get(Constants.DB_DIRECTORY+"/DATA_"+fileIndex+".rf"));

		//When
		DataBaseManager.INSTANCE.getDiskManager().writePage(pageId, pageBuffer);
		
		try(RandomAccessFile raf = new RandomAccessFile(Path.of(filePath).toFile(), "r")){
			raf.seek(0);
			raf.read(buffer);
			raf.close();
		}catch (Exception e) {
			finish();
		}
		
		//Then
		Assertions.assertEquals(2, buffer[1]);
		Assertions.assertEquals(12, buffer[2]);
		
		finish();
	}
	
	
	
	@Test
	void testReset() throws IOException {
		//Given
		Files.createDirectory(Paths.get(Constants.DB_DIRECTORY));
		Files.createFile(Paths.get(Constants.DB_DIRECTORY+"/test1.txt"));
		Files.createFile(Paths.get(Constants.DB_DIRECTORY+"/test2.txt"));
		Files.createFile(Paths.get(Constants.DB_DIRECTORY+"/test3.txt"));
		
		//When
		DataBaseManager.INSTANCE.getDiskManager().reset();
		
		//Then
		Assertions.assertEquals(0, Path.of(Constants.DB_DIRECTORY).toFile().listFiles().length);
		
		finish();
	}
	
	

}

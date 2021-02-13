package core;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.buffer_management_layer.PageIdentifier;
import core.disk_space_management_layer.DiskManager;

public class DiskManagerTest {
	
	@Test
	public void should_throw_FileNotFoundException() {
		//Given
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(0);
		pageId.setPageIndex(0);
		
		byte[] pageBuffer = new byte[Constants.PAGE_SIZE];
		
		//When Then
		Assertions.assertThrows(FileNotFoundException.class, ()->{DiskManager.INSTANCE.addPage(2, pageId);});
		Assertions.assertThrows(FileNotFoundException.class, ()->{DiskManager.INSTANCE.readPage(pageId, pageBuffer);});
		Assertions.assertThrows(IOException.class, ()->{DiskManager.INSTANCE.writePage(pageId, pageBuffer);});
	}
}

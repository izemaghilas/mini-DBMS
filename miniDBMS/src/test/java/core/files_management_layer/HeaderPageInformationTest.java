package core.files_management_layer;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.Constants;
import core.files_management_layer.util.DataPage;

class HeaderPageInformationTest {

	@Test
	void testReadFromBuffer() {
		//Given
		HeaderPageInformation headerPage = new HeaderPageInformation();
		byte[] buffer = new byte[Constants.PAGE_SIZE];
		buffer[3]=2;//number of data pages
		//first page
		buffer[7]=1;
		buffer[11]=25;
		//second page
		buffer[15]=2;
		buffer[19]=2;
		
		//When
		headerPage.readFromBuffer(buffer);
		
		//Then
		//header page number of data pages 
		Assertions.assertEquals(2, headerPage.getDataPageCount());
		
		//first data page
		Assertions.assertEquals(1, headerPage.getDataPages().get(0).getDataPageIndex());
		Assertions.assertEquals(25, headerPage.getDataPages().get(0).getFreeSlots());
		
		//second data page
		Assertions.assertEquals(2, headerPage.getDataPages().get(1).getDataPageIndex());
		Assertions.assertEquals(2, headerPage.getDataPages().get(1).getFreeSlots());
		
	}

	@Test
	void testWriteToBuffer() {
		//Given
		HeaderPageInformation headerPage = new HeaderPageInformation();
		DataPage p1 = new DataPage();
		DataPage p2 = new DataPage();
		
		p1.setDataPageIndex(1);
		p1.setFreeSlots(25);
		
		p2.setDataPageIndex(2);
		p2.setFreeSlots(30);
		
		headerPage.getDataPages().add(p1);
		headerPage.getDataPages().add(p2);
		headerPage.setDataPageCount(2);
		
		byte[] buffer=new byte[Constants.PAGE_SIZE];
		byte[] buffer1=new byte[Constants.PAGE_SIZE];
		//number of data page 
		ByteBuffer.wrap(buffer).putInt(0, 2);
		//first data page
		ByteBuffer.wrap(buffer).putInt(4, 1);
		ByteBuffer.wrap(buffer).putInt(8, 25);
		//second data page
		ByteBuffer.wrap(buffer).putInt(12, 2);
		ByteBuffer.wrap(buffer).putInt(16, 30);
		
		//When
		headerPage.writeToBuffer(buffer1);
		
		//Then
		Assertions.assertArrayEquals(buffer, buffer1);
	}

}

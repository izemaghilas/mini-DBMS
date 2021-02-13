package core.files_management_layer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.files_management_layer.util.DataPage;

public class HeaderPageInformation {
	private int dataPageCount;
	private List<DataPage> dataPages;
	
	public HeaderPageInformation() {
		dataPageCount=0;
		dataPages=new ArrayList<>();
	}

	public int getDataPageCount() {
		return dataPageCount;
	}

	public List<DataPage> getDataPages() {
		return dataPages;
	}

	public void setDataPageCount(int dataPageCount) {
		this.dataPageCount = dataPageCount;
	}

	public void setDataPages(List<DataPage> dataPages) {
		this.dataPages = dataPages;
	}
	
	public int readFromBuffer(byte[] headePageBuffer) {
		int offset=0;
		byte[] dataPageCountBuffer = Arrays.copyOfRange(headePageBuffer, offset, offset+4);
		
		setDataPageCount(ByteBuffer.wrap(dataPageCountBuffer).getInt());
		
		offset+=4;
		for(int i=0; i<dataPageCount; i++) {
			DataPage dataPage = new DataPage();
			int pageIndex = ByteBuffer.wrap(
						Arrays.copyOfRange(headePageBuffer, offset, offset+4)
					).getInt();
			
			offset+=4;
			int freeSlots = ByteBuffer.wrap(
					Arrays.copyOfRange(headePageBuffer, offset, offset+4)
				).getInt();
			
			dataPage.setDataPageIndex(pageIndex);
			dataPage.setFreeSlots(freeSlots);
			dataPages.add(dataPage);
			
			offset+=4;
		}
		
		return dataPageCountBuffer.length;
	}
	
	public void writeToBuffer(byte[] headerPageBuffer) {
		int offset=0;
		ByteBuffer.wrap(headerPageBuffer).putInt(offset, dataPageCount);
		for(DataPage dataPage: dataPages) {
			offset+=4;
			ByteBuffer.wrap(headerPageBuffer).putInt(offset, dataPage.getDataPageIndex());
			offset+=4;
			ByteBuffer.wrap(headerPageBuffer).putInt(offset, dataPage.getFreeSlots());
		}
	}
}
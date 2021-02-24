package dbms;

import java.io.FileNotFoundException;
import java.io.IOException;

import dbms.core.buffer_management_layer.PageIdentifier;

public interface DiskSpaceManagement {
	
	public void reset() throws IOException;
	
	public void createFile(long fileIndex) throws IOException;
	
	public void addPage(long fileIndex, PageIdentifier pageIdentifier ) throws FileNotFoundException, IOException;

	public void readPage(PageIdentifier pageId, byte[] pageBuffer) throws FileNotFoundException, IOException;

	public void writePage(PageIdentifier pageId, byte[] pageBuffer) throws IOException;

}

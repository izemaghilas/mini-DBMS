package codes;

import java.io.FileNotFoundException;
import java.io.IOException;

import core.buffer_management_layer.PageIdentifier;

public interface BufferManagement {
	
	public void init();
	
	public void reset();
	
	public byte[] getPage(PageIdentifier pageId, DiskSpaceManagement diskManager) throws FileNotFoundException, IOException;
	
	public void freePage(PageIdentifier pageId, boolean isDirty);
	
	public void setBufferPool(Object bufferPool);
	
	public void setReplacementPolicy(Object replacementPolicy);
	
}

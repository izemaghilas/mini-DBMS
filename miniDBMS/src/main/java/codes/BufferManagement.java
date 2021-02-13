package codes;

import java.io.FileNotFoundException;
import java.io.IOException;

import core.buffer_management_layer.PageIdentifier;
import core.buffer_management_layer.Pool;
import core.buffer_management_layer.ReplacementPolicy;

public interface BufferManagement {
	
	public void init(Pool bufferPool, ReplacementPolicy replacementPolicy);
	
	public void reset();
	
	public byte[] getPage(PageIdentifier pageId, DiskSpaceManagement diskManager) throws FileNotFoundException, IOException;
	
	public void freePage(PageIdentifier pageId, boolean isDirty);
	
}

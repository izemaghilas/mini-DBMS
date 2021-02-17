package core.buffer_management_layer;

import core.buffer_management_layer.util.DirtyFrameException;
import core.buffer_management_layer.util.Frame;
import core.buffer_management_layer.util.FullBufferPoolException;
import core.buffer_management_layer.util.InUseFrameException;

public interface Pool {
	
	public void init();
	
	public boolean isEmpty();
	
	public void addFrame(PageIdentifier pageId, byte[] pageBuffer) throws FullBufferPoolException;
	
	public void remove(Frame frameToRemove) throws DirtyFrameException;
	
	public void replace(Frame frameToReplace, PageIdentifier pageId, byte[] pageBuffer) 
			throws DirtyFrameException, InUseFrameException;
	
	public byte[] getPage(PageIdentifier pageId);
	
	public void freePage(PageIdentifier pageId, boolean isDirty);
	
	public void reset();
	
	
}

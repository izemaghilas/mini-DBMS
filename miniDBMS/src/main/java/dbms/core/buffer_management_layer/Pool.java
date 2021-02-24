package dbms.core.buffer_management_layer;

import dbms.core.buffer_management_layer.util.DirtyFrameException;
import dbms.core.buffer_management_layer.util.Frame;
import dbms.core.buffer_management_layer.util.FullBufferPoolException;
import dbms.core.buffer_management_layer.util.InUseFrameException;

public interface Pool {
	
	public void init();
	
	public boolean isEmpty();
	
	public void addFrame(Frame frameToAdd) throws FullBufferPoolException;
	
	public void remove(Frame frameToRemove) throws DirtyFrameException;
	
	public void replace(Frame frameToReplace, PageIdentifier pageId, byte[] pageBuffer) 
			throws DirtyFrameException, InUseFrameException;
	
	public byte[] getPage(PageIdentifier pageId);
	
	public Frame freePage(PageIdentifier pageId, boolean isDirty);
	
	public void reset();
	
	
}

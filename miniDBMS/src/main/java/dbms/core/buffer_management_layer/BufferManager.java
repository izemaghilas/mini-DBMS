package dbms.core.buffer_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;

import dbms.BufferManagement;
import dbms.DiskSpaceManagement;
import dbms.core.Constants;
import dbms.core.buffer_management_layer.util.DirtyFrameException;
import dbms.core.buffer_management_layer.util.Frame;
import dbms.core.buffer_management_layer.util.FullBufferPoolException;
import dbms.core.buffer_management_layer.util.InUseFrameException;

public enum BufferManager implements BufferManagement{
	INSTANCE;

	private Pool bufferPool;
	private LRUReplacementPolicy LRUreplacementPolicy;
	
	@Override
	public void init() {		
		bufferPool.init();
		LRUreplacementPolicy.init();
	}
	
	@Override
	public byte[] getPage(PageIdentifier pageId, DiskSpaceManagement diskManager) 
			throws FileNotFoundException, IOException {
		
		byte[] pageBuffer = bufferPool.getPage(pageId);
		
		
		if (pageBuffer == null) {
			pageBuffer = new byte[Constants.PAGE_SIZE];
			diskManager.readPage(pageId, pageBuffer);
			try {
				Frame frame = new Frame(pageId, 1, false, pageBuffer);
				bufferPool.addFrame(frame);
				
			} catch (FullBufferPoolException e) {
				applyReplacementPolicy(pageId, pageBuffer, diskManager);
			}
			return pageBuffer;
		}
		
		return pageBuffer;
	}
	
	private void applyReplacementPolicy(PageIdentifier pageId, byte[] pageBuffer, DiskSpaceManagement diskManager) 
			throws IOException {
		try {
		
			LRUreplacementPolicy.applyTo(bufferPool, pageId, pageBuffer);
		
		} catch (DirtyFrameException e) {
			
			PageIdentifier framePageId = e.getFrameToWriteInDisk().getPageId();
			byte[] framePageBuffer = e.getFrameToWriteInDisk().getPageBuffer();
			diskManager.writePage(framePageId, framePageBuffer);
			
		} catch (InUseFrameException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void freePage(PageIdentifier pageId, boolean isDirty) {
		Frame frame = bufferPool.freePage(pageId, isDirty);
		if(frame.getPinCount() == 0) {
			LRUreplacementPolicy.moveFrame(frame);
		}
	}

	public void flushAll() {

	}

	@Override
	public void reset() {
		bufferPool.reset();
		LRUreplacementPolicy.reset();
	}
	
	@Override
	public void setBufferPool(Object bufferPool) {
		this.bufferPool = (Pool) bufferPool;
	}
	
	@Override
	public void setReplacementPolicy(Object replacementPolicy) {
		this.LRUreplacementPolicy = (LRUReplacementPolicy) replacementPolicy;
	}
}

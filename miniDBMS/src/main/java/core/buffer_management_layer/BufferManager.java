package core.buffer_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;

import codes.BufferManagement;
import codes.DiskSpaceManagement;
import core.Constants;
import core.buffer_management_layer.util.DirtyFrameException;
import core.buffer_management_layer.util.FullBufferPoolException;
import core.buffer_management_layer.util.InUseFrameException;

public enum BufferManager implements BufferManagement{
	INSTANCE;

	private Pool bufferPool;
	private ReplacementPolicy replacementPolicy;
	
	@Override
	public void init() {		
		bufferPool.init();
	}
	
	@Override
	public byte[] getPage(PageIdentifier pageId, DiskSpaceManagement diskManager) throws FileNotFoundException, IOException {
		byte[] pageBuffer = bufferPool.getPage(pageId);
		
		if (pageBuffer == null) {
			pageBuffer = new byte[Constants.PAGE_SIZE];
			diskManager.readPage(pageId, pageBuffer);
			try {
				bufferPool.addFrame(pageId, pageBuffer);
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
			replacementPolicy.applyTo(bufferPool, pageId, pageBuffer);
		} catch (DirtyFrameException e) {
			e.printStackTrace();
			
			PageIdentifier framePageId = e.getFrameToWriteInDisk().getPageId();
			byte[] framePageBuffer = e.getFrameToWriteInDisk().getPageBuffer();
			diskManager.writePage(framePageId, framePageBuffer);
		} catch (InUseFrameException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void freePage(PageIdentifier pageId, boolean isDirty) {
		bufferPool.freePage(pageId, isDirty);
	}

	public void flushAll() {

	}

	@Override
	public void reset() {
		bufferPool.reset();
		replacementPolicy.reset();
	}
	
	@Override
	public void setBufferPool(Object bufferPool) {
		this.bufferPool = (Pool) bufferPool;
	}
	
	@Override
	public void setReplacementPolicy(Object replacementPolicy) {
		this.replacementPolicy = (ReplacementPolicy) replacementPolicy;
	}
}

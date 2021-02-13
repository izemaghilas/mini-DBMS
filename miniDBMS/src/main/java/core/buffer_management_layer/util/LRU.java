package core.buffer_management_layer.util;

import java.util.ArrayList;
import java.util.List;

import core.buffer_management_layer.PageIdentifier;
import core.buffer_management_layer.Pool;
import core.buffer_management_layer.ReplacementPolicy;

/**
 * define LRU replacement policy
 */
public enum LRU implements ReplacementPolicy {
	INSTANCE;
	
	//TODO: for LRU add method move frame to the end
	
	private static List<Frame> FIFO = new ArrayList<>();
	
	public byte[] getPage(Pool bufferPool, PageIdentifier pageId, byte[] pageBuffer) {
		Frame frameToReplace = getFrameToReplace();
		return frameToReplace.getPageBuffer();
	}

	@Override
	public void applyTo(Pool pool, PageIdentifier pageId, byte[] pageBuffer) 
			throws DirtyFrameException, InUseFrameException {
		Frame frame = getFrameToReplace();
		pool.replace(frame, pageId, pageBuffer);
	}
	
	private Frame getFrameToReplace() {
		Frame frameToReplace = FIFO.get(0);
		FIFO.remove(0);
		
		return frameToReplace;
	}

	@Override
	public void reset() {
		FIFO.clear();
	}
}
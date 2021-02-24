package dbms.core.buffer_management_layer.util;

import java.util.ArrayList;
import java.util.List;

import dbms.core.buffer_management_layer.LRUReplacementPolicy;
import dbms.core.buffer_management_layer.PageIdentifier;
import dbms.core.buffer_management_layer.Pool;

/**
 * define LRU replacement policy
 */
public enum LRU implements LRUReplacementPolicy {
	INSTANCE;
	
	private List<Frame> FIFO;
	
	@Override
	public void init() {
		FIFO = new ArrayList<>();
	}
	
	@Override
	public void applyTo(Pool pool, PageIdentifier pageId, byte[] pageBuffer) 
			throws DirtyFrameException, InUseFrameException {
		
		Frame frame = getFrameToReplace();
		pool.replace(frame, pageId, pageBuffer);
		
	}
	
	private Frame getFrameToReplace() {
		Frame frameToReplace = FIFO.remove(0);
		
		return frameToReplace;
	}
	
	@Override
	public void reset() {
		FIFO.clear();
	}

	@Override
	public void moveFrame(Frame frame) {
		int index = FIFO.indexOf(frame);
		if(index==-1)
			FIFO.add(frame);
		else {
			FIFO.remove(index);
			FIFO.add(frame);
		}
	}

	

}
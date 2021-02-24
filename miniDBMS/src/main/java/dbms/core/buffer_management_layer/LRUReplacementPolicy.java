package dbms.core.buffer_management_layer;

import dbms.core.buffer_management_layer.util.DirtyFrameException;
import dbms.core.buffer_management_layer.util.Frame;
import dbms.core.buffer_management_layer.util.InUseFrameException;

public interface LRUReplacementPolicy {
	
	/**
	 * apply LRU replacement policy algorithm
	 * @param pool
	 * @param pageBuffer 
	 * @param pageId 
	 * @throws InUseFrameException, DirtyFrameException 
	 */
	public void applyTo(Pool pool, PageIdentifier pageId, byte[] pageBuffer) 
			throws DirtyFrameException, InUseFrameException;
	
	public void init();
	
	public void reset();
	
	public void moveFrame(Frame frame);
	
}

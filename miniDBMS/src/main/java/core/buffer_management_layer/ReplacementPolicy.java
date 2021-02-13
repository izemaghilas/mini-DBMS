package core.buffer_management_layer;

import core.buffer_management_layer.util.DirtyFrameException;
import core.buffer_management_layer.util.InUseFrameException;

public interface ReplacementPolicy {
	
	/**
	 * apply the replacement policy algorithm
	 * @param pool
	 * @param pageBuffer 
	 * @param pageId 
	 * @throws InUseFrameException, DirtyFrameException 
	 */
	public void applyTo(Pool pool, PageIdentifier pageId, byte[] pageBuffer) 
			throws DirtyFrameException, InUseFrameException;
	
	public void reset();
}

package dbms.core.buffer_management_layer.util;

/**
 * 
 */
public class DirtyFrameException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1936483182167364618L;
	
	private Frame frameToWriteInDisk;

	public DirtyFrameException(Frame frameToWriteInDisk) {
		super();
		this.frameToWriteInDisk = frameToWriteInDisk;
	}

	public Frame getFrameToWriteInDisk() {
		return frameToWriteInDisk;
	}
	
}

package core.buffer_management_layer.util;

import java.util.ArrayList;
import java.util.List;

import core.Constants;
import core.buffer_management_layer.PageIdentifier;
import core.buffer_management_layer.Pool;

public enum BufferPool implements Pool{
	INSTANCE;

	private List<Frame> frames;
	
	public void init() {
		frames = new ArrayList<>();
	}

	public List<Frame> getFrames() {
		return frames;
	}

	public void setFrames(List<Frame> frames) {
		this.frames = frames;
	}

	@Override
	public boolean isEmpty() {
		return frames.isEmpty();
	}

	@Override
	public void addFrame(PageIdentifier pageId, byte[] pageBuffer) throws FullBufferPoolException {
		if (frames.size() < Constants.FRAME_COUNT)
			frames.add(new Frame(pageId, 1, false, pageBuffer));
		else
			throw new FullBufferPoolException("Full BufferPool");

	}

	@Override
	public void remove(Frame frameToRemove) throws DirtyFrameException {

	}

	@Override
	public void replace(Frame frameToReplace, PageIdentifier pageId, byte[] pageBuffer) 
			throws DirtyFrameException, InUseFrameException {
		
		int frameIndex = frames.indexOf(frameToReplace);
		Frame frame = frames.get(frameIndex);
		
		if (frame.getPinCount() > 0)
			throw new InUseFrameException();
		
		if(frame.getPinCount() == 0 && frame.isFlagDirty())
			throw new DirtyFrameException(frame);

		frames.set(frameIndex, new Frame(pageId, 1, false, pageBuffer));
	}

	@Override
	public byte[] getPage(PageIdentifier pageId) {
		Frame frame = findFrameBy(pageId);
		if(frame != null) {
			frame.incrementPinCount();
			return frame.getPageBuffer();
		}
		return null;
	}
	
	@Override
	public void freePage(PageIdentifier pageId, boolean isDirty) {
		Frame frame = findFrameBy(pageId);
		frame.setFlagDirty(isDirty);
		frame.decrementPinCount();
	}

	private Frame findFrameBy(PageIdentifier pageId) {
		// frame equal frame1 if frame.pageid equal frame1.pageid
		Frame frameToFound = new Frame(pageId, 0, false, null);
		int frameIndex = frames.indexOf(frameToFound);
		if (frameIndex > -1) {
			frames.get(frameIndex).incrementPinCount();
			return frames.get(frameIndex);
		}
		return null;
	}

	@Override
	public void reset() {
		frames.clear();
	}

}

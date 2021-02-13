package core.buffermanagerlayer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.Constants;
import core.buffer_management_layer.PageIdentifier;
import core.buffer_management_layer.util.BufferPool;
import core.buffer_management_layer.util.DirtyFrameException;
import core.buffer_management_layer.util.Frame;
import core.buffer_management_layer.util.FullBufferPoolException;
import core.buffer_management_layer.util.InUseFrameException;

public class BufferPoolTest {
	@Test
	public void empty_is_true() {
		//Given
		BufferPool.INSTANCE.init();
		
		//When
		boolean empty=BufferPool.INSTANCE.isEmpty();
		
		//Then
		Assertions.assertTrue(empty);
	}
	
	@Test
	public void empty_is_false() {
		//Given
		BufferPool.INSTANCE.init();
		BufferPool.INSTANCE.setFrames(
				Arrays.asList(new Frame(new PageIdentifier(), 0, false, new byte[Constants.PAGE_SIZE]))
				);
		//When
		boolean empty=BufferPool.INSTANCE.isEmpty();
		
		//Then
		Assertions.assertFalse(empty);
	}
	
	@Test
	public void should_throw_FullBufferPoolException() {
		//Given
		BufferPool.INSTANCE.init();
		List<Frame>list=new ArrayList<>();
		list.add(new Frame(new PageIdentifier(), 0, false, null));
		list.add(new Frame(new PageIdentifier(), 0, false, null));
		BufferPool.INSTANCE.setFrames(list);
		
		//When
		//Then
		Assertions.assertThrows(FullBufferPoolException.class, 
				()->{
					BufferPool.INSTANCE.addFrame(new PageIdentifier(), new byte[Constants.PAGE_SIZE]);
					});
		
	}
	
	@Test
	public void should_throw_DirtyFrameException() {
		//Given
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(0);
		pageId.setPageIndex(1);
		Frame f = new Frame(pageId, 0, true, null);
		
		BufferPool.INSTANCE.init();
		List<Frame>list=new ArrayList<>();
		list.add(f);
		list.add(new Frame(new PageIdentifier(), 0, false, null));
		BufferPool.INSTANCE.setFrames(list);
		
		//When
		//Then
		Assertions.assertThrows(DirtyFrameException.class, 
				()->{
					BufferPool.INSTANCE.replace(f, pageId, new byte[Constants.PAGE_SIZE]);
					});
		
	}
	@Test
	public void should_define_frame_on_DirtyFrameException_object() throws InUseFrameException {
		//Given
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(0);
		pageId.setPageIndex(1);
		Frame f = new Frame(pageId, 0, true, null);
		
		BufferPool.INSTANCE.init();
		List<Frame>list=new ArrayList<>();
		list.add(f);
		list.add(new Frame(new PageIdentifier(), 0, false, null));
		BufferPool.INSTANCE.setFrames(list);
		
		//When
		try {
			BufferPool.INSTANCE.replace(f, pageId, new byte[Constants.PAGE_SIZE]);
		} catch (DirtyFrameException e) {
			//e.printStackTrace();
			//Then
			Assertions.assertEquals(f, e.getFrameToWriteInDisk());
		}
	}
	
	@Test
	public void should_throw_InUseFrameException() {
		//Given
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(0);
		pageId.setPageIndex(1);
		Frame f = new Frame(pageId, 1, false, null);
		
		BufferPool.INSTANCE.init();
		List<Frame>list=new ArrayList<>();
		list.add(f);
		list.add(new Frame(new PageIdentifier(), 0, false, null));
		BufferPool.INSTANCE.setFrames(list);
		
		//When
		//Then
		Assertions.assertThrows(InUseFrameException.class, 
				()->{
					BufferPool.INSTANCE.replace(f, pageId, new byte[Constants.PAGE_SIZE]);
					});
	}
	
	
}

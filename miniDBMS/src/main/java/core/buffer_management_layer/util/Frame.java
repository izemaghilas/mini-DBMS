package core.buffer_management_layer.util;

import core.buffer_management_layer.PageIdentifier;

public class Frame {
	private PageIdentifier pageId;
	private int pinCount;
	private boolean flagDirty;
	private byte[] data;

	public Frame(PageIdentifier pageId, int pinCount, boolean flagDirty, byte[] data) {
		this.pageId = pageId;
		this.pinCount = pinCount;
		this.flagDirty = flagDirty;
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pageId == null) ? 0 : pageId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Frame other = (Frame) obj;
		if (pageId == null) {
			if (other.pageId != null)
				return false;
		} else if (!pageId.equals(other.pageId))
			return false;
		return true;
	}

	public byte[] getPageBuffer() {
		return data;
	}

	public PageIdentifier getPageId() {
		return pageId;
	}

	public int getPinCount() {
		return pinCount;
	}

	public boolean isFlagDirty() {
		return flagDirty;
	}

	public void setPageId(PageIdentifier pageId) {
		this.pageId = pageId;
	}

	public void setPinCount(int pinCount) {
		this.pinCount = pinCount;
	}

	public void setFlagDirty(boolean flagDirty) {
		this.flagDirty = flagDirty;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void incrementPinCount() {
		this.pinCount+=1;
	}
	
	public void decrementPinCount() {
		this.pinCount-=1;
		
	}
	

}

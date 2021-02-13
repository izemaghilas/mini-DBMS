package core.files_management_layer;

import core.buffer_management_layer.PageIdentifier;

public class RecordIdentifier {
	private PageIdentifier pageId;
	private int slotIndex;
	
	public RecordIdentifier() {
	}

	public PageIdentifier getPageId() {
		return pageId;
	}

	public int getSlotIndex() {
		return slotIndex;
	}

	public void setPageId(PageIdentifier pageId) {
		this.pageId = pageId;
	}

	public void setSlotIndex(int slotIndex) {
		this.slotIndex = slotIndex;
	}
	
	
}

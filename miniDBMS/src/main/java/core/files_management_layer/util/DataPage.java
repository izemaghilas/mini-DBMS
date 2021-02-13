package core.files_management_layer.util;

public class DataPage {
	private int dataPageIndex;
	private int freeSlots;
	
	public DataPage() {
	}
	
	public int getDataPageIndex() {
		return dataPageIndex;
	}
	public int getFreeSlots() {
		return freeSlots;
	}
	public void setDataPageIndex(int dataPageIndex) {
		this.dataPageIndex = dataPageIndex;
	}
	public void setFreeSlots(int freeSlots) {
		this.freeSlots = freeSlots;
	}
	
	
}

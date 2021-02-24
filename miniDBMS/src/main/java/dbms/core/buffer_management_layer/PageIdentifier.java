package dbms.core.buffer_management_layer;

/**
 * page on disk with 4096 bytes size
 */
public class PageIdentifier {
	private long fileIndex;
	private long pageIndex;

	public PageIdentifier() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (fileIndex ^ (fileIndex >>> 32));
		result = prime * result + (int) (pageIndex ^ (pageIndex >>> 32));
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
		PageIdentifier other = (PageIdentifier) obj;
		if (fileIndex != other.fileIndex)
			return false;
		if (pageIndex != other.pageIndex)
			return false;
		return true;
	}

	public long getFileIndex() {
		return fileIndex;
	}

	public long getPageIndex() {
		return pageIndex;
	}

	public void setFileIndex(long fileIndex) {
		this.fileIndex = fileIndex;
	}

	public void setPageIndex(long pageIndex) {
		this.pageIndex = pageIndex;
	}

}

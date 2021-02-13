package core.files_management_layer;

import java.io.Serializable;
import java.util.List;

import core.Constants;

/**
 * define relation structure the relation is a table. 
 * used types : int, float and fixed length string noted as stringT where T is the length. 
 * columns names: for a relation with N columns, the columns are C1,C2,...,CN (columns names not stored)
 */
public class RelationDefinition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1555877738068465759L;
	
	private String name;
	private int numberOfColumns;
	private List<String> columnsTypes;
	private int recordSize;
	private int slotCount;
	private int fileIndex;
	
	public static class Builder {
		private String name;
		private int numberOfColumns;
		private List<String> columnsTypes;
		private int recordSize;
		private int slotCount;
		private int fileIndex;
		
		public Builder withRelationName(String name) {
			this.name=name;
			return this;
		}
		public Builder withRelationNumberOfColumns(String numberOfColumns) {
			this.numberOfColumns=Integer.parseInt(numberOfColumns);
			return this;
		}
		public Builder withRelationColumnsTypes(List<String> columnsTypes) {
			this.columnsTypes=columnsTypes;
			return this;
		}
		public Builder withRelationRecordSize(int recordSize) {
			this.recordSize=recordSize;
			return this;
		}
		public Builder withRelationSlotCount(int recordSize) {
			this.slotCount=Constants.PAGE_SIZE/recordSize;
			return this;
		}
		public Builder withRelationFileIndex(int fileIndex) {
			this.fileIndex=fileIndex;
			return this;
		}
		public RelationDefinition build() {
			RelationDefinition relation = new RelationDefinition();
			relation.setName(this.name);
			relation.setNumberOfColumns(this.numberOfColumns);
			relation.setColumnsTypes(this.columnsTypes);
			relation.setRecordSize(this.recordSize);
			relation.setSlotCount(this.slotCount);
			relation.setFileIndex(this.fileIndex);			
			return relation;
		}
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnsTypes == null) ? 0 : columnsTypes.hashCode());
		result = prime * result + fileIndex;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numberOfColumns;
		result = prime * result + recordSize;
		result = prime * result + slotCount;
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
		RelationDefinition other = (RelationDefinition) obj;
		if (columnsTypes == null) {
			if (other.columnsTypes != null)
				return false;
		} else if (!columnsTypes.equals(other.columnsTypes))
			return false;
		if (fileIndex != other.fileIndex)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberOfColumns != other.numberOfColumns)
			return false;
		if (recordSize != other.recordSize)
			return false;
		if (slotCount != other.slotCount)
			return false;
		return true;
	}

	
	public int getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(int fileIndex) {
		this.fileIndex = fileIndex;
	}

	public int getRecordSize() {
		return recordSize;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public void setSlotCount(int slotCount) {
		this.slotCount = slotCount;
	}

	public RelationDefinition() {

	}

	public String getName() {
		return name;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public List<String> getColumnsTypes() {
		return columnsTypes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public void setColumnsTypes(List<String> columnsTypes) {
		this.columnsTypes = columnsTypes;
	}

}

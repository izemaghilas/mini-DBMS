package dbms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;



public interface DataBaseManagement {
	
	public void init() throws FileNotFoundException, ClassNotFoundException, IOException;
	
	public void clean() throws IOException;
	
	public void finish() throws FileNotFoundException, ClassNotFoundException, IOException;
	
	public void createRelation(String relationName, int numberOfColumns, List<String> columns) throws IOException;
	
	public void insertRecord(String relationName, List<String> recordValues) throws FileNotFoundException, IOException;
	
	public String getRecords(String relationName) throws FileNotFoundException, IOException;
	
	public String getFilteredRecords(String relationName, int columnIndex, String value) throws FileNotFoundException, IOException;
	
	public DataBaseDefinition getDataBaseDefinition();
	
	public FileManagement getFileManager(); 
	
	public BufferManagement getBufferManager();
	
	public DiskSpaceManagement getDiskManager();
	
	public void setDataBaseDefintion(DataBaseDefinition dataBaseDefintion);
	
	public void setFileManagement(FileManagement fileManager);
	
	public void setBufferManagement(BufferManagement bufferManager);
	
	public void setDiskSpaceManagement(DiskSpaceManagement diskManager);
}


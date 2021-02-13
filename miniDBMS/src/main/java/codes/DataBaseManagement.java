package codes;

import java.io.FileNotFoundException;
import java.io.IOException;

import core.files_management_layer.DataBaseDefinition;

public interface DataBaseManagement {
	
	public void init() throws FileNotFoundException, ClassNotFoundException, IOException;
	
	public void clean() throws IOException;
	
	public void finish();
	
	public DataBaseDefinition getDataBaseDefinition();
	
	public FileManagement getFileManager(); 
	
	public BufferManagement getBufferManager();
	
	public DiskSpaceManagement getDiskManager();
}


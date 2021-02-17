package core.database_management_layer;

import java.io.FileNotFoundException;
import java.io.IOException;

import codes.BufferManagement;
import codes.DataBaseManagement;
import codes.DiskSpaceManagement;
import codes.FileManagement;

public enum DataBaseManager implements DataBaseManagement {
	INSTANCE;
	
	private DataBaseDefinition dataBaseDefinition;
	private BufferManagement bufferManager;
	private DiskSpaceManagement diskManager;
	private FileManagement fileManager;
	
	@Override
	public void init() throws FileNotFoundException, ClassNotFoundException, IOException {
		dataBaseDefinition.init();
		fileManager.init(INSTANCE);
		bufferManager.init();
	}

	@Override
	public void clean() throws IOException {
		fileManager.reset();
		dataBaseDefinition.reset();
		bufferManager.reset();
		diskManager.reset();
	}

	@Override
	public DataBaseDefinition getDataBaseDefinition() {
		return dataBaseDefinition;
	}

	@Override
	public FileManagement getFileManager() {
		return fileManager;
	}

	@Override
	public BufferManagement getBufferManager() {
		return bufferManager;
	}

	@Override
	public DiskSpaceManagement getDiskManager() {
		return diskManager;
	}

	@Override
	public void finish() {
		
	}

	@Override
	public void setDataBaseDefintion(Object dataBaseDefintion) {
		this.dataBaseDefinition = (DataBaseDefinition) dataBaseDefintion;
	}

	@Override
	public void setFileManagement(FileManagement fileManagement) {
		this.fileManager = fileManagement;
	}

	@Override
	public void setBufferManagement(BufferManagement bufferManagement) {
		this.bufferManager = bufferManagement;
	}

	@Override
	public void setDiskSpaceManagement(DiskSpaceManagement diskSpaceManagement) {
		this.diskManager = diskSpaceManagement;
	}

}

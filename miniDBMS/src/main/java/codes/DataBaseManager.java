package codes;

import java.io.FileNotFoundException;
import java.io.IOException;

import core.buffer_management_layer.BufferManager;
import core.disk_space_management_layer.DiskManager;
import core.files_management_layer.DataBaseDefinition;
import core.files_management_layer.FileManager;

public enum DataBaseManager implements DataBaseManagement {
	INSTANCE;
	
	@Override
	public void init() throws FileNotFoundException, ClassNotFoundException, IOException {
		DataBaseDefinition.INSTANCE.init();
		FileManager.INSTANCE.init(INSTANCE);
		//BufferManager.INSTANCE
	}

	@Override
	public void clean() throws IOException {
		FileManager.INSTANCE.reset();
		DataBaseDefinition.INSTANCE.reset();
		BufferManager.INSTANCE.reset();
		DiskManager.INSTANCE.reset();
	}

	@Override
	public DataBaseDefinition getDataBaseDefinition() {
		return DataBaseDefinition.INSTANCE;
	}

	@Override
	public FileManagement getFileManager() {
		return FileManager.INSTANCE;
	}

	@Override
	public BufferManagement getBufferManager() {
		return BufferManager.INSTANCE;
	}

	@Override
	public DiskSpaceManagement getDiskManager() {
		return DiskManager.INSTANCE;
	}

	@Override
	public void finish() {
		
	}

}

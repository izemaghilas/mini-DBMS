package core.disk_space_management_layer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import codes.DiskSpaceManagement;
import core.Constants;
import core.buffer_management_layer.PageIdentifier;

public enum DiskManager implements DiskSpaceManagement {
	INSTANCE;
	
	@Override
	public void createFile(long fileIndex){
		if(Files.notExists(Paths.get(Constants.DB_DIRECTORY))) {
			try {
				Files.createDirectory(Paths.get(Constants.DB_DIRECTORY));
			} catch (IOException e) {
				// TODO  diskManagerException
			}
		}
		Path dataPath = Path.of(Constants.DB_DIRECTORY + "/DATA_" + fileIndex + ".rf");
		try {
			Files.createFile(dataPath);
		} catch (IOException e) {
			// TODO diskManagerException
		}
	}
	
	@Override
	public void addPage(long fileIndex, PageIdentifier pageIdentifier){

		Path pathToDataFile = Path.of(Constants.DB_DIRECTORY + "/DATA_" + fileIndex + ".rf");

		try(RandomAccessFile raf = new RandomAccessFile(pathToDataFile.toFile(), "rw")){
			long fileLength = raf.length();
			raf.seek(fileLength);
			raf.write(new byte[Constants.PAGE_SIZE]);
			fileLength=raf.length();
			pageIdentifier.setFileIndex(fileIndex);
			pageIdentifier.setPageIndex(fileLength / Constants.PAGE_SIZE -1 );
			
			raf.close();
		} catch (FileNotFoundException e) {
			//TODO: diskManagerException
		} catch (IOException e) {
			//TODO: diskManagerException
		}

	}

	@Override
	public void readPage(PageIdentifier pageId, byte[] pageBuffer) {

		Path pathToDataFile = Path.of(Constants.DB_DIRECTORY + "/DATA_" + pageId.getFileIndex() + ".rf");

		try(RandomAccessFile raf = new RandomAccessFile(pathToDataFile.toFile(), "r")){
			long pageIndex = pageId.getPageIndex() * Constants.PAGE_SIZE;

			raf.seek(pageIndex);
			raf.read(pageBuffer, 0, Constants.PAGE_SIZE);

			raf.close();
		} catch (FileNotFoundException e) {
			//TODO: diskManagerException
		} catch (IOException e) {
			//TODO: diskManagerException
		}

	}

	@Override
	public void writePage(PageIdentifier pageId, byte[] pageBuffer){
		Path pathToDataFile = Path.of(Constants.DB_DIRECTORY + "/DATA_" + pageId.getFileIndex() + ".rf");
		try(RandomAccessFile raf = new RandomAccessFile(pathToDataFile.toFile(), "rw")){
			long pageIndex = pageId.getPageIndex() * Constants.PAGE_SIZE;

			raf.seek(pageIndex);
			raf.write(pageBuffer);

			raf.close();
		} catch (FileNotFoundException e) {
			//TODO: diskManagerException
		} catch (IOException e) {
			//TODO: diskManagerException
		}
		
		
	}

	@Override
	public void reset(){
		for(File file : Path.of(Constants.DB_DIRECTORY).toFile().listFiles()) {
			file.delete();
		}
	}

}

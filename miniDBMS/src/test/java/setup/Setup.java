package setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import dbms.DataBaseManagement;
import dbms.core.Constants;
import dbms.core.buffer_management_layer.PageIdentifier;
import dbms.core.database_management_layer.RelationDefinition;

public class Setup {
	
	private Setup() {
	}
	
	
	private static void fillDataPage(byte[] buffer) {
		int recordSize=48;
		int slotCount=Constants.PAGE_SIZE/recordSize;
		
		try(BufferedReader br = new BufferedReader(new FileReader("src/test/resources/data_set_get_all_records.csv"))){
			String record="";
			int index=0; // index in slot count
			while( (record= br.readLine())!=null ) {
				record = record.replace(",", ".");
				String[] values = record.split(";");
				int offset= index*recordSize + slotCount;
				ByteBuffer.wrap(buffer).put(index, (byte)1);
				
				ByteBuffer.wrap(buffer).putInt(offset, Integer.parseInt(values[0]));
				offset+=4;
				ByteBuffer.wrap(buffer).putFloat(offset, Float.parseFloat(values[1]));
				offset+=4;
				for(int i=0; i<values[2].length(); i++) {
					ByteBuffer.wrap(buffer).putChar(offset, values[2].charAt(i));
					offset+=2;
				}
				index+=1;
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getFirstLineFromDataSet() throws FileNotFoundException{
		Scanner scan = new Scanner(new File("src/test/resources/data_set_get_all_records.csv"));
		String first_line = scan.nextLine();
		first_line = first_line.replace(",", ".");
		String[] values = first_line.split(";");
		List<String> recordValues = List.of(values[0], values[1], values[2]); 
		scan.close();
		
		return recordValues;
	}
	
	public static void fillDB(DataBaseManagement dataBaseManager, RelationDefinition relation) throws IOException {
		PageIdentifier headerPageId = new PageIdentifier();
		headerPageId.setFileIndex(relation.getFileIndex());
		headerPageId.setPageIndex(0);
		
		PageIdentifier pageId = new PageIdentifier();
		pageId.setFileIndex(relation.getFileIndex());
		pageId.setPageIndex(1);//DB with one Data Page
		
		byte[] headerPageBuffer = new byte[Constants.PAGE_SIZE];
		byte[] pageBuffer = new byte[Constants.PAGE_SIZE];
		fillDataPage(pageBuffer);
		
		dataBaseManager.getDiskManager().writePage(pageId, pageBuffer);
		
		int offset=0;
		ByteBuffer.wrap(headerPageBuffer).putInt(offset, 1);
		offset+=4;
		ByteBuffer.wrap(headerPageBuffer).putInt(offset, 1);
		offset+=4;
		ByteBuffer.wrap(headerPageBuffer).putInt(offset, 15);
		
		
		dataBaseManager.getDiskManager().writePage(headerPageId, headerPageBuffer);
		
	}
	
	public static void initDB(DataBaseManagement dataBaseManager, RelationDefinition relation) throws IOException {
		
		dataBaseManager.getDiskManager().createFile(relation.getFileIndex());
		
		PageIdentifier headerPageId = new PageIdentifier();
		headerPageId.setFileIndex(relation.getFileIndex());
		headerPageId.setPageIndex(0);
		dataBaseManager.getDiskManager().addPage(relation.getFileIndex(), headerPageId);
	}
	
	public static void resetBufferManager(DataBaseManagement dataBaseManager) {
		dataBaseManager.getBufferManager().reset();
	}
	
	public static void deleteDB() throws IOException {
		for(File file: Path.of(Constants.DB_DIRECTORY).toFile().listFiles()) {
			file.delete();
		}
		Files.deleteIfExists(Path.of(Constants.DB_DIRECTORY));
	}
}

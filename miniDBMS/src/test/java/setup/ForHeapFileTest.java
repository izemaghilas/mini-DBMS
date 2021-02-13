package setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import core.Constants;

public class ForHeapFileTest {
	
	private ForHeapFileTest() {
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
	
	public static void initDB() {
		byte[] headerPageBuffer = new byte[Constants.PAGE_SIZE];
		byte[] pageBuffer = new byte[Constants.PAGE_SIZE];
		fillDataPage(pageBuffer);
		
		headerPageBuffer[3]=1;
		headerPageBuffer[7]=1;
		headerPageBuffer[11]=76;
		
		//write to DB
		try(RandomAccessFile raf = new RandomAccessFile(new File("DB/DATA_0.rf"), "rw")){
			
			//raf.seek(0);
			raf.write(headerPageBuffer);
			
			//raf.seek(raf.length());
			raf.write(pageBuffer);
			
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteDB() throws IOException {
		for(File file: Path.of(Constants.DB_DIRECTORY).toFile().listFiles()) {
			file.delete();
		}
		Files.deleteIfExists(Path.of(Constants.DB_DIRECTORY));
	}
}

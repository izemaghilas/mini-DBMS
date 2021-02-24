package dbms.core.util;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntSupplier;


public class ColumnTypeManager {
	private static int STRING_T_LENGTH;
	private static int STRING_T_SIZE;
	private static int offset;
	
	private String type;
	private BiConsumer<String, byte[]> writeToBuffer;
	private Function<byte[], String> readFromBuffer;
	private IntSupplier getTypeSize;
	
	private ColumnTypeManager(
			String type, 
			BiConsumer<String, byte[]> writeToBuffer, 
			Function<byte[], String> readFromBuffer,
			IntSupplier getTypeSize) {
		
		this.type = type;
		this.writeToBuffer = writeToBuffer;
		this.readFromBuffer = readFromBuffer;
		this.getTypeSize = getTypeSize;
	}
	
	public String readRecordValueFromBuffer(byte[] buffer) {
		return readFromBuffer.apply(buffer);
	}
	
	public void writeRecordValueToBuffer(String recordValue, byte[] pageBuffer) {
		writeToBuffer.accept(recordValue, pageBuffer);
	}
	
	public int getSize() {
		return getTypeSize.getAsInt();
	}
	
	
	private final static ColumnTypeManager INT_DATA_TYPE = new ColumnTypeManager(
				"INT",
				(recordValue, buffer)->{
					int value = Integer.parseInt(recordValue);
					ByteBuffer.wrap(buffer).putInt(offset, value);
					offset+=Integer.SIZE/8;
				},
				buffer -> {
					int value = ByteBuffer.wrap(buffer).getInt(offset);
					offset+=Integer.SIZE/8;
					return ""+value;
				},
				() -> {
					return Integer.SIZE/8;
				}
			);
	
	private final static ColumnTypeManager FLOAT_DATA_TYPE = new ColumnTypeManager(
				"FLOAT",
				(recordValue, buffer)->{
					float value = Float.parseFloat(recordValue);
					ByteBuffer.wrap(buffer).putFloat(offset, value);
					offset+=Float.SIZE/8;
				},
				buffer -> {
					float value = ByteBuffer.wrap(buffer).getFloat(offset);
					offset+=Float.SIZE/8;
					return ""+value;
				},
				() -> {
					return Float.SIZE/8;
				}
			);
	
	private final static ColumnTypeManager STRING_DATA_TYPE = new ColumnTypeManager(
				"^STRING[0-9]+$",
				(recordValue, buffer)->{
					for(int i=0; i<recordValue.length(); i++) {
						ByteBuffer.wrap(buffer).putChar(offset, recordValue.charAt(i));
						offset+=Character.SIZE/8;
					}
				},
				buffer -> {
					String value="";
					for(int i=0; i<STRING_T_LENGTH; i++) {
						value+=ByteBuffer.wrap(buffer).getChar(offset);
						offset+=Character.SIZE/8;
					}
					return value.trim();
				},
				() -> {
					return ColumnTypeManager.STRING_T_SIZE;
				}
			);
	
	private final static List<ColumnTypeManager> columnTypesManagers = List.of(INT_DATA_TYPE, FLOAT_DATA_TYPE);
	
	private static void setStringDataTypeLength(String type) {
		String length = type.substring(6);
		STRING_T_LENGTH = Integer.parseInt(length);
		STRING_T_SIZE = STRING_T_LENGTH*Character.SIZE/8;
	}
	
	public static void setOffset(int offset) {
		ColumnTypeManager.offset = offset;
	}
	
	public static ColumnTypeManager of(String dataType) {
		if(dataType.toUpperCase().matches(STRING_DATA_TYPE.type)) {
			setStringDataTypeLength(dataType);
			return STRING_DATA_TYPE;
		}
		
		return columnTypesManagers.stream()
				.filter(columnTypeManager -> dataType.toUpperCase().matches(columnTypeManager.type))
				.findAny()
				.orElseThrow();
	}


}

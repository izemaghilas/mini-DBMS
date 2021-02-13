package core.files_management_layer.util;


import org.junit.jupiter.api.Test;

class RecordManagerTest {
	
	/*
	@Test
	void testWriteRecordValueInBuffer() {
		//Given
		List<String> types = List.of("int", "float", "string12");
		List<String> recordValues = List.of("1","2.556","test2020");
		
		byte[] buffer1 = new byte[32];
		ByteBuffer.wrap(buffer1).putInt(0, 1);
		ByteBuffer.wrap(buffer1).putFloat(4, 2.556f);
		String s="test2020";
		int offset=8;
		for(int i=0; i<s.length(); i++) {
			ByteBuffer.wrap(buffer1).putChar(offset, s.charAt(i));
			offset+=2;
		}
			
		byte[] buffer = new byte[32];
		
		//When
		RecordManager.setOffset(0);
		for(int i=0; i<recordValues.size();i++) {
			RecordManager recordManager = RecordManager.of(types.get(i));
			String recordValue=recordValues.get(i);
			
			recordManager.writeRecordValueInBuffer(recordValue, buffer);
		}
		
		//Then
		Assertions.assertArrayEquals(buffer1, buffer);
	}
	*/
	
	@Test
	void testOf() {
	}

}

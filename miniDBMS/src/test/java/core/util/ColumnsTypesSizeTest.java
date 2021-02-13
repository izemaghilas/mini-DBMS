package core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ColumnsTypesSizeTest {
	@Test
	public void should_return_4_int() {
		//Given
		String type="int";
		ColumnsTypes t = ColumnsTypes.of(type);
		
		//When
		int size=t.getTypeSize(type);
		
		//Then
		Assertions.assertEquals(4, size);
	}
	@Test
	public void should_return_4_float() {
		//Given
		String type="float";
		ColumnsTypes t = ColumnsTypes.of(type);
		
		//When
		int size=t.getTypeSize(type);
		
		//Then
		Assertions.assertEquals(4, size);
	}
	
	@Test
	public void should_return_8() {
		//Given
		String type="string4";
		ColumnsTypes t = ColumnsTypes.of(type);
		
		//When
		int size=t.getTypeSize(type);
		
		//Then
		Assertions.assertEquals(8, size);
	}
}

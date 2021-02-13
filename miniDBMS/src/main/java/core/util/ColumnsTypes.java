package core.util;

import java.util.List;
import java.util.function.Function;

public class ColumnsTypes {
	private String columnType;
	private Function<String, Integer> getTypeSize;
	
	
	private ColumnsTypes(String columnType, Function<String, Integer> getTypeSize) {
		this.columnType = columnType;
		this.getTypeSize = getTypeSize;
	}
	
	private final static ColumnsTypes INT_SIZE = new ColumnsTypes(
				"INT",
				type->{
					return 4;
				}
			); 
	private final static ColumnsTypes FLOAT_SIZE = new ColumnsTypes(
			"FLOAT",
			type->{
				return 4;
			}
		); 
	private final static char CHAR_SIZE=2;
	private final static ColumnsTypes STRING_SIZE = new ColumnsTypes(
			"^STRING[0-9]+$",
			type->{
				int stringLength = extracteStringLength(type);
				return stringLength*CHAR_SIZE;
			}
		);

	private static int extracteStringLength(String type) {
		String length = type.substring(6);
		int stringLength=Integer.parseInt(length);
		return stringLength;
	} 
	
	private final static List<ColumnsTypes> types_size = List.of(
				INT_SIZE,
				FLOAT_SIZE,
				STRING_SIZE
			);
	
	public static ColumnsTypes of(String columnType) {
		return types_size.stream()
				.filter(
						type->columnType.toUpperCase().matches(type.columnType)
				)
				.findAny()
				.orElseThrow();
	}
	
	public int getTypeSize(String type) {
		return getTypeSize.apply(type);
	}
	
}

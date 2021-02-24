package dbms.core.files_management_layer;

import java.util.List;

public class Record {
	private List<String> values;
	
	public Record() {
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	
	@Override
	public String toString() {
		String record="";
		for(String value: values)
			record += value+" ";
		
		return record.trim();
	}
	
}

package dbms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dbms.core.database_management_layer.RelationDefinition;

public interface DataBaseDefinition {

	public void init() throws FileNotFoundException, IOException, ClassNotFoundException;
	
	public void reset() throws IOException;
	
	public List<RelationDefinition> getRelations();
	
	public int getNumberOfRelations();
	
	public void setRelations(List<RelationDefinition> relations);
	
	public void setNumberOfRelations(int numberOfRelations);
	
	public void addRelation(RelationDefinition relation);
	
	public void finish() throws FileNotFoundException, IOException, ClassNotFoundException;
		
}

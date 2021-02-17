package core.database_management_layer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import core.Constants;
import core.files_management_layer.RelationDefinition;

/**
 * define database structure of mini DBMS with one database
 */
public enum DataBaseDefinition {
	INSTANCE;

	private List<RelationDefinition> relations;
	private int numberOfRelations;
	 
	public void init() throws FileNotFoundException, IOException, ClassNotFoundException {
		relations = new ArrayList<>();
		numberOfRelations=0;
		
		if ( Files.exists(Paths.get(Constants.DB_DIRECTORY+"/Catalog.def"))
				&& Files.size(Paths.get(Constants.DB_DIRECTORY+"/Catalog.def")) > 0
				) {
			
			ObjectInputStream deserializer = new ObjectInputStream(
						new FileInputStream(
									new File(Constants.DB_DIRECTORY+"/Catalog.def")
								)
					);
			
			deserializer.readObject();
			readObject(deserializer);
			
			deserializer.close();
		}
	}
	
	private void readObject(ObjectInputStream deserializer) throws ClassNotFoundException, IOException {
		numberOfRelations = deserializer.readInt();
		
		for(int i=0; i<numberOfRelations;i++)
			relations.add((RelationDefinition) deserializer.readObject());
	}
	
	private void writeObject(ObjectOutputStream serializer) throws IOException{
		if(numberOfRelations > 0) {			
			serializer.writeInt(numberOfRelations);
			
			for(RelationDefinition relation: relations)
				serializer.writeObject(relation);
		}
	}
	
	public void reset() throws IOException {
		relations.clear();
		numberOfRelations=0;
		Files.deleteIfExists(Paths.get(Constants.DB_DIRECTORY+"/Catalog.def"));
	}

	public List<RelationDefinition> getRelations() {
		return relations;
	}

	public int getNumberOfRelations() {
		return numberOfRelations;
	}

	public void setRelations(List<RelationDefinition> relations) {
		this.relations = relations;
	}

	public void setNumberOfRelations(int numberOfRelations) {
		this.numberOfRelations = numberOfRelations;
	}

	public void addRelation(RelationDefinition relation) {
		relations.add(relation);
		numberOfRelations++;
	}

	public void finish() throws FileNotFoundException, IOException, ClassNotFoundException {
		
		if (!Files.exists( Paths.get(Constants.DB_DIRECTORY+"/Catalog.def") ) )
			Files.createFile( Paths.get(Constants.DB_DIRECTORY+"/Catalog.def") );
		
		ObjectOutputStream serializer = new ObjectOutputStream(new FileOutputStream(Constants.DB_DIRECTORY+"/Catalog.def"));
		
		serializer.writeObject(INSTANCE);
		writeObject(serializer);
		
		serializer.close();
	}
}

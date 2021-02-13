package core.files_management_layer;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.Constants;

class DataBaseDefinitionTest {

	@Test
	void testInit() throws FileNotFoundException, ClassNotFoundException, IOException {
		testInit_no_catalog_file();
		testInit_with_empty_catalog_file();
	}
	
	@Test
	private void testInit_no_catalog_file() throws FileNotFoundException, ClassNotFoundException, IOException {
		//Given When
		DataBaseDefinition.INSTANCE.init();
		
		int n = DataBaseDefinition.INSTANCE.getNumberOfRelations();
		List<RelationDefinition> relations = DataBaseDefinition.INSTANCE.getRelations();
		
		//Then
		Assertions.assertEquals(0, n);
		Assertions.assertEquals(0, relations.size());
	}
	
	@Test
	private void testInit_with_empty_catalog_file() throws FileNotFoundException, ClassNotFoundException, IOException {
		createCatalogFile();
		testInit_no_catalog_file();
	}
	private void createCatalogFile() throws IOException {
		Files.createDirectory(Paths.get(Constants.DB_DIRECTORY));
		Files.createFile(Paths.get(Constants.DB_DIRECTORY+"/Catalog.def"));
	}

	@Test
	void testFinish() throws FileNotFoundException, ClassNotFoundException, IOException {
		//Given
		RelationDefinition r1 = new RelationDefinition();
		r1.setName("EXPERTS");
		r1.setNumberOfColumns(2);
		r1.setColumnsTypes(Arrays.asList("string45", "string45"));
		
		RelationDefinition r2 = new RelationDefinition();
		r2.setName("AGENTS");
		r2.setNumberOfColumns(2);
		r2.setColumnsTypes(Arrays.asList("string45", "string45"));
		
		List<RelationDefinition> l = List.of(r1, r2);
		
		
		DataBaseDefinition.INSTANCE.setRelations(new ArrayList<>());
		DataBaseDefinition.INSTANCE.setNumberOfRelations(0);
		DataBaseDefinition.INSTANCE.addRelation(r1);
		DataBaseDefinition.INSTANCE.addRelation(r2);
		
		
		//When
		DataBaseDefinition.INSTANCE.finish();
		DataBaseDefinition.INSTANCE.setRelations(new ArrayList<>());
		DataBaseDefinition.INSTANCE.setNumberOfRelations(0);
		DataBaseDefinition.INSTANCE.init();
		
		//Then
		Assertions.assertEquals(2, DataBaseDefinition.INSTANCE.getNumberOfRelations());
		Assertions.assertIterableEquals(l, DataBaseDefinition.INSTANCE.getRelations());
	}

}

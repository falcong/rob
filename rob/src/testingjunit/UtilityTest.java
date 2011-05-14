package testingjunit;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import rob.Utility;



public class UtilityTest {

	@Before
	public void setUp(){
	}

	@Test
	public void testGetConfigParameter(){
		try{
			String value = Utility.getConfigParameter("testValue");
			String expectedValue = "C:\\This\\Is\\A\\Test";
			assertTrue(value.equalsIgnoreCase(expectedValue));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testOpenFile(){
		//apro
		String fileName = Utility.getConfigParameter("proveVarie")+"\\prova1.txt";
		PrintStream file = Utility.openOutFile(fileName, true);
		
		//scrivo
		file.println("ciao pippo\n");
		
		//chiudo
		file.close();
	}
	
	
	@Test
	public void testWrite(){
		String outFile = Utility.getConfigParameter("proveVarie")+"\\prova2.txt";
		Utility.write(outFile, "ciao\r\n");
		Utility.write(outFile, "a tutti\r\n");
		Utility.write(outFile, "QUANTI!");
	}
}

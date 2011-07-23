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
	
	//test di cloneArray()
	/*
	 * caso generale: controllare che gli oggetti siano differenti
	 */
	@Test
	public final void testCloneArray1(){
		
	}
	
	//test di cloneMatrix()
	/*
	 * caso generale: controllare che gli oggetti siano differenti
	 */
	public final void testCloneMatrix1(){
		
	}
	
	//test di getConfigParameter
	/*
	 * caso generico
	 * parameter assente
	 * parameter presente più volte
	 */
	@Test
	public void testGetConfigParameter1(){
		try{
			String value = Utility.getConfigParameter("testValue");
			String expectedValue = "C:\\This\\Is\\A\\Test";
			assertTrue(value.equalsIgnoreCase(expectedValue));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	//test di openOutFile()
	/*
	 * caso generale
	 * apro file, ci scrivo e leggo (scritto=letto)
	 */
	@Test
	public final void testOpenOutFile(){
		//apro
		String fileName = Utility.getConfigParameter("proveVarie")+"\\prova1.txt";
		PrintStream file = Utility.openOutFile(fileName, true);
		
		//scrivo
		file.println("ciao pippo\n");
		
		//chiudo
		file.close();
	}
	
	//test di write()
	/*
	 * caso generale
	 * scrivi su file e controlla che quanto scrittto sia giusto
	 */
	@Test
	public void testWrite(){
		String outFile = Utility.getConfigParameter("proveVarie")+"\\prova2.txt";
		Utility.write(outFile, "ciao\r\n");
		Utility.write(outFile, "a tutti\r\n");
		Utility.write(outFile, "QUANTI!");
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
	
	
/*	@Test
	public void testOpenOutFile(){
		//apro
		String fileName = Utility.getConfigParameter("proveVarie")+"\\prova1.txt";
		PrintStream file = Utility.openOutFile(fileName, true);
		
		//scrivo
		file.println("ciao pippo\n");
		
		//chiudo
		file.close();
	}
	*/
	

}

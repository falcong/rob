//annarosa
package testingjunit;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Test;

import rob.Utility;



public class UtilityTest {

	//test di cloneArray()
	/*
	 * caso generale: controllare che gli oggetti siano differenti
	 */
	@Test
	public final void testCloneArray1(){
		int [] originalArray = {1, 4 ,2, 7};
		int [] clonedArray = Utility.cloneArray(originalArray);
		//controllo lunghezza
		assertTrue(originalArray.length==clonedArray.length);
		//controllo uguaglianza elementi
		for(int i=0; i<originalArray.length;i++)
			assertTrue(originalArray[i]==clonedArray[i]);
		//controllo che sia diverso il riferimento in memoria
		assertTrue(originalArray!=clonedArray);
	}
	
	//test di cloneMatrix()
	/*
	 * caso generale: controllare che gli oggetti siano differenti
	 */
	@Test
	public final void testCloneMatrix1(){
		final int COLUMNS = 5;
		final int ROWS = 3;
		int [] originalRow0 = {0, 0 , 0, 0, 0};
		int [] originalRow1 = {0, 5 , 20, 2, 5};
		int [] originalRow2 = {0, 2 , 23, 4, 6};
		
		int [][] originalMatrix = new int[ROWS][COLUMNS];
		originalMatrix[0] = originalRow0;
		originalMatrix[1] = originalRow1;
		originalMatrix[2] = originalRow2;
		
		int [][] clonedMatrix = Utility.cloneMatrix(originalMatrix);
		
		//controllo uguaglianza elementi
		for(int i=0; i<originalMatrix.length;i++) {
			for(int j=0; j<originalMatrix[i].length;j++) {
				assertTrue(originalMatrix[i][j]==clonedMatrix[i][j]);
			}
			//controllo ciascuna riga abbia riferimento diverso
			assertTrue(originalMatrix[i]!=clonedMatrix[i]);
		}
				
		//controllo che sia diverso il riferimento in memoria
		assertTrue(originalMatrix!=clonedMatrix);
		
	}
	
	//test di getConfigParameter
	/*
	 * caso generico
	 */
	@Test
	public void testGetConfigParameter1(){
		final String PARAMETER = "testValue";
		final String EXPECTED_VALUE = "C:\\This\\Is\\A\\Test";
		try{
			String value = Utility.getConfigParameter(PARAMETER);
			assertTrue(value.equals(EXPECTED_VALUE));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/*
	 * parametro assente
	 */
	@Test
	public void testGetConfigParameter2(){
		final String PARAMETER = "nonExisting";
		try{
			String value = Utility.getConfigParameter(PARAMETER);
			assertTrue(value==null);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/*
	 * parametro presente più volte (è attesto che il metodo si fermi alla prima occorrenza, quindi deve restituire il primo valore)
	 */
	@Test
	public void testGetConfigParameter3(){
		final String PARAMETER = "testValueR";
		final String EXPECTED_VALUE = "Test1";
		
		try{
			String value = Utility.getConfigParameter(PARAMETER);
			assertTrue(value.equals(EXPECTED_VALUE));
			
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
		final String FILE_NAME="testOpenOutFile.txt";
		final String MESSAGE="Test message";
		final String APPEND_MESSAGE="Append";
		final String lines[] = {MESSAGE, APPEND_MESSAGE};
		//apro con append false
		
		String fullFilePath = Constants.OUTPUT_PATH+System.getProperty("file.separator")+FILE_NAME;
		PrintStream file = Utility.openOutFile(fullFilePath, false);
		
		//scrivo
		file.println(MESSAGE);
		//chiudo
		file.close();
		
		
		//Apro con append true		
		file = Utility.openOutFile(fullFilePath, true);
		
		//scrivo
		file.println(APPEND_MESSAGE);
		//chiudo
		file.close();
		
		assertTrue(verifyOutput(lines, fullFilePath)); 
	}
	
	//test di write()
	/*
	 * caso generale
	 * scrivi su file e controlla che quanto scrittto sia giusto
	 */
	@Test
	public void testWrite(){
		final String FILE_NAME="testWrite.txt";
		final String [] messages = {"First Line", "Second Line", "Third Line"};
		
		String outFile = Constants.OUTPUT_PATH+System.getProperty("file.separator")+FILE_NAME;
		
		//write scrive sempre in append, quindi cancello il file se esiste già prima di eseguire il test (altrimenti il test non è ripetibile)
		File target = new File(outFile);
		if(target.exists())
			target.delete();
		
		for(int i=0; i<messages.length;i++)
			Utility.write(outFile, messages[i]+System.getProperty("line.separator"));
		

		//Test che la scrittura sia giusta
		assertTrue(verifyOutput(messages, outFile)); 
	}

	private boolean verifyOutput(final String[] expectedOutputLines, String file) {
		boolean ok=true;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String strLine;
			int i=0;
			while ((strLine = in.readLine()) != null)   {
				ok = ok && expectedOutputLines[i].equals(strLine);
				i++;
			}
			in.close();
			return ok;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	
	
	
	
	
	

//	@Test
//	public void testGetConfigParameter(){
//		try{
//			String value = Utility.getConfigParameter("testValue");
//			String expectedValue = "C:\\This\\Is\\A\\Test";
//			assertTrue(value.equalsIgnoreCase(expectedValue));
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
//	}
//	
	
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

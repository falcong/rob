package testingjunit;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;

import util.Constants;
import util.Utility;



public class UtilityTest {
	
	//test di getConfigParameter
	/*
	 * caso generico
	 */
	@Test
	public void testGetConfigParameter1() throws Exception{
		final String PARAMETER = "testValue";
		final String EXPECTED_VALUE = "C:\\This\\Is\\A\\Test";
		String value = Utility.getConfigParameter(PARAMETER);
		assertTrue(value.equals(EXPECTED_VALUE));
	}
	
	/*
	 * parametro assente
	 */
	@Test
	public void testGetConfigParameter2() throws Exception{
		final String PARAMETER = "nonExisting";
		String value = Utility.getConfigParameter(PARAMETER);
		assertTrue(value==null);
	}
	
	/*
	 * parametro presente più volte (è attesto che il metodo si fermi alla prima occorrenza, quindi deve restituire il primo valore)
	 */
	@Test
	public void testGetConfigParameter3() throws Exception {
		final String PARAMETER = "testValueR";
		final String EXPECTED_VALUE = "Test1";
		String value = Utility.getConfigParameter(PARAMETER);
		assertTrue(value.equals(EXPECTED_VALUE));
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
	
	//TODO implementare test
	@Test
	public final void testOpenInFile(){
		;
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
}

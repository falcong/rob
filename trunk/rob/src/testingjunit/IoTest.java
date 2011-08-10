package testingjunit;

import static org.junit.Assert.*;

import io.Io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;


public class IoTest {
	
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
		PrintStream file = Io.openOutFile(fullFilePath, false);
		
		//scrivo
		file.println(MESSAGE);
		//chiudo
		file.close();
		
		
		//Apro con append true		
		file = Io.openOutFile(fullFilePath, true);
		
		//scrivo
		file.println(APPEND_MESSAGE);
		//chiudo
		file.close();
		
		assertTrue(verifyOutput(lines, fullFilePath)); 
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
	
	
	
	
	//TODO implementare test
	@Test
	public final void testOpenInFile(){
		;
	}
	
	
	
	
	//test di getConfigParameter
	/*
	 * caso generico
	 */
	@Test
	public void testGetConfigParameter1() throws Exception{
		final String PARAMETER = "testValue";
		final String EXPECTED_VALUE = "C:\\This\\Is\\A\\Test";
		String value = Io.getConfigParameter(PARAMETER);
		assertTrue(value.equals(EXPECTED_VALUE));
	}
	
	/*
	 * parametro assente
	 */
	@Test
	public void testGetConfigParameter2() throws Exception{
		final String PARAMETER = "nonExisting";
		String value = Io.getConfigParameter(PARAMETER);
		assertTrue(value==null);
	}
	
	/*
	 * parametro presente più volte (è attesto che il metodo si fermi alla prima occorrenza, quindi deve restituire il primo valore)
	 */
	@Test
	public void testGetConfigParameter3() throws Exception {
		final String PARAMETER = "testValueR";
		final String EXPECTED_VALUE = "Test1";
		String value = Io.getConfigParameter(PARAMETER);
		assertTrue(value.equals(EXPECTED_VALUE));
	}
}

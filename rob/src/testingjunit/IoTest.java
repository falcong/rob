package testingjunit;

import static org.junit.Assert.*;

import io.Io;

import org.junit.Test;


public class IoTest {
	
	//test di getConfigParameter
	/*
	 * caso generico
	 */
	@Test
	public void testGetConfigParameter1() throws Exception{
		final String PARAMETER = "testValue";
		final String EXPECTED_PARAMETR_VALUE = "C:\\This\\Is\\A\\Test";
		String value = Io.getConfigParameter(PARAMETER);
		assertTrue(value.equals(EXPECTED_PARAMETR_VALUE));
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

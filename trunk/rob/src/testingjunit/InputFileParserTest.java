package testingjunit;

import static org.junit.Assert.*;

import java.io.File;

import io.InputFileParser;

import org.junit.Test;

import rob.Run;
import rob.Utility;

public class InputFileParserTest {

	@Test
	public final void testParse() {
		InputFileParser parser = new InputFileParser();
		String inputFile=Utility.getConfigParameter("testRunFile")+ File.separator + "runFile.txt";
		try {
			Run run = (Run) parser.parse(inputFile);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}

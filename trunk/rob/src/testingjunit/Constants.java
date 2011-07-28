package testingjunit;

import rob.Utility;

public class Constants {
	//percorso della cartella contenente i file di input per il testing
	final static String INPUT_PATH = Utility.getConfigParameter("testInput");
	//percorso della cartella contenente i file di output per il testing
	final static String OUTPUT_PATH = Utility.getConfigParameter("testOutput");
}
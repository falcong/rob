package util;


public class Constants {
	//codice di errore parser
	public final static int ERROR_PARSER = 1;
	
	//percorso della cartella contenente i file di input per il testing
	public final static String INPUT_PATH = Utility.getConfigParameter("testInput");
	//percorso della cartella contenente i file di output per il testing
	public final static String OUTPUT_PATH = Utility.getConfigParameter("testOutput");
}

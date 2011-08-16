package util;



public class Constants {
	//percorso della cartella contenente i file di input per il testing
	public final static String TESTING_INPUT_PATH;
	//percorso della cartella contenente i file di output per il testing
	public final static String TESTING_OUTPUT_PATH;
	
	static{
		try {
			TESTING_INPUT_PATH = Io.getConfigParameter("testInput");
			TESTING_OUTPUT_PATH = Io.getConfigParameter("testOutput");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Non è stato possibile leggere il parametro testInput o testOutput");
		}
	}
}

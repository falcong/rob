package testingjunit;

import io.Io;


public class Constants {
	//percorso della cartella contenente i file di input per il testing
	public final static String INPUT_PATH;
	//percorso della cartella contenente i file di output per il testing
	public final static String OUTPUT_PATH;
	
	static{
		try {
			//percorso della cartella contenente i file di input per il testing
			INPUT_PATH = Io.getConfigParameter("testInput");		
			//percorso della cartella contenente i file di output per il testing
			OUTPUT_PATH = Io.getConfigParameter("testOutput");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Non è stato possibile leggere il parametro testInput o testOutput");
		}
	}
}

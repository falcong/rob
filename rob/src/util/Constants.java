package util;


public class Constants {
	//file di configurazione
	public final static String CONFIG_FILE = "config.txt";
	
	//errore generato dal parser quando fallisce un'operazione
	public final static int ERROR_PARSER = 1;
	//errore generato quando non è possibile aprire il file di configurazione
	public final static int ERROR_CONFIG = 2;
	
	//percorso della cartella contenente i file di input per il testing
	public final static String INPUT_PATH = Utility.getConfigParameter("testInput");
	//percorso della cartella contenente i file di output per il testing
	public final static String OUTPUT_PATH = Utility.getConfigParameter("testOutput");
}

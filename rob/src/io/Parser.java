package io;

import java.util.Observable;

public abstract class Parser extends Observable {
	final int INT_NOT_USED = -1;
	//percorso assoluto della cartella contenente i file di input
	protected String path;
	
	/**
	 * Crea un parser in grado di leggere un file contenente la descrizione di un oggetto e di restituire 
	 * l'Object corrispondente.
	 * path indica la posizione (percorso assoluto) della cartella contenente il file di input (che viene specificato
	 * con il parametro inputFile del metodo parse).
	 */
	public Parser(String path) {
		this.path = path;
	}
	
	
	/**
	 * Effettua il parsing del file di nome inputFile, crea e restituisce l'oggetto descritto in tale file.
	 */
	public abstract Object parse(String inputFile) throws Exception;
}

package io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Io {
	//file di configurazione
	private static final String CONFIG_FILE = "config.txt";
	
	/*
	 * Apre un file il lettura e restituisce il corrispondente BufferedReader. 
	 */
	public static BufferedReader openInFile(String fileName) throws Exception{
		FileInputStream fileInputStream = new FileInputStream(fileName);
		DataInputStream dataInputStream = new DataInputStream(fileInputStream);
		InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream);
		return new BufferedReader(inputStreamReader);
	}
	
	/*
	 * apre un file su cui scrivere e restituisce il Print Stream corrispondente
	 */
	public static PrintStream openOutFile(String fileName, boolean append){
	    try{
	    	FileOutputStream fileOutputStream = new FileOutputStream(fileName, append);
	    	return new PrintStream(fileOutputStream);
	    }catch (IOException e) {
	    	//TODO eliminare try catch e fare throws
	    	throw new Error(e);
	    }
	}
	
	/*
	 * legge da config.txt il parametro specificato
	 * [restituisce null se il parametro non viene trovato]
	 * Se parameter è presente più volte viene restituito il primo valore.
	 * Il file non deve contenere linee vuote.
	 */
	public static String getConfigParameter(String parameter) throws Exception {
		final char COMMENT_DELIMITER = '#';
		final String SEPARATOR = "[\\s]*=[\\s]*";
	
		BufferedReader bufferedReader = Io.openInFile(CONFIG_FILE);
		
		String line;
		boolean found = false;
		String lineElements[] = null;
		String tag;
		String value = null;
		while((line = bufferedReader.readLine()) != null	&&	!found){
			if (line.charAt(0)==COMMENT_DELIMITER)
				continue;
			lineElements = line.split(SEPARATOR);
			tag = lineElements[0];
			value = lineElements[1];

			
			if(tag.equalsIgnoreCase(parameter)){
				found = true;
			}else{
				value= null;
			}
		}
		bufferedReader.close();
		
		return value;
	}
}

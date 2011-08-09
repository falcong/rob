package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Utility {
	
	/*
	 * come sopra ma matrix è di int
	 */
	//TODO eliminare
	//t
/*	public static void printMatrix2D(int matrix[][], String x, String y,
			int i, int j, int numTab1, int numTab2, int numTab3, int numTab4){
		int numRows		= matrix.length; 
		int numColumns	= matrix[0].length;
		
		//intestazione tabella [y-j y-j+1 y-j+2 ...]
		for(int a=j; a<=numColumns-1; a++){
			//la prima volta numTab1 tab poi numTab2
			if(a==j){
				for(int c=1; c<=numTab1; c++){
					System.out.print("\t");
				}
			}else{
				for(int c=1; c<=numTab2; c++){
					System.out.print("\t");
				}
			}
			System.out.print(y+a);
		}
		System.out.println("\n");
		
		//stampo le righe
		for(int a=i; a<=numRows-1; a++){
			//intestazione 
			System.out.print(x+a);
			for(int b=j; b<=numColumns-1; b++){
				//la prima volta numTab3 tab poi numTab4
				if(b==j){
					for(int c=1; c<=numTab3; c++){
						System.out.print("\t");
					}
				}else{
					for(int c=1; c<=numTab4; c++){
						System.out.print("\t");
					}
				}
				System.out.print("("+a+","+b+")="+matrix[a][b]);
			}
			System.out.println("");
		}
		System.out.println("");
	}*/
	
	
	/*
	 * legge da config.txt il parametro specificato
	 * [restituisce null se il parametro non viene trovato]
	 * Se parameter è presente più volte viene restituito il primo valore.
	 * Il file non deve contenere linee vuote.
	 */
	public static String getConfigParameter(String parameter) throws Exception {
		final char COMMENT_DELIMITER = '#';
		final String SEPARATOR = "[\\s]*=[\\s]*";
	
		BufferedReader bufferedReader = Utility.openInFile(Constants.CONFIG_FILE);
		
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
	 * Apre un file il lettura e restituisce il corrispondente. 
	 */
	public static BufferedReader openInFile(String fileName) throws Exception{
		FileInputStream fileInputStream = new FileInputStream(fileName);
		DataInputStream dataInputStream = new DataInputStream(fileInputStream);
		InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream);
		return new BufferedReader(inputStreamReader);
	}
	
	/*
	 * scrive info in modalità append in outFile
	 */
	public static void write(String outFile, String info){
		PrintStream out = openOutFile(outFile, true);
		out.print(info);
		out.close();
	}
	
	/*
	 * Lancia un'eccezione contenente message. 
	 */
	public static void exception(String message) throws Exception{
		System.err.println("Errore:");
		System.err.println(message);
		throw new Exception(message);
	}
	
	/**
	 * Stampa un messaggio di warning.
	 */
	public static void warning(String message){
		System.err.println("Attenzione:");
		System.err.println(message);
	}
}

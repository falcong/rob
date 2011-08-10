package util;

import io.Io;

import java.io.BufferedReader;

public class Utility {	
	
	/*
	 * Lancia un'eccezione contenente message. 
	 */
	public static void exception(String message) throws Exception{
		System.err.println("Errore:");
		System.err.println(message);
		throw new Exception(message);
	}
	
	/*
	 * Stampa un messaggio di warning.
	 */
	public static void warning(String message){
		System.err.println("Attenzione:");
		System.err.println(message);
	}
}

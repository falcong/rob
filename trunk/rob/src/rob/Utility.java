package rob;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class Utility {
	/*
	 * stampa la matrice che riceve in ingresso bidimensionale
	 * x e y indicano come nominare le rughe e le colonne
	 * i e j indicano da quale riga e colonna, rispettivamente, partire
	 * numTab1	numero tab prima riga intestazione [2]
	 * numTab2 	numero tab fra elementi riga intestazione [3] 
	 * numTab3	numero tab inizio ogni riga matrice [2]
	 * numTab4	numero tab fra gli elementi di ogni riga della matrice [2]
	 */
	//t
	public static void printMatrix2D(double matrix[][], String x, String y,
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
	}
	
	/*
	 * come sopra ma matrix è di int
	 */
	//t
	public static void printMatrix2D(int matrix[][], String x, String y,
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
	}
	
	
	/*
	 * legge da config.txt il parametro specificato
	 * [restituisce null se il parametro non viene trovato]
	 * Se parameter è presente più volte viene restituito il primo valore.
	 * Il file non deve contenere linee vuote.
	 */
	public static String getConfigParameter(String parameter) throws Error {
		try{
			FileInputStream fstream = new FileInputStream("config.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			boolean found = false;
			String lineElements[] = null;
			String tag;
			String value = null;
			while((line = br.readLine()) != null	&&	!found){
				if (line.charAt(0)=='#')
					continue;
				lineElements = line.split("[\\s]*=[\\s]*");
				tag = lineElements[0];
				value = lineElements[1];

				
				if(tag.equalsIgnoreCase(parameter)){
					found = true;
				}else{
					value= null;
				}
			}
			return value;
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
			throw new Error(e.getMessage());
		}
	}
	
	/*
	 * Questo metodo clona una matrice.
	 * PRecondizione: presuppone sempre che la riga e la colonna 0 siano inutilizzate e riempite di zeri.
	 */
	public static int[][] cloneMatrix(int [][] matrix){
		int[][] newMatrix = new int[matrix.length][];
		newMatrix[0]=new int[matrix[0].length];
		Arrays.fill(newMatrix[0], 0);
		for (int i=1; i<matrix.length;i++) {
			newMatrix[i]=new int[matrix[i].length];
			for(int j=1;j< matrix[i].length;j++) {
				newMatrix[i][j]=matrix[i][j];
			}
		}
		return newMatrix;
		}

	public static int[] cloneArray(int[] array) {
		int[] newArray = new int[array.length];
		for (int i=0; i<array.length;i++) {
			newArray[i]=array[i];
		}
		return newArray;
	}
	
	
	/*
	 * apre un file su cui scrivere e restituisce il Print Stream corrispondente
	 */
	public static PrintStream openOutFile(String fileName, boolean append){
	    try{
	    	FileOutputStream fileOutputStream = new FileOutputStream(fileName, append);
	    	return new PrintStream(fileOutputStream);
	    }catch (IOException e) {
	    	throw new Error(e);
	    }
	}
	
	
	/*
	 * scrive info in modalità append in outFile
	 */
	public static void write(String outFile, String info){
		PrintStream out = openOutFile(outFile, true);
		out.print(info);
		out.close();
	}
	
}

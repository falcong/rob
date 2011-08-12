package io;

import java.io.BufferedReader;
import java.util.Arrays;
import data.Problem;
import data.Solution;

public class SolutionParser extends Parser {
	private Problem problem;
	
	/**
	 * Crea un parser in grado di leggere un file contenente la descrizione di una soluzione e di restituire 
	 * l'oggetto corrispondente.
	 * path indica la posizione (percorso assoluto) della cartella contenente il file di input (che viene specificato
	 * con il parametro inputFile del metodo parse).
	 */
	public SolutionParser(String path, Problem problem) {
		super(path);
		this.problem = problem;
	}
	
	/**
	 * Effettua il parsing del file di nome inputFile, crea e restituisce la Solution descritta in tale file.
	 */
	@Override
	public Solution parse(String file) throws Exception {
		int numSuppliers = problem.getDimension();
		int numProducts = problem.getNumProducts();
		int solutionMatrix[][] = new int[numSuppliers+1][numProducts+1];
		//non uso la riga 0
		Arrays.fill(solutionMatrix[0], INT_NOT_USED);
		
		BufferedReader bufferedReader = Io.openInFile(path+file);
		String line;
		int supplier=1;
		while ((line = bufferedReader.readLine()) != null){
			String [] lineArray=line.split("\t");
			solutionMatrix[supplier][0] = INT_NOT_USED;
			for (int i=1;i<=numProducts;i++){
				solutionMatrix[supplier][i]= Integer.parseInt(lineArray[i-1]);
			}
			supplier++;
		}
		bufferedReader.close();
		
		return new Solution(solutionMatrix, problem);
	}
}

package io;

import java.io.BufferedReader;
import java.util.Arrays;
import data.Problem;
import data.Solution;

public class SolutionParser extends Parser {
	private Problem problem;

	public SolutionParser(Problem problem){
		this.problem = problem;
	}
	
	/*
	 * Esegue il parsing di una soluzione da un file (file = nome completo [incluso il path])
	 * Precondizione: i dati devono essere nel formato usato dal metodo export(), con i valori separati da \t
	 */
	@Override
	public Solution parse(String file) throws Exception {
		int numSuppliers = problem.getDimension();
		int numProducts = problem.getNumProducts();
		int solutionMatrix[][] = new int[numSuppliers+1][numProducts+1];
		//non uso la riga 0
		Arrays.fill(solutionMatrix[0], INT_NOT_USED);
		
		BufferedReader bufferedReader = Io.openInFile(file);
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

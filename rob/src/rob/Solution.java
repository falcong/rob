package rob;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

import rob.Utility;

public class Solution {
	/*
	 * Matrice di dimensione (numSuppliers+1)*(numProducts+1) [poichè la riga 0-esima 
	 * e la colonna 0-esima non sono utilizzate], indica le quantità di prodotto 
	 * acquistate presso ogni fornitore;
	 * Esempio:
     * solution[i][k]=x significa che x unità del prodotto k sono acquistate presso 
     * il fornitore i;
	 */
	private int[][] solutionMatrix;
	private double objectiveFunction;
	private int numSuppliers;
	private int numProducts;
	
	//t
	public Solution(int[][] solution, Problem problem) {
		solutionMatrix=solution;
		numSuppliers = solution.length-1;
		numProducts = solution[1].length-1;
		objectiveFunction=calcObjectiveFunction(problem);
	}
	
	/*
	 * Costruttore overloaded che restituisce una copia dell'oggetto Solution in ingresso
	 */
	//ta
	public Solution(Solution sol) {
		numSuppliers=sol.getNumSuppliers();
		numProducts=sol.getNumProducts();
		solutionMatrix=Utility.cloneMatrix(sol.getSolutionMatrix());
		objectiveFunction=sol.getObjectiveFunction();
	}
	
	
	//t
	public int getQuantity(int supplier, int product){
		return getSolutionMatrix()[supplier][product];
	}
	
	/*
	 * file = nome completo [incluso il path]
	 */
	public Solution(String file,Problem problem) {
		numSuppliers = problem.getDimension();
		numProducts = problem.getNumProducts();
		solutionMatrix = new int[numSuppliers+1][numProducts+1];
		Arrays.fill(solutionMatrix[0], 0);
		try {
			FileInputStream fstream = new FileInputStream(file);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			int supplier=1;
			while ((line = br.readLine()) != null){
				String [] lineArray=line.split("\t");
				solutionMatrix[supplier][0]=0;
				for (int i=1;i<=numProducts;i++){
					solutionMatrix[supplier][i]= Integer.parseInt(lineArray[i-1]);
				}
				supplier++;
			}
		} catch (IOException e) {
			throw new Error("Errore: " + e.getMessage());
		}
		objectiveFunction=calcObjectiveFunction(problem);
	}
	
public void print(){
		System.out.println("Soluzione: (matrice fornitori*prodotti):");
		Utility.printMatrix2D(getSolutionMatrix(), "f", "p", 1, 1, 2, 2, 2, 2);
		System.out.println("");
		
		System.out.println("funzione obiettivo = "+this.getObjectiveFunction());
	}

	public void setSolution(int[][] solution) {
		this.solutionMatrix = solution;
	}

	public int[][] getSolutionMatrix() {
		return solutionMatrix;
	}

	public void setObjectiveFunction(double objectiveFunction) {
		this.objectiveFunction = objectiveFunction;
	}

	public double getObjectiveFunction() {
		return objectiveFunction;
	}
	
	public boolean isAdmissible(Problem problem) {
		int [] demand=problem.getDemand();
		
		/*
		 * controllo che la riga e la colonna 0-esime di solutionMatrix contengano 0 e che tutti gli altri
		 * elementi siano >= 0
		 */
		for(int s=0; s<=numSuppliers; s++){
			for(int p=0; p<=numProducts; p++){
				if(s==0 || p==0){
					//riga 0-esima e colonna 0-esima devono contenere 0
					if(solutionMatrix[s][p]!=0){
						return false;
					}
				}else{
					//gli altri elementi devono essere >=0
					if(solutionMatrix[s][p]<0){
						return false;
					}
				}
			}
		}
		
		/*
		 * per ogni prodotto sommo tutto quello che compro per ogni supplier
		 * la somma dovrebbe essere pari alla domanda per il prodotto dato
		 */
		Supplier [] suppliers = problem.getSuppliers();
		for(int prod=1; prod<=problem.getNumProducts();prod++) {
			int sum=0;
			for (int supId=1; supId<=problem.getDimension();supId++) {
				// Controllo vincoli di disponibilità: se compro più di quello che il fornitore mi offre ritorno falso
				int availability=suppliers[supId].getAvailability(prod);
				
				if(availability>=0 && availability<solutionMatrix[supId][prod])
					return false;
				else if (availability<0 && solutionMatrix[supId][prod]>0)
					return false;
				sum+=solutionMatrix[supId][prod];
			}
			//se la quantità del prodotto che ho comprato è diversa dalla domanda ritorno falso 
			if(sum!=demand[prod])
				return false;
		}
		//Tutto a posto
		return true;
	}
	
	private double calcObjectiveFunction(Problem problem) {
		double value=0;
		for (int supp=1;supp<=problem.getDimension();supp++) {
			int total=0;
			for (int prod=1;prod<=problem.getNumProducts();prod++){
				//calcolo del totale
				total=total+solutionMatrix[supp][prod];
			}
			for (int prod=1;prod<=problem.getNumProducts();prod++){
				//somma dei costi
				double price=problem.getSuppliers()[supp].getDiscountedPrice(prod, total);
				if (price==-1)
					continue;
				value+=solutionMatrix[supp][prod]*price;
			}	
		}
		return value;
	}

	public void setNumProducts(int numProducts) {
		this.numProducts = numProducts;
	}

	public int getNumProducts() {
		return numProducts;
	}

	public void setNumSuppliers(int numSuppliers) {
		this.numSuppliers = numSuppliers;
	}

	public int getNumSuppliers() {
		return numSuppliers;
	}
	
	/*
	 * Questo metodo sposta una certa quantità di prodotto da una cella all'altra. NB: NON fa controlli di 
	 * consistenza e ammissibilità, essi sono a carico del chiamante (volendo, si potrebbe volere un'esplorazione
	 * dell'intorno che va in regione di non ammissibilità, è il chiamante a doversene occupare)
	 */
	public void moveQuantity(int productId, int fromSupplierId, int toSupplierId, int quantity, Problem problem) {
		solutionMatrix[fromSupplierId][productId]-=quantity;
		solutionMatrix[toSupplierId][productId]+=quantity;
		objectiveFunction=calcObjectiveFunction(problem);
	}

	/*
	 * Calcola la distanza tra se stessa e un'altra soluzione data
	 */
	public int calcDistance(Solution s2) {
		int sum=0;
		for(int supId=1;supId<=numSuppliers;supId++) {
			for(int prod=1;prod<=numProducts;prod++){
				sum+=Math.abs(getSolutionMatrix()[supId][prod]-s2.getSolutionMatrix()[supId][prod]);
			}
		}
		return sum/2;
	}
  

	/*
	 * scrive sul file fileName la soluzione
	 */
	public void export(String filePath) {

		PrintStream output = null;
	    try {
	       FileOutputStream file = new FileOutputStream(filePath);
	       output = new PrintStream(file);
	    } catch (IOException e) {
	       System.out.println("Errore: " + e);
	       System.exit(1);
	     }
		
		for(int i=1; i<=numSuppliers; i++){
      	for(int k=1; k<=numProducts; k++){
      		output.print(solutionMatrix[i][k]+"\t");
      	}
      	output.println();
      }
		
		output.close();
	}
	
	/*
	 * restituisce la quantità totale di prodotti comprati presso supplier
	 */
	public int totalQuantityBought(int supplier){
		int sum = 0;
		for(int p=1; p<=numProducts; p++){
			sum += solutionMatrix[supplier][p];
		}
		
		return sum;
	}
}
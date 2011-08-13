package data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

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
		solutionMatrix=cloneMatrix(sol.getSolutionMatrix());
		objectiveFunction=sol.getObjectiveFunction();
	}
	
	
	public int getQuantity(int supplier, int product){
		return getSolutionMatrix()[supplier][product];
	}
	
	
	public void print(){
		System.out.println("Soluzione: (matrice fornitori*prodotti):");
		
		//ricavo una matrice di double contenente gli stessi valori di solutionMatrix
		double tempMatrix[][] = new double[solutionMatrix.length][solutionMatrix[0].length];
		for(int i = 0; i<solutionMatrix.length; i++){
			for(int j = 0; j<solutionMatrix[1].length; j++){
				tempMatrix[i][j] = solutionMatrix[i][j];
			}
		}
		DataPrinter.printMatrix2D(tempMatrix, "f", "p", 1, 1, 2, 2, 2, 2);
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
	
	/*
	 * Questo metodo clona una matrice.
	 * PRecondizione: presuppone sempre che la riga e la colonna 0 siano inutilizzate e riempite di zeri.
	 */
	private int[][] cloneMatrix(int [][] matrix){
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
	
	public int getSupplierFromCell (int cell) {
		return (cell-getProductFromCell(cell))/numProducts+1;
	}
	
	public int getProductFromCell (int cell) {
		return (cell-1)%numProducts+1;
	}
	
	public int getCell(int supplier,int product){
		return (supplier-1)*numProducts+product;
	}
	
	public int getQuantity(int cell){
		return getQuantity(getSupplierFromCell(cell), getProductFromCell(cell));
	}

	/**
	 * Questo metodo verifica se è possibile decrementare il valore della cella {@code cell} della
	 * soluzione {@code solution} di almeno un'unità, senza spostare gli acquisti nelle celle
	 * proibite contenute in {@code forbiddenCells}. 
	 * Precondizione: cell contiene almeno 1 prodotto acquistato.
	 * <ol> 
	 * <li>C 				= insieme di tutte le celle della stessa colonna di {@code cell}, esclusa {@code cell} stessa</li>
	 * <li>C_vietate		= intersezione(C; {@code forbiddenCells})</li>
	 * <li>C_riempibili		= C\C_vietate</li>
	 * <li>Q_tot_vietato	= quantità totale di prodotti comprati presso le C_vietate</li>
	 * <li>D_effettiva		= (disponibilità residua totale delle C_riempibili)-Q_tot_vietato</li>
	 * </ol>
	 * Per decrementare il valore contenuto nella cella {@code cell} si può utilizzare solamente D_effettiva.
	 * 
	 * @param cell
	 * @param problem TODO
	 * @param forbiddenCells
	 * @return {@code true} se almeno 1 prodotto acquistato in cell è spostabile presso un altro fornitore;<br>
	 * {@code false} altrimenti.
	 */
	public boolean cellValueIsDecrementable(int cell, Problem problem, ArrayList<Integer> forbiddenCells){
		int product=getProductFromCell(cell);
		//somma disponibilità
		int sumResidualAvailability=0;
		//ciclo sui fornitori, mantenendo fisso il prodotto
		for (int supplierId=1;supplierId<=problem.getDimension();supplierId++){
			int targetCell = getCell(supplierId,product);
			if (targetCell==cell) // la cella è la stessa che sto valutando
				continue;
			/*
			 * Se la cella è tra quelle proibite, sottraggo dalla disponibilità
			 * residua i prodotti acquistati da quella cella, perché presuppongo
			 * che tali celle dovranno essere svuotate a loro volta. 
			 */
			if (forbiddenCells.contains(targetCell)) 
				sumResidualAvailability-=getQuantity(supplierId, product);
			else //La cella può accogliere nuovi acquisti, aumento la disponibilità totale.
				sumResidualAvailability+=problem.getSupplier(supplierId).getResidual(product, this);
		}
		if (sumResidualAvailability>0)
			return true;
		else 
			return false;
	}
}
package solutionhandlers;

import java.util.HashSet;

import rob.Problem;
import rob.Solution;
import rob.Supplier;


public class EmptyCellsRandomNeighbourGenerator extends EmptyCellsNeighbourGenerator{
	protected Problem problem;
   private int numSuppliers;
	private int numProducts;
	int totalCells;
	double ratio;
	
	public EmptyCellsRandomNeighbourGenerator(Problem problem) {
		super(problem);
		this.problem=problem;
		this.numSuppliers = problem.getDimension();
		this.numProducts = problem.getNumProducts();
		dontAdd = new HashSet<Integer>();
		totalCells=numSuppliers*numProducts;
		dropped=new HashSet<Integer>();
		this.ratio = 0;
	}
	
	public EmptyCellsRandomNeighbourGenerator(Problem problem, double ratio) {
		super(problem);
		this.problem=problem;
		this.numSuppliers = problem.getDimension();
		this.numProducts = problem.getNumProducts();
		dontAdd = new HashSet<Integer>();
		totalCells=numSuppliers*numProducts;
		dropped=new HashSet<Integer>();
		this.ratio = ratio;
	}
	
	
	protected void empty(Solution sol, int cell){
		int supplier = problem.getSupplierFromCell(cell);
		int product = problem.getProductFromCell(cell);
		
		//fornitori ordinati in base al prezzo crescente
		Supplier orderedSuppliers[] = problem.sortByCurrentPrice(product, sol);
		//altero casualmente l'ordine dei primi fornitori
		orderedSuppliers = scramble(orderedSuppliers, ratio);
		
		
		int quantity = sol.getQuantity(supplier, product); 
			
		for(int i=1; i<=numSuppliers && quantity>0; i++){
			Supplier receivingSupplier = orderedSuppliers[i];
			int recSup = receivingSupplier.getId();
			int residualAvailability = problem.getSupplier(recSup).getResidual(product, sol);
			int recCell = problem.getCell(recSup, product);
			
			if(recSup!=supplier && !cellsToEmpty.contains(recCell) && residualAvailability>0){
				int quantityToMove = Math.min(quantity, residualAvailability);
				sol.moveQuantity(product, supplier, recSup, quantityToMove, problem);
				quantity -= quantityToMove; 
			}
		}		
	}
	
	/*
	 * altera l'ordine in maniera casuale dei primi ratio*array.length elementi
	 */
	public Supplier[] scramble(Supplier array[], double ratio){
		//numero elementi da "mischiare"
		int elementsToMove = (int)((array.length-1)*ratio);
		
		Supplier result[] = new Supplier[array.length];
		
		//result[0] non usato
		result[0] = null;
		
		HashSet<Integer> alreadyChosen = new HashSet<Integer>();
		//"mischio" i primi elementsToMove elementi
		int randomSup;
		for(int i=1; i<=elementsToMove; i++){
			do{
				randomSup = (int)(Math.random()*elementsToMove+1);
			}while(alreadyChosen.contains(randomSup));
			
			result[i] = array[randomSup];
		}
		
		//i restanti supllier li mantengo nello stesso ordine 
		for(int i=elementsToMove+1; i<=array.length-1; i++){
			result[i] = array[i];
		}
		
		return result;
	}
	
	public void setRatio(double ratio){
		this.ratio = ratio;
	}
}
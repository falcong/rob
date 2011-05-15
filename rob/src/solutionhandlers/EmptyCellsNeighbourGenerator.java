package solutionhandlers;

import java.util.HashSet;

import rob.Problem;
import rob.Solution;
import rob.Supplier;


public class EmptyCellsNeighbourGenerator extends NeighbourGenerator{
	protected Problem problem;
   private int numSuppliers;
	private int numProducts;
	int totalCells;

	HashSet<Integer> dontAdd;
	HashSet<Integer> dropped;
	
	public EmptyCellsNeighbourGenerator(Problem problem) {
		this.problem=problem;
		this.numSuppliers = problem.getDimension();
		this.numProducts = problem.getNumProducts();
		dontAdd = new HashSet<Integer>();
		totalCells=numSuppliers*numProducts;
		dropped=new HashSet<Integer>();
	}
	
	@Override
	public Solution generate(Solution solution, int distance){
		//copia di solution da modificare
		Solution result=new Solution(solution);

		
		
		
		int [] cellsToEmpty= new int[distance];
		
		for (int i=0;i<distance;i++){
			if(blacklistsFull()) {
				dontAdd.clear();
				dropped.clear();
				return result;
			}
				
			cellsToEmpty[i]=chooseCell();
		}
		
		int move=0;
		while (move<distance){
			if(blacklistsFull())
				break;
			boolean moved = empty(cellsToEmpty[move],result,dontAdd);
			if (moved)
				move++; //passo alla mossa successiva
			else {//sostituisco la cella con un'altra
				dontAdd.remove(cellsToEmpty[move]);
				dropped.add(cellsToEmpty[move]);
				cellsToEmpty[move]=chooseCell();
				}
		}
		dropped.clear();
		dontAdd.clear();
		return result;
		/*
		 * nota: sostituire una cella in un'iterazione che non è la prima rende possibile che tale cella sia stata riempita 
		 * in un'iterazione precedente. 
		 */
		//TODO tenere traccia delle celle di destinazione degli spostamenti e impedire che ciò si verifichi??
	}

	private boolean blacklistsFull() {
		if (dropped.size()+dontAdd.size()==totalCells){
			System.err.println("[EmptyCellsNG] Warning: non è stato possibile terminare tutti gli spostamenti richiesti.");
			return true;
			} else
				return false;
	}

	private int chooseCell() {
		int cell;
		do {
			cell=1+(int)(Math.random()*totalCells);			
		}while(dontAdd.contains(cell) || dropped.contains(cell));
		dontAdd.add(cell);
		return cell;
	}
	
	
	/*
	 * Cerca di svuotare la cella del tutto o almeno in parte (se le disponibilità degli altri fornitori non consente di svuotare tutto).
	 * Se non riesce a spostare niente ritorna false al chiamante.
	 */
	private boolean empty(int cell, Solution solution, HashSet<Integer> dontAdd) {
		//variabile che viene impostata a true qualora sia possibile effettuare uno spostamento
		boolean success=false;
		int product = (cell-1)%numProducts+1;
		int supplierToEmpty=(cell-product)/numProducts+1;
		
		
		int startSupplier=1+(int)(Math.random()*numSuppliers);
		
		int s=startSupplier;
		
		Supplier supToEmpty = problem.getSupplier(supplierToEmpty);
		//condizione di terminazione è che il residuo sia pari alla disponibilità (cella vuota) oppure che s==startSupplier (fine giro)
		do {
			int residual = problem.getSupplier(s).getResidual(product, solution);
			/*
			 * Passa subito al supplier successivo se
			 * 1) x=(s-1)*numProducts+product (numero univoco della cella di destinazione) è contenuto in dontAdd.
			 * 2) il residuo di s per il prodotto da spostare è 0
			 */
			if(dontAdd.contains((s-1)*numProducts+product) || residual<=0) {
				s=s%numSuppliers+1;
				continue;
			}
			
			// sposta il minimo tra la quantità contenuta nella cella da svuotare e il residuo della cella da riempire
			int quantity= Math.min(residual,solution.getQuantity(supplierToEmpty, product));
			solution.moveQuantity(product, supplierToEmpty, s, quantity, problem);
			//dato che è riuscito a spostare qualcosa, imposta success a true
			success=true;
			//incremento
			s=s%numSuppliers+1;
		} while(supToEmpty.getAvailability(product)-supToEmpty.getResidual(product, solution)>0 && s!=startSupplier);
		return success;
	}

	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
		this.numSuppliers = problem.getDimension();
		this.numProducts = problem.getNumProducts();
	}
}

package solutionhandlers;

import java.util.Arrays;
import java.util.HashSet;

import rob.Problem;
import rob.Solution;
import rob.Supplier;
import rob.Utility;

public class LinesSolutionGenerator extends SolutionGenerator {

	int suppliersNumber;
	int productsNumber;
	int solution [][];
	int [] residualDemand;
	                
	public LinesSolutionGenerator (Problem problem) {
		super(problem);
		suppliersNumber = problem.getDimension();
		productsNumber = problem.getNumProducts();
		residualDemand=Utility.cloneArray(problem.getDemand());
	}
	
	public LinesSolutionGenerator () {
	}
	
	@Override
	public void setProblem(Problem problem){
		this.problem=problem;
		suppliersNumber = problem.getDimension();
		productsNumber = problem.getNumProducts();
		residualDemand=Utility.cloneArray(problem.getDemand());
	}

	@Override
	public Solution generate() {
		HashSet<Integer> chosen=new HashSet<Integer>();
		
		solution = new int [suppliersNumber+1][productsNumber+1];
		for (int supId = 0; supId <= suppliersNumber; supId++) {
			Arrays.fill(solution[supId], 0);
		}
		
		while(!isZero(residualDemand)) {
			/*
			 * creo varie soluzioni temporanee che hanno una riga (fornitore) satura
			 * (considerando la domanda residua e la disponibilità)
			 * tra queste tengo traccia di quella con la f.o. migliore
			 */
			int bestSupplierId=0;
			double bestObjFunct=Double.MAX_VALUE;

			for(int sup=1;sup<=suppliersNumber;sup++) {
				if(chosen.contains(sup))
					continue; //l'ho già scelto, passo oltre
				Solution temp=createSolutionWithLine(sup,chosen);
				double tempObj= temp.getObjectiveFunction();
				if(tempObj<bestObjFunct) {
					bestSupplierId=sup;
					bestObjFunct=tempObj;
				}
			}
			
			/*
			 * nella soluzione definitiva compro tutto quello che posso dal fornitore
			 * considerato migliore nel passo precedente
			 */
			
			makeLine(bestSupplierId,solution,residualDemand);
			chosen.add(bestSupplierId); //l'ho già scelto e comprato tutto il comprabile, quindi non lo visito più
		}
		return new Solution(solution,problem);
	}

	private Solution createSolutionWithLine(int sup,HashSet<Integer> alreadyChosen) {
		//compro tutto quello che posso dal fornitore sup
		int [][] solutionMatrix = new int[suppliersNumber+1][productsNumber+1];
		for (int supId = 0; supId <= suppliersNumber; supId++) {
			Arrays.fill(solutionMatrix[supId], 0);
		}
		int[] tempResidualDemand=Utility.cloneArray(residualDemand);
		makeLine(sup, solutionMatrix, tempResidualDemand);
		//riempio il resto della domanda con lo stesso principio di Trivial
		for (int p=1; p<=productsNumber;p++){
			HashSet<Integer> exclude=new HashSet<Integer>();
			exclude.addAll(alreadyChosen); //non uso le righe di fornitori già presi in soluzione definitiva
			exclude.add(sup); //sup è saturo quindi va sempre escluso
			while(tempResidualDemand[p]!=0){
				Supplier tempSupplier = findBestBasePrice(p,exclude);
				if(tempSupplier==null){
					throw new Error("Si è verificato un errore. Controlla l'ammissibilità dei dati.");
				}
				int tmpAvailability=tempSupplier.getAvailability(p);
				if (tmpAvailability==-1)
					tmpAvailability=0;
				int quantityToBuy = Math.min(tempResidualDemand[p], tmpAvailability);
				solutionMatrix[tempSupplier.getId()][p]=quantityToBuy;
				tempResidualDemand[p]-=quantityToBuy;
				exclude.add(tempSupplier.getId());
			}
						
		}
		
		return new Solution(solutionMatrix,problem);
	}

	
	/*
	 * aggiorna la matrice solutionMatrix e la residual demand (nb java passa il puntatore ai vettori)
	 */
	private void makeLine(int sup, int[][] solutionMatrix,int [] tempResidualDemand) {
		Supplier supplier=problem.getSupplier(sup);
		for(int p=1;p<=productsNumber;p++){
			if(tempResidualDemand[p]==0)
				continue;
			
			int availability=supplier.getAvailability(p);
			if (availability==-1)
				availability=0;
			
			
			int boughtQuantity=Math.min(tempResidualDemand[p],availability);
			solutionMatrix[sup][p]=boughtQuantity;
			tempResidualDemand[p]-=boughtQuantity;
		}
		return;
	}

	private boolean isZero(int[] residualDemand) {
		int sum=0;
		for (int i=1; i<=productsNumber;i++)
			sum+=residualDemand[i];
		
		if (sum==0)
			return true;
		else
			return false;
	}
	
	
	/*
	 * restituisce il fornitore con il prezzo base [fascia zero] di product più basso escludendo i fornitori contenuti in exclude
	 */
	private Supplier findBestBasePrice(int product,HashSet<Integer> exclude) {
		double bestprice = Double.MAX_VALUE;
		Supplier s = null;
		for(int i=1; i<=problem.getDimension(); i++){
			if(exclude.contains(i))
				continue;
			
			double price=problem.getSupplier(i).getBasePrice(product);
			if(price < bestprice && price>0){
				bestprice=price;
				s=problem.getSupplier(i);
			}
		}
		return s;
	}

}

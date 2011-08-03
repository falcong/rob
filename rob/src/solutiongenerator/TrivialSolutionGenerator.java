package solutiongenerator;

import java.util.Arrays;
import java.util.HashSet;

import data.Problem;
import data.Solution;
import data.Supplier;


public class TrivialSolutionGenerator extends SolutionGenerator {
	public TrivialSolutionGenerator(Problem problem) {
		super(problem);
	}
	
	// Costruttore che crea un generatore vuoto
	public TrivialSolutionGenerator() {
	}
	
	@Override
	public Solution generate() {
		
		int numProducts = problem.getNumProducts();
		int purchased[][]=new int[problem.getDimension()+1][numProducts+1];
		
		// Riempio la matrice purchased di zeri
		for (int supId = 0; supId <= problem.getDimension(); supId++) {
			Arrays.fill(purchased[supId], 0);
		}
		
		for (int p=1; p<=numProducts;p++){
			/*
			 * quantità comprata del prodotto p-esimo, mi fermo quando bought[p]=demand[p]
			 */
			int bought=0;
			HashSet<Integer> exclude=new HashSet<Integer>();
			do{
				Supplier sup = findBestBasePrice(p,exclude);
				if(sup==null){
					throw new Error("Si è verificato un errore. Controlla l'ammissibilità dei dati.");
				}
				int quantityToBuy = Math.min(problem.getDemand()[p]-bought, sup.getAvailability(p));
				purchased[sup.getId()][p]=quantityToBuy;
				bought+=quantityToBuy;
				exclude.add(sup.getId());
			} while(bought < problem.getDemand()[p]);			
		}
		return new Solution(purchased,problem);
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

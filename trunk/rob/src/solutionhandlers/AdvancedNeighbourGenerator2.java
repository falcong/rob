package solutionhandlers;

import java.util.HashSet;

import rob.Problem;
import rob.Solution;
import rob.Supplier;



public class AdvancedNeighbourGenerator2 extends NeighbourGenerator {
	protected Problem problem;
	protected Supplier[] suppliersList;
	public AdvancedNeighbourGenerator2(Problem problem) {
		this.problem=problem;
		suppliersList=problem.getSuppliers().clone();
	}
	
	/*
	 * Nella soluzione che riceve in ingresso individua un fornitore casuale lo fa passare
	 * alla successiva fascia di sconto (facendo in modo per quanto possibile che negli altri
	 * fornitori la fascia di sconto attiva non diminuisca)
	 * Restituisce la nuova soluzione trovata
	 * il parametro di ingresso distance non viene utilizzato
	 */
	@Override
	public Solution generate(Solution currentSolution, int distance){
		int numSuppliers=problem.getDimension();
		Solution solution = new Solution(currentSolution);
		int startSupplier = findTargetSupplier(solution);
		//Ciao
		boolean relaxed=false;
		boolean done=false;
		int s=startSupplier;
		while(!done) {
			done = moveQuantity(s, solution,relaxed);
			s = s%numSuppliers+1;
			if(s==startSupplier){
				//ho fatto il giro, rilasso le condizioni
				relaxed=true;
				System.err.println("[AdvNG2] Warning: rilasso le condizioni.");
			}
		}
		

		
		return solution;
	}

	
	/*
	 * trova un fornitore presso cui aumentare gli acquisti
	 */
	private int findTargetSupplier(Solution solution){
		int target = findSupplierImproveable(solution);
		if(target == 0){
			target = findSupplierNotSaturated(solution);
			if(target == 0){
				throw new Error("Situazione anomala: tutti i fornitori sono saturi.\n" +
						"Il programma verrà terminato.\n");
			}
		}
		
		return target;
	}
	
	
	public int findTargetSupplierPublic(Solution solution){
		return findTargetSupplier(solution);
	}
	
	
	/*
	 * trova un fornitore la cui fascia di sconto attiva sia aumentabile di 1
	 */
	private int findSupplierImproveable(Solution solution) {
		int numSuppliers = problem.getDimension();
		
		int startSupplier = (int)(Math.random()*numSuppliers+1);
		int supplier = startSupplier;
		
		boolean terminate = false;
		do{
			//activatedSegment =  fascia di sconto attiva in supplier1
			int totalQuantityBought = solution.totalQuantityBought(supplier);
			int activatedSegment = problem.getSupplier(supplier).activatedSegment(totalQuantityBought);
			//massima fascia di sconto attivabile in supplier1
			int maxSegmentActivable = problem.maxSegmentActivable(supplier);
			
			//controllo che in supplier1 sia possibile far aumentare di 1 la fascia di sconto attiva;
			if(activatedSegment<maxSegmentActivable){
				terminate = true;
			}else{
				if(supplier==numSuppliers){
					supplier=1;
				}else{
					supplier++;
				}
			}
		}while(!terminate && supplier!=startSupplier);
		
		if(!terminate){
			supplier = 0;
		}
		
		return supplier;
	}	

	
	public int findSupplierImproveablePublic(Solution solution){
		return findSupplierImproveable(solution);
	}
	
	
	private int findSupplierNotSaturated(Solution solution) {
		int numSuppliers = problem.getDimension();
		
		int startSupplier = (int)(Math.random()*numSuppliers+1);
		int supplier = startSupplier;
		
		boolean terminate = false;
		do{
			//disponibilità residua totale di tutti i prodotti
			int residual=problem.getSupplier(supplier).getTotalResidualAvailability(solution);	
			if (residual>0)
				terminate=true;
			else {
				if (supplier==numSuppliers)
					supplier=1;
				else
					supplier++;
			}
		}while(!terminate && supplier!=startSupplier);
		
		if(!terminate){
			supplier = 0;
		}
		
		return supplier;
	}
	
	
	public int findSupplierNotSaturatedPublic(Solution solution){
		return findSupplierNotSaturated(solution) ;
	}

	
	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
		suppliersList=problem.getSuppliers().clone();
	}
	
	/*
	 * modifica solution in maniera tale che in targetSupplier la fascia di sconto attiva aumenti di 1;
	 * se targetSupplier si trova già nella fascia massima allora esso viene saturato
	 */
	private boolean moveQuantity(int targetSupplier, Solution solution, boolean relaxed){
		int startSupplier = -1;
		do {
			startSupplier = (int)(Math.random()*problem.getDimension()+1);
			}while(startSupplier==targetSupplier);
		
		int supplier = startSupplier;
		int numProducts=problem.getNumProducts();
		int numSuppliers=problem.getDimension();
		/*
		 * quantità totale di prodotti acquistati che è necessario spostare dagli altri fornitori
		 * a targetSupplier per far aumentare la fascia di sconto attiva di 1 in targetSupplier
		 */
		int quantityToMove = problem.getSupplier(targetSupplier).quantityToIncreaseSegment(solution);
		int startProduct=(int)(Math.random()*problem.getNumProducts()+1);
		int g=0;
		while(quantityToMove>0){
			int maxMoveableTotalQuantity;
			if (!relaxed)
				maxMoveableTotalQuantity=problem.getSupplier(supplier).quantityToNotDecreaseSegment(solution);
			else
				maxMoveableTotalQuantity=0;
			
			int p= startProduct;
			//ciclo sui prodotti
			while((maxMoveableTotalQuantity>0 || relaxed) && quantityToMove>0) {
				int quantity;
				if (!relaxed) {
					quantity = Math.min(
							Math.min(quantityToMove, maxMoveableTotalQuantity), 
							Math.min(problem.getSupplier(targetSupplier).getResidual(p, solution), solution.getQuantity(supplier, p)));
				} else {
					quantity = Math.min(
							quantityToMove, 
							Math.min(problem.getSupplier(targetSupplier).getResidual(p, solution), solution.getQuantity(supplier, p)));
				}
				
				//Aggiornamenti
				solution.moveQuantity(p, supplier, targetSupplier, quantity, problem);
						
				quantityToMove-=quantity;
				if(!relaxed){
					maxMoveableTotalQuantity-=quantity;
				}
				
				p=p%numProducts+1;
				
				//fine ciclo
				if(p==startProduct)
					break;					
			}
			
			supplier = supplier%numSuppliers+1;
			if(supplier==targetSupplier){
				supplier = supplier%numSuppliers+1;
			}
			g++;
			if (supplier == startSupplier && !relaxed && quantityToMove>0)
				return false;
			else if (supplier == startSupplier && relaxed && quantityToMove>0)
				return true;
			
		}
		System.out.println(g);
		return true;
	}
	
}
//modifica claudio
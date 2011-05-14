package solutionhandlers;

import java.util.HashSet;

import rob.Problem;
import rob.Solution;
import rob.Supplier;

public class AdvancedNeighbourGenerator extends NeighbourGenerator{
	protected Problem problem;
	public AdvancedNeighbourGenerator(Problem problem) {
		this.problem=problem;
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
		//System.out.println("x");
		Solution solution = new Solution(currentSolution);
		
		//supplier1 = fornitore a cui far aumentare di 1 la fascia di sconto attiva
		int supplier1 = findTargetSupplier(solution);
		
		moveQuantity(supplier1, solution);
		
		return solution;
	}

	
	/*
	 * trova il fornitore presso cui aumentare gli acquisti
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
	}
	
	/*
	 * modifica solution in maniera tale che in targetSupplier la fascia di sconto attiva aumenti di 1;
	 * se targetSupplier si trova già nella fascia massima allora esso viene saturato
	 */
	private void moveQuantity(int targetSupplier, Solution solution){
		boolean ok = false;
		int startSupplier = -1;
		while(!ok){
			startSupplier = (int)(Math.random()*problem.getDimension()+1);
			if(startSupplier!=targetSupplier){
				ok = true;
			}
		}
		
		int supplier = startSupplier;
		int numProducts=problem.getNumProducts();
		int numSuppliers=problem.getDimension();
		/*
		 * quantità totale di prodotti acquistati che è necessario spostare dagli altri fornitori
		 * a targetSupplier per far aumentare la fascia di sconto attiva di 1 in targetSupplier
		 */
		int quantityToMove = problem.getSupplier(targetSupplier).quantityToIncreaseSegment(solution);
		int startProduct=(int)(Math.random()*problem.getNumProducts()+1);

		boolean relaxed = false;
		
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
				
				if(p==numProducts)
					p=1;
				else
					p++;
				
				//fine ciclo
				if(p==startProduct)
					break;					
			}
			
			supplier = supplier%numSuppliers+1;
			if(supplier==targetSupplier){
				supplier = supplier%numSuppliers+1;
			}
			
			if (supplier == startSupplier  && relaxed && quantityToMove>0){
				//throw new Error("Errore: non è possibile effettuare lo spostamento richiesto.");
				System.err.println("Attenzione: non sono riuscito a saturare il fornitore target.");
				break;
			}
			else if (supplier == startSupplier)
				relaxed=true;
		}
/*		fornitore iniziale = rand
		prodottoiniziale = rand
		rilassati = false
		
		s = fornitore iniziale
		while(quantityToMove>0){
			max_q_spostabTot          [if(rilassati)max_q_spostabTot=0]
			p = primo prodotto
			while((max_q_spostTot>0 || rilassati)&& quantityToMove>0){
				sposto max spostabile da s,p a targetS,p ---> min(quantityToMove, disp_target_s, q_comprata_s, max_q_spostabTot[solo se !rilassati])
				aggiorno max_q_spostTot(solo se !rilassati), quantityToMove
				p++
				if(p==primo prodotto)
					break
			}
			s++
			if(s==primo fornitore && rilassati)
				errore
			if(s==primo fornitore)
				rilassati = true
		}*/}
}

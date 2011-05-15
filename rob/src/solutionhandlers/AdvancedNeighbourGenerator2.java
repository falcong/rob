package solutionhandlers;

import java.util.Arrays;

import rob.Problem;
import rob.Solution;
import rob.Supplier;



public class AdvancedNeighbourGenerator2 extends NeighbourGenerator {
	protected Problem problem;
	//protected Supplier[] suppliersList;
	public AdvancedNeighbourGenerator2(Problem problem) {
		this.problem=problem;
		//suppliersList=problem.getSuppliers().clone();
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
		//orderSuppliersByGapToNextSegment(currentSolution);
		Solution solution=null;
		boolean relaxed=false;
		boolean done=false;
		
		int starttS=(int)(Math.random()*problem.getDimension()+1);
		int tS=starttS;
		//for(int tS=1;tS<=numSuppliers && !done;tS++){
		while (tS>0 && !done) {
			//resetto la soluzione perché il target è cambiato
			solution = new Solution(currentSolution);
			//done = moveQuantity(suppliersList[tS].getId(), solution,relaxed);
			//
			done = moveQuantity(tS, solution,relaxed);
			tS=cyclicIncrement(numSuppliers, starttS,tS);
			//
			//if(tS==numSuppliers && !done){
			if(tS<0 && !done){
				//ho fatto il giro, rilasso le condizioni
				relaxed=true;
				//resetto s (a zero, perchè il fine ciclo mi riporterà a 1)
				//tS=0;
				tS=starttS;
				System.err.println("[AdvNG2] Warning: rilasso le condizioni.");
			}
		}
		return solution;
	}

	
//	private void orderSuppliersByGapToNextSegment(Solution solution) {
//		IncreaseSegmentComparator comparator=new IncreaseSegmentComparator(solution);
//		Arrays.sort(suppliersList, 1, suppliersList.length, comparator);
//	}

	
	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
		//suppliersList=problem.getSuppliers().clone();
	}
	
	/*
	 * modifica solution in maniera tale che in targetSupplier la fascia di sconto attiva aumenti di 1;
	 * se targetSupplier si trova già nella fascia massima allora esso viene saturato
	 */
	private boolean moveQuantity(int targetSupplier, Solution solution, boolean relaxed){
		
		int numProducts=problem.getNumProducts();
		int numSuppliers=problem.getDimension();
		/*
		 * quantità totale di prodotti acquistati che è necessario spostare dagli altri fornitori
		 * a targetSupplier per far aumentare la fascia di sconto attiva di 1 in targetSupplier
		 */
		int quantityToMove = problem.getSupplier(targetSupplier).quantityToIncreaseSegment(solution);
		/*
		 * prodotto da cui iniziare i trasferimenti: ogni ciclo di trasferimento inizia dallo stesso prodotto
		 */
		int startProduct=(int)(Math.random()*problem.getNumProducts()+1);
		
		int startSupplier = 0;
		do {
			startSupplier = (int)(Math.random()*problem.getDimension()+1);
			}while(startSupplier==targetSupplier);
		
		int s = startSupplier;
		
		//ciclo sui fornitori
		while(quantityToMove>0 && s>0){
			//check source!=target
			if (s==targetSupplier) {
				s = cyclicIncrement(numSuppliers, startSupplier, s);
				continue;
			}
				
			
			int maxMoveableTotalQuantity;
			if (!relaxed)
				maxMoveableTotalQuantity=problem.getSupplier(s).quantityToNotDecreaseSegment(solution);
			else
				maxMoveableTotalQuantity=0;
			
			//check quantità movimentabile da source > 0
			if (maxMoveableTotalQuantity==0) {
				s = cyclicIncrement(numSuppliers, startSupplier, s);
				continue;
			}
			
			int p=startProduct;
			/*
			 * ciclo sui prodotti - condizioni di terminazione:
			 * maxMoveableTotalQuantity=0 ove le condizioni NON sono rilassate
			 * quantityToMove=0 (ho fatto scattare la fascia di sconto)
			 * p>0 (ho fatto passare tutti i prodotti per il fornitore corrente - vedi metodo cyclicIncrement)
			 */
			while((maxMoveableTotalQuantity>0 || relaxed) && quantityToMove>0 && p>0) {
				int quantity;
				int targetResidual = problem.getSupplier(targetSupplier).getResidual(p, solution);
				int sourceQuantity = solution.getQuantity(s, p);
				if (targetResidual<=0 || sourceQuantity==0){
					//Il prodotto p non può essere spostato: incremento p e continuo
					p = cyclicIncrement(numProducts, startProduct, p);
					continue;
				}
				
				if (!relaxed) {
					quantity = Math.min(
							Math.min(quantityToMove, maxMoveableTotalQuantity), 
							Math.min(targetResidual, sourceQuantity));
				} else {
					quantity = Math.min(
							quantityToMove, 
							Math.min(targetResidual, sourceQuantity));
				}

				//Aggiornamenti
				solution.moveQuantity(p, s, targetSupplier, quantity, problem);
						
				quantityToMove-=quantity;
				if(!relaxed){
					maxMoveableTotalQuantity-=quantity;
				}
				
				p = cyclicIncrement(numProducts, startProduct, p);					
			}
			s=cyclicIncrement(numSuppliers, startSupplier, s);
		}
		
		if (quantityToMove>0 && !relaxed)
			return false;
		else
			return true;
	}

	
	/*
	 * Questo metodo incrementa l'indice i in modo ciclico dati il valore massimo dell'indice (totalItems) e il valore da cui è iniziato il ciclo (startItemIdx)
	 * Se dopo l'incremento il valore dell'indice è pari al valore di partenza, il metodo ritorna -1 per indicare che il ciclo è terminato.
	 */
	private int cyclicIncrement(int totalItems, int startItemIdx, int i) {
		i=i%totalItems+1;
		if(i==startItemIdx)
			return -1;
		return i;
	}

	/*
	 * trova un fornitore presso cui aumentare gli acquisti
	 */
//	private int findTargetSupplier(Solution solution){
//		int target = findSupplierImproveable(solution);
//		if(target == 0){
//			target = findSupplierNotSaturated(solution);
//			if(target == 0){
//				throw new Error("Situazione anomala: tutti i fornitori sono saturi.\n" +
//						"Il programma verrà terminato.\n");
//			}
//		}
//		
//		return target;
//	}
//	
//	
//	public int findTargetSupplierPublic(Solution solution){
//		return findTargetSupplier(solution);
//	}
//	
//	
//	/*
//	 * trova un fornitore la cui fascia di sconto attiva sia aumentabile di 1
//	 */
//	private int findSupplierImproveable(Solution solution) {
//		int numSuppliers = problem.getDimension();
//		
//		int startSupplier = (int)(Math.random()*numSuppliers+1);
//		int supplier = startSupplier;
//		
//		boolean terminate = false;
//		do{
//			//activatedSegment =  fascia di sconto attiva in supplier1
//			int totalQuantityBought = solution.totalQuantityBought(supplier);
//			int activatedSegment = problem.getSupplier(supplier).activatedSegment(totalQuantityBought);
//			//massima fascia di sconto attivabile in supplier1
//			int maxSegmentActivable = problem.maxSegmentActivable(supplier);
//			
//			//controllo che in supplier1 sia possibile far aumentare di 1 la fascia di sconto attiva;
//			if(activatedSegment<maxSegmentActivable){
//				terminate = true;
//			}else{
//				if(supplier==numSuppliers){
//					supplier=1;
//				}else{
//					supplier++;
//				}
//			}
//		}while(!terminate && supplier!=startSupplier);
//		
//		if(!terminate){
//			supplier = 0;
//		}
//		
//		return supplier;
//	}	

//	
//	public int findSupplierImproveablePublic(Solution solution){
//		return findSupplierImproveable(solution);
//	}
	
	
//	private int findSupplierNotSaturated(Solution solution) {
//		int numSuppliers = problem.getDimension();
//		
//		int startSupplier = (int)(Math.random()*numSuppliers+1);
//		int supplier = startSupplier;
//		
//		boolean terminate = false;
//		do{
//			//disponibilità residua totale di tutti i prodotti
//			int residual=problem.getSupplier(supplier).getTotalResidualAvailability(solution);	
//			if (residual>0)
//				terminate=true;
//			else {
//				if (supplier==numSuppliers)
//					supplier=1;
//				else
//					supplier++;
//			}
//		}while(!terminate && supplier!=startSupplier);
//		
//		if(!terminate){
//			supplier = 0;
//		}
//		
//		return supplier;
//	}
//	
//	
//	public int findSupplierNotSaturatedPublic(Solution solution){
//		return findSupplierNotSaturated(solution) ;
//	}

}

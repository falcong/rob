/*
 * questa classe è utilizzata per generare un vicino nella Local Search
 */
package neighbourgenerator;

import data.Problem;
import data.Solution;


public class AdvancedNeighbourGenerator extends NeighbourGenerator {
	protected Problem problem;
	
	
	public AdvancedNeighbourGenerator(Problem problem) {
		this.problem=problem;
	}
	
	/*
	 * Nella soluzione che riceve in ingresso individua un fornitore casuale lo fa passare
	 * alla successiva fascia di sconto (facendo in modo per quanto possibile che negli altri
	 * fornitori la fascia di sconto attiva non diminuisca)
	 * Restituisce la nuova soluzione trovata
	 */
	@Override
	public Solution generate(Solution currentSolution){
		int numSuppliers=problem.getDimension();
		//orderSuppliersByGapToNextSegment(currentSolution);
		Solution solution=null;
		boolean relaxed=false;
		boolean done=false;
		
		int starttS=(int)(Math.random()*problem.getDimension()+1);
		int tS=starttS;
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
		int quantityToMove = problem.getSupplier(targetSupplier).quantityToNextSegment(solution);
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
				maxMoveableTotalQuantity=problem.getSupplier(s).quantityToPreviousSegment(solution);
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
				solution.moveQuantity(p, s, targetSupplier, quantity);
						
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
}

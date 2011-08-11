/*
 * pesca un numero distance di fornitori casuali da bandire(presso cui quantità totale comprata >= 1)
 * ogni fornitore viene svuotato così:
 * scarico tutti i prodotti in un fornitore casuale, se non sufficiente continuo con i successivi
 */

package neighbourgenerator;

import java.util.HashSet;

import data.Problem;
import data.Solution;


public class BanSupplierNeighbourGenerator extends NeighbourGenerator implements DistancedNeighbourGenerator{
	protected Problem problem;
	public BanSupplierNeighbourGenerator(Problem problem) {
		this.problem=problem;
	}
	
	public Solution generate(Solution solution){
		final int DISTANCE = 1;
		return generate(solution, DISTANCE);
	}
	
	@Override
	public Solution generate(Solution solution, int distance){
		//copia di solution da modificare
		Solution result=new Solution(solution);
		
		//Costruisco una lista di distance fornitori casuali che voglio "bandire"
		HashSet<Integer> banned = new HashSet<Integer>();
		int [] banArray=new int[distance];
		for (int i=0;i<distance;i++) {
			int randomSup = problem.getRandomSupplierId(banned);
			int total=solution.totalQuantityBought(randomSup);
			if (total==0) {
				i--;
				continue;
			}
            banArray[i]=randomSup;
			banned.add(banArray[i]);
		}
		
		for(int i=0;i<distance;i++){
			emptySupplier(banArray[i],banned,result);
		}
		
		return result;
	}
	
	/*
	 * Cerca per quanto possibile di svuotare completamente il fornitore bannedSupId
	 * Se gli altri mercati non sono sufficienti a contenere i suoi prodotti svuota tutto
	 * quello è possibile spostare mantenendo l'ammissibilità
	 */
	protected void emptySupplier(int bannedSupId, HashSet<Integer> banned, Solution solution) {
		int startSupplier = problem.getRandomSupplierId(banned);
		int numSuppliers=problem.getDimension();
		int s=startSupplier;
		boolean done=false;
		while (!done) {			
			for(int p=1;p<=problem.getNumProducts();p++){
				//quanto sto comprando attualmente del prodotto i presso il fornitore bandito
				int currentlyBuying=solution.getQuantity(bannedSupId,p);
				if (currentlyBuying==0)
					continue; //non sto comprando niente, passo al prodotto successivo
				
				//disponibilità residua del fornitore s
				int residual=problem.getSupplier(s).getResidual(p,solution);
				//se ha disponibilità residua, sposto tutto il possibile da bannedSup a s
				if(residual>0)
					solution.moveQuantity(p,bannedSupId,s,Math.min(currentlyBuying,residual),problem);				
			}
			
			//incremento ciclico del supplier
			s = s%numSuppliers+1;
			
			//il fornitore s non va bene come destinazione se è contenuto nella banned list
			//quindi lo incremento ancora prima di ricominciare il ciclo
			while (banned.contains(s) && s!=startSupplier) {
				s = s%numSuppliers+1;
			}
			//controllo se ho finito il giro
			if (s==startSupplier)
				done=true;			
		}
		
	}
}

package solutionhandlers;

import java.util.HashSet;

import rob.Problem;
import rob.Solution;
import rob.Supplier;

public class BasicNeighbourGenerator extends NeighbourGenerator{
	protected Problem problem;
	public BasicNeighbourGenerator(Problem problem) {
		this.problem=problem;
	}
	
	private final long MAXIMUM_TIME=20000; //massimo tempo = 20 secondi, dopo torno la soluzione che ho trovato lanciando un warning
	
	/*
	 * Genera un vicino esattamente di distanza distance, dove distance = |currentSolution-nextSolution|/2, cioè il numero di unità di prodotto scambiate
	 */
	@Override
	public Solution generate(Solution solution, int distance){
		long startTime=System.currentTimeMillis();
		Solution neighbour= new Solution(solution); //genero una copia della soluzione corrente
		int newDistance=0;
		while(newDistance<distance){
			int randomProduct = 1+(int)(Math.random()*problem.getNumProducts());
			
			//Primo supplier: da questo supplier voglio acquistare un'unità IN MENO.
			//metto in blacklist quei supplier da cui non sto comprando randomProduct
			HashSet<Integer> notBuyingFrom=new HashSet<Integer>();
			for (int i=1;i<=problem.getDimension();i++){
				if (neighbour.getSolutionMatrix()[i][randomProduct]==0)
					notBuyingFrom.add(i);
			}
			Supplier supplier1=problem.getRandomSupplier(notBuyingFrom);
			if (supplier1==null) // questo può verificarsi solo se la domanda di randomProduct è zero
				continue;
			//Secondo supplier: da questo supplier voglio acquistare un'unità IN PIÙ
			//metto in blacklist i supplier che non hanno almeno un'unità residua da acquistare
			HashSet<Integer> exclude=new HashSet<Integer>();
			exclude.add(supplier1.getId()); //chiaramente non devo ri-estrarre supplier1
			for (int i=1;i<=problem.getDimension();i++) {
				if(problem.getSupplier(i).getResidual(randomProduct, neighbour)<1)
					exclude.add(i);
			}
			Supplier supplier2=problem.getRandomSupplier(exclude);
			if (supplier2==null) //questo si verifica se supplier1 è l'unico venditore di randomProduct, o se tutti gli altri venditori di randomProduct non hanno residuo
				continue;
			
			neighbour.moveQuantity(randomProduct, supplier1.getId(), supplier2.getId(), 1, problem);
			newDistance=solution.calcDistance(neighbour);
			if (System.currentTimeMillis()-startTime>= MAXIMUM_TIME){
				System.err.println("Warning: La generazione di un vicino alla distanza "+ distance + " sta richiedendo troppo tempo.\nRestituisco un vicino a distanza " + newDistance);
				return neighbour;
			}
		}

		return neighbour;
	}
	
	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
	}
	
}

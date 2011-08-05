package solvingalgorithm;

import neighbourgenerator.NeighbourGenerator;
import data.Problem;
import data.Solution;
import util.Utility;

public class LocalSearch extends TemporizedAlgorithm{
	public enum SuccessorChoiceMethod{
		FIRST_IMPROVEMENT, BEST_IMPROVEMENT 
	}
	
	private int maxNeighboursNumber; //Numero massimo di vicini da esplorare
	private int maxStepsNumber; //numero massimo di passi prima di arrestare
	private SuccessorChoiceMethod successorChoice;
	private NeighbourGenerator generator;
	
	public LocalSearch(int maxNeighboursNumber, int maxStepsNumber, SuccessorChoiceMethod successorChoice,
			NeighbourGenerator generator, Problem problem){
		this.problem=problem;
		this.maxNeighboursNumber=maxNeighboursNumber;
		this.maxStepsNumber=maxStepsNumber;
		this.successorChoice=successorChoice;
		this.generator= generator;
	}
	
	//maximumTime espresso in secondi
	public LocalSearch(int maxNeighboursNumber, int maxStepsNumber, SuccessorChoiceMethod successorChoice,
			NeighbourGenerator generator, Problem problem, int maxTime){
		this.problem=problem;
		this.maxNeighboursNumber=maxNeighboursNumber;
		this.maxStepsNumber=maxStepsNumber;
		this.successorChoice=successorChoice;
		this.generator= generator;
		timer = new Timer(maxTime);
	}
	
	public Solution execute(Solution startSolution){
		if(timer!=null){
			timer.addObserver(this);
			timer.start();
		}
		
		Solution currentSolution=startSolution;
		int step;
		for (step=1;step<=maxStepsNumber;step++) {
			//controllo che il timer non mi abbia detto di fermarmi
			if(stop){
				return currentSolution;
			}
			
			Solution successor;
			//TODO eliminare switch con polimorfismo (state o strategy?)
			switch(successorChoice) {
			case FIRST_IMPROVEMENT: successor=firstImprovementExploration(currentSolution);break;
			case BEST_IMPROVEMENT: successor=bestImprovementExploration(currentSolution);break;
			default: successor=firstImprovementExploration(currentSolution);break;
			}
			if(successor!=null){ 
				//ho un successore, continuo la ricerca
				currentSolution=successor;
			}
			else{
				//l'esplorazione del vicinato ha ritornato null, siamo in un minimo locale
				return currentSolution;
			}
		}
		return currentSolution; //arrivo qui se esaurisco il numero di passi
	}
	

	
	private Solution bestImprovementExploration(Solution solution) {
		Solution best=solution;
		for (int created=0;created<maxNeighboursNumber;created++){
			//controllo di non avere superato tempo max
			/*if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return best;
			}*/
			
			Solution neighbour=generator.generate(solution, 1);
			if(neighbour.getObjectiveFunction()<best.getObjectiveFunction()){
				best=neighbour;
			}	
		}
		if(best!=solution){ 
			//ritorno best solo se è cambiato rispetto a solution
			//stampa n° vicini generati
			//printNumNeighbours(maxNeighboursNumber);
			return best;
		}else{
			//stampa n° vicini generati
			//printNumNeighbours(maxNeighboursNumber);
			return null;
		}
	}

	
	private Solution firstImprovementExploration(Solution solution) {
		int visited;
		for(visited=1;visited<=maxNeighboursNumber;visited++) {
			//controllo di non avere superato tempo max
			/*if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return solution;
			}*/
			
			//stampa vicino
			//printNeighbour(visited);
			Solution neighbour=generator.generate(solution, 1);
			if (neighbour.getObjectiveFunction()<solution.getObjectiveFunction()){
				//stampa n° vicini generati
				//printNumNeighbours(visited);
				//#n° vicini generati
				return neighbour; //Ho trovato un vicino migliore della soluzione corrente
			}
		}
		//stampa n° vicini generati
		//printNumNeighbours(visited-1);
		//#n° vicini generati
		return null;
	}
}















import io.Io;
import io.ProblemParser;
import neighbourgenerator.AdvancedNeighbourGenerator;
import neighbourgenerator.BanFullNeighbourGenerator;
import neighbourgenerator.EmptyCellsNeighbourGenerator;
import data.Problem;
import data.Solution;
import solutiongenerator.RandomSolutionGenerator;
import solvingalgorithm.Cplex;
import solvingalgorithm.temporizedalgorithm.VNS;
import solvingalgorithm.temporizedalgorithm.localsearch.LocalSearch;
import solvingalgorithm.temporizedalgorithm.localsearch.LocalSearch.StrategyName;



public class Main {
		
	public static void main(String[] args) throws Exception{
		final String PROBLEM_NAME = "Cap.50.40.3.1.70.1.ctqd";
		
		ProblemParser problemParser = null;
		final String INPUT_PARAMETER = "input";
		try {
			//il parametro input in config.txt indica la cartella con i file di input
			problemParser = new ProblemParser(Io.getConfigParameter(INPUT_PARAMETER));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Errore:\nNon è stato possibile leggere il parametro "+INPUT_PARAMETER+
					"nel file di configurazione. Il programma verrà terminato.");			
			System.exit(1);
		}
		
		Problem problem = null;
		problem = problemParser.parse(PROBLEM_NAME);
		
		
		//local search
		final int MAX_NEIGHBOURS_NUMBER = 50;
		final int MAX_STEPS_NUMBER = 1000;
		final StrategyName successorChoice = StrategyName.BEST_IMPROVEMENT;
		final AdvancedNeighbourGenerator neighGenerator = new AdvancedNeighbourGenerator(problem);
      	LocalSearch localSearch = new LocalSearch(MAX_NEIGHBOURS_NUMBER, MAX_STEPS_NUMBER, successorChoice,
      			neighGenerator, problem);
		
		//vns interna
      	final double RANDOMIZATION_FACTOR = 12/50;
		EmptyCellsNeighbourGenerator intShaking = new EmptyCellsNeighbourGenerator(problem, RANDOMIZATION_FACTOR);
		final int L_MAX = 10;
		final int K_INCREMENT = 2;
		final int NUM_RESTARTS = -1;
		VNS intVNS = new VNS(L_MAX, localSearch, intShaking, NUM_RESTARTS, problem);
		intVNS.setIncrement(K_INCREMENT);
		
		//vns esterna
      	//s0 = soluzione iniziale random
		RandomSolutionGenerator randomGenerator = new RandomSolutionGenerator(problem);
      	Solution s0 = randomGenerator.generate();
      	
      	final int K_MAX = 6;
      	final BanFullNeighbourGenerator extShaking = new BanFullNeighbourGenerator(problem);
      	//in secondi
      	final int MAX_TIME = 30;
      	VNS extVNS = new VNS(K_MAX, intVNS, extShaking, problem, MAX_TIME);
		
      	//lancio la VNS esterna (s1=soluzione finale)
      	Solution s1 = extVNS.execute(s0);
      	System.out.println("funzione obiettivo della soluzione s1 = "+s1.getObjectiveFunction()+"\n\n\n");
		
		//trovo ottimo con cplex
		Cplex cplexSolver = new Cplex(problem);
		Solution cplexSol = cplexSolver.execute(null);
		
		//stampo errore di s1
		double error = s1.getObjectiveFunction()/cplexSol.getObjectiveFunction()*100-100;
		System.out.println("\n\n\nerrore della soluzione s1 = "+error);
	}
}

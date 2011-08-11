
import io.Io;
import io.ProblemParser;
import neighbourgenerator.AdvancedNeighbourGenerator;
import neighbourgenerator.EmptyCellsNeighbourGenerator;
import data.Problem;
import data.Solution;
import solutiongenerator.LinesSolutionGenerator;
import solutiongenerator.TrivialSolutionGenerator;
import solvingalgorithm.Cplex;
import solvingalgorithm.temporizedalgorithm.VNS;
import solvingalgorithm.temporizedalgorithm.localsearch.LocalSearch;
import solvingalgorithm.temporizedalgorithm.localsearch.LocalSearch.StrategyName;
import util.Utility;



public class Main {
		
	public static void main(String[] args) throws Exception{
		final String PROBLEM_NAME = "Cap.50.100.3.2.10.2.ctqd";
		
		ProblemParser problemParser = null;
		final String INPUT_PARAMETER = "input";
		try {
			//il parametro input in config.txt indica la cartella con i file di input
			problemParser = new ProblemParser(Io.getConfigParameter(INPUT_PARAMETER));
		} catch (Exception e) {
			e.printStackTrace();
			//TODO eliminare qua
			//System.err.println("Si è verificato un errore il programma verrà terminato\n"+
					//"(Non è stato possibile aprire il file di configurazione "+Constants.CONFIG_FILE+").");			
			//System.exit(Constants.ERROR_CONFIG);
			//TODO fine eliminare
			
			Utility.exception("Non è stato possibile leggere il parametro "+INPUT_PARAMETER+
					"nel file di configurazione. Il programma verrà terminato.");
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
		int lMax = 10;
		int kIncrement = 1;
		String intLabel = "i";
		VNS intVNS = new VNS(lMax, localSearch, intShaking, problem, -1, 2000);
		//intVNS.setStatistics(1, outputFile, intLabel);
		//intVNS.setCplex(cplex);
		intVNS.setIncrement(kIncrement);
		
		//vns esterna
      	//s0 = soluzione lines
      	LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
      	Solution lineSol = linesGenerator.generate();
      	
      	//s0 = soluzione random
/*		RandomSolutionGenerator randomGenerator = new RandomSolutionGenerator(problem);
      	Solution s0 = randomGenerator.generate();*/
      	
      	//s0 = soluzione trivial
      	TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
      	Solution s0 = trivialGenerator.generate();
      	
/*      	int kMax = 6;
      	BanFullNeighbourGenerator extShaking = new BanFullNeighbourGenerator(problem);
      	int numRestarts = -1;
      	int time = 3600;
      	String extLabel = "e";
      	VNS extVNS = new VNS(kMax, intVNS, extShaking, problem, numRestarts, time);
      	extVNS.setStatistics(1, outputFile, extLabel);*/
      	//extVNS.setCplex(cplex);
		

		
		/*
		 * lancio VNS: verrà interrotta manualmente!
		 */
		//extVNS.execute(s0);
		intVNS.execute(lineSol);
		
		//TODO
		//fare esempio con cplex [...]
		Cplex cplex;
		
	}
}

package benchmarktest;


import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.AdvancedNeighbourGenerator;
import solutionhandlers.BanSupplierNeighbourGenerator;
import solutionhandlers.BanFullNeighbourGenerator;
import solutionhandlers.BasicNeighbourGenerator;
import solutionhandlers.DirectionedBanNeighbourGenerator;
import solutionhandlers.EmptyCellsNeighbourGenerator;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.NeighbourGenerator;
import solutionhandlers.SolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solvingalgorithms.Cplex;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;
import solvingalgorithms.VNS;

public class ProveVarie {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void prova1(){
		//ottimo di Cap.50.100.3.2.10.2.ctqd
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
			
		//cplex
		Cplex cplex 				= new Cplex(problem);
		Solution cplexSolution	= cplex.execute(null);
		cplexSolution.export("cplex_sol.txt");
	}
	
	@Test
	/*
	 * provo local search con AdvancedNeighbourGenerator partendo da una sol trivial
	 */
	public void prova2(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		TrivialSolutionGenerator gen = new TrivialSolutionGenerator(problem);
		Solution tSol = gen.generate();
		System.out.println("fo trivial = "+tSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(20, 20, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGen, problem);
		Solution finalSol = ls.execute(tSol);
		System.out.println("fo lsa(trivial) = "+finalSol.getObjectiveFunction());
	}
	
	@Test
	/*
	 * provo VNS vecchia con lsa
	 */
	public void prova3(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		TrivialSolutionGenerator gen = new TrivialSolutionGenerator(problem);
		Solution tSol = gen.generate();
		System.out.println("fo trivial = "+tSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(20, 20, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGen, problem);
		BasicNeighbourGenerator bGenerator = new BasicNeighbourGenerator(problem);
		VNS vns = new VNS(50, ls, bGenerator, problem);
		
		Solution finalSol = vns.execute(tSol);
		System.out.println("fo VNS(trivial) = "+finalSol.getObjectiveFunction());
	}
	
	
	@Test
	/*
	 * provo local search con AdvancedNeighbourGenerator partendo da una sol lines
	 */
	public void prova4(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
		LinesSolutionGenerator gen = new LinesSolutionGenerator(problem);
		Solution lSol = gen.generate();
		System.out.println("fo lines = "+lSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(100, 20, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGen, problem);
		Solution finalSol = ls.execute(lSol);
		System.out.println("fo lsa(lines) = "+finalSol.getObjectiveFunction());
	}
	
	@Test
	/*
	 * provo VNS con BanSupplierNG e lsa
	 */
	public void prova5(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		TrivialSolutionGenerator gen = new TrivialSolutionGenerator(problem);
		Solution tSol = gen.generate();
		System.out.println("fo trivial = "+tSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(15, 10, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGen, problem);
		BanSupplierNeighbourGenerator banGen = new BanSupplierNeighbourGenerator(problem);
		VNS vns = new VNS((int)(problem.getDimension()/2), ls, banGen, problem);
		
		Solution finalSol = vns.execute(tSol);
		System.out.println("fo VNS(trivial) = "+finalSol.getObjectiveFunction());
	}
	
	
	@Test
	/*
	 * provo local search con AdvancedNeighbourGenerator2 partendo da una sol trivial
	 */
	public void prova6(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		TrivialSolutionGenerator gen = new TrivialSolutionGenerator(problem);
		Solution tSol = gen.generate();
		System.out.println("fo trivial = "+tSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(20, 10, SuccessorChoiceMethod.BEST_IMPROVEMENT, nGen, problem);
		Solution finalSol = ls.execute(tSol);
		System.out.println("fo lsa(trivial) = "+finalSol.getObjectiveFunction());
	}
	
	@Test
	/*
	 * provo local search con AdvancedNeighbourGenerator2 partendo da una sol lines
	 */
	public void prova7(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		LinesSolutionGenerator gen = new LinesSolutionGenerator(problem);
		Solution lSol = gen.generate();
		System.out.println("fo lines = "+lSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(20, 20, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGen, problem);
		Solution finalSol = ls.execute(lSol);
		System.out.println("fo lsa(lines) = "+finalSol.getObjectiveFunction());
	}
	
	
	@Test
	/*
	 * provo VNS con BanSupplierNG e lsa2 e lines
	 */
	public void prova8(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		LinesSolutionGenerator gen = new LinesSolutionGenerator(problem);
		Solution lSol = gen.generate();
		System.out.println("fo lines = "+lSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(7, 10, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGen, problem);
		BanSupplierNeighbourGenerator banGen = new BanSupplierNeighbourGenerator(problem);
		VNS vns = new VNS((int)(problem.getDimension()/3), ls, banGen, problem);
		
		Solution finalSol = vns.execute(lSol);
		System.out.println("fo VNS(lines) = "+finalSol.getObjectiveFunction());
	}
	
	@Test
	/*
	 * 
	 */
	public void prova9(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		SolutionGenerator gen = new TrivialSolutionGenerator(problem);
		Solution lSol = gen.generate();
		System.out.println("fo lines = "+lSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(20, 20, SuccessorChoiceMethod.BEST_IMPROVEMENT, nGen, problem);
		
		Solution iSol=ls.execute(lSol);
		
		EmptyCellsNeighbourGenerator ecGen = new EmptyCellsNeighbourGenerator(problem);
		VNS vnsInternal = new VNS(10, ls, ecGen, problem);
		
		BanSupplierNeighbourGenerator banGen = new BanSupplierNeighbourGenerator(problem);
		VNS vnsExternal = new VNS((int)(problem.getDimension()/3), vnsInternal, banGen, problem,1,-1);
		
		Solution finalSol = vnsExternal.execute(iSol);
		System.out.println("fo VNS(lines) = "+finalSol.getObjectiveFunction());
	}
	
	@Test
	/*
	 * provo VNS con EmptyCellsNG e lsa2 e lines
	 */
	public void prova10(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		LinesSolutionGenerator gen = new LinesSolutionGenerator(problem);
		Solution lSol = gen.generate();
		System.out.println("fo lines = "+lSol.getObjectiveFunction());
		
		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch ls = new LocalSearch(100, 20, SuccessorChoiceMethod.BEST_IMPROVEMENT, nGen, problem);
		EmptyCellsNeighbourGenerator ecGen = new EmptyCellsNeighbourGenerator(problem);
		VNS vns = new VNS(problem.getNumProducts()/2, ls, ecGen, problem);
		
		Solution finalSol = vns.execute(lSol);
		System.out.println("fo VNS(lines) = "+finalSol.getObjectiveFunction());
	}

	@Test
	public void prova11(){
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//62
		Problem problem = parser.parse("Cap.50.100.3.2.10.2.ctqd");
		SolutionGenerator gen = new LinesSolutionGenerator(problem);
		Solution preStartSol = gen.generate();
		System.out.println("fo lines = "+preStartSol.getObjectiveFunction());
		NeighbourGenerator basic=new BasicNeighbourGenerator(problem);

		

		AdvancedNeighbourGenerator nGen = new AdvancedNeighbourGenerator(problem);
		LocalSearch preprocessingLs = new LocalSearch(10, 10, SuccessorChoiceMethod.FIRST_IMPROVEMENT, nGen, problem);
		VNS preprocessingVNS = new VNS(50, preprocessingLs, basic, problem);
		preprocessingVNS.setIncrement(10);
		Solution startSol = preprocessingVNS.execute(preStartSol);
		System.out.println("Post Preprocessing = " + startSol.getObjectiveFunction());
		
		LocalSearch ls = new LocalSearch(50, 10, SuccessorChoiceMethod.BEST_IMPROVEMENT, nGen, problem);
		
		EmptyCellsNeighbourGenerator ecGen = new EmptyCellsNeighbourGenerator(problem);
		VNS vnsInternal = new VNS(10, ls, ecGen, problem);
		vnsInternal.setIncrement(3);
		
		BanSupplierNeighbourGenerator banGen = new BanSupplierNeighbourGenerator(problem);
		VNS vnsExternal = new VNS(6, vnsInternal, banGen, problem,-1,300);
		
		Solution finalSol = vnsExternal.execute(startSol);
		System.out.println("fo VNS(lines) = "+finalSol.getObjectiveFunction());
		finalSol.export("/home/nekomukuro/Solution62-2.txt");
	}
	
	@Test
	public void prova12(){
		ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = probParser.parse("Cap.50.100.3.2.10.2.ctqd");
		//local search
		int maxNeighboursNumber = 50;
		int maxStepsNumber = 10;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.BEST_IMPROVEMENT;
		AdvancedNeighbourGenerator neighGenerator = new AdvancedNeighbourGenerator(problem);
      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
      			neighGenerator, problem);
		
		//vns interna
		EmptyCellsNeighbourGenerator intShaking = new EmptyCellsNeighbourGenerator(problem);
		int lMax = 1;
		int kIncrement = 1;
		VNS intVNS = new VNS(lMax, locSearch, intShaking, problem);
		intVNS.setIncrement(kIncrement);
		//soluzioni iniziali
      	//s0l = soluzione lines
      	LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
      	Solution s0l = linesGenerator.generate();
      	
      	//s0t = soluzione trivial
      	TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
      	Solution s0t = trivialGenerator.generate();
      	
      	//vns esterna
      	int kMax = 1;
      	BanFullNeighbourGenerator extShaking = new BanFullNeighbourGenerator(problem);
      	int numRestarts = -1;
      	int maximumTime = 20;
      	VNS extVNS = new VNS(kMax, intVNS, extShaking, problem, numRestarts, maximumTime);
      	
      	//s1t=VNS(s0t)
      	Solution s1t = extVNS.execute(s0t);
      	//STAMPA VNS(t)
      	System.out.println(  s1t.getObjectiveFunction()+"\t");
      	
      	
      	
      	//s1l=VNS(s0l)
      	Solution s1l = extVNS.execute(s0l);
      	//STAMPA VNS(t)
      	System.out.println( s1l.getObjectiveFunction()+"\t");
      	
	}
}

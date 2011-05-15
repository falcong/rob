package benchmarktest;


import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.AdvancedNeighbourGenerator;
import solutionhandlers.AdvancedNeighbourGenerator2;
import solutionhandlers.BanSupplierNeighbourGenerator;
import solutionhandlers.BasicNeighbourGenerator;
import solutionhandlers.EmptyCellsNeighbourGenerator;
import solutionhandlers.LinesSolutionGenerator;
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
		
		AdvancedNeighbourGenerator2 nGen = new AdvancedNeighbourGenerator2(problem);
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
		
		AdvancedNeighbourGenerator2 nGen = new AdvancedNeighbourGenerator2(problem);
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
		
		AdvancedNeighbourGenerator2 nGen = new AdvancedNeighbourGenerator2(problem);
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
		
		AdvancedNeighbourGenerator2 nGen = new AdvancedNeighbourGenerator2(problem);
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
		
		AdvancedNeighbourGenerator2 nGen = new AdvancedNeighbourGenerator2(problem);
		LocalSearch ls = new LocalSearch(100, 20, SuccessorChoiceMethod.BEST_IMPROVEMENT, nGen, problem);
		EmptyCellsNeighbourGenerator ecGen = new EmptyCellsNeighbourGenerator(problem);
		VNS vns = new VNS(problem.getNumProducts()/2, ls, ecGen, problem);
		
		Solution finalSol = vns.execute(lSol);
		System.out.println("fo VNS(lines) = "+finalSol.getObjectiveFunction());
	}
	
}

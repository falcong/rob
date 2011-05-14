package rob;

import java.io.File;

import solvingalgorithms.Algorithm;
import solutionhandlers.SolutionGenerator;

public class Run {
	//algoritmo da usare per risolvere i problemi
	private Algorithm algorithm;
	/*
	 * algorithm contiene il metodo con cui generare la soluzione iniziale da dare in pasto a algorithm;
	 * pure problemFile tale soluzione e dunque startSolutionMethod e problemFile sono utilizzati in 
	 * maniera esclusiva
	 */
	private SolutionGenerator startSolutionGenerator;
	
	/*
	 * Se true dice al Solver di salvare le matrici delle soluzioni in un file
	 */
	private boolean exportSolutionsOption;
	private String exportPath;
	private String exportSuffix;
	
	/*
	 * Contiene i problemi da risolvere
	 */
	private Problem [] problems;

//	/*
//	 * Crea un run con tutti i parametri
//	 */
//	public Run(Problem [] problems, Algorithm algorithm, SolutionGenerator startSolutionMethod){
//		this.problems=problems;
//		this.algorithm=algorithm;
//		this.startSolutionGenerator=startSolutionMethod;
//		this.exportSolutionsOption=false;
//	}
	
	/*
	 * crea un Run vuoto
	 */
	public Run() {
		
	}

	public void setExportSolutionsOption(boolean exportSolutionsOption) {
		this.exportSolutionsOption = exportSolutionsOption;
	}

	public boolean isExportSolutionsOptionEnabled() {
		return exportSolutionsOption;
	}
	
	public Problem getProblem(int number) {
		return problems[number];
	}
	
	public SolutionGenerator getSolutionGenerator() {
		return startSolutionGenerator;
	}

	public void setProblems(Problem [] problems) {
		this.problems = problems;
	}
	
	public void setExportSuffix(String suffix){
		this.exportSuffix=suffix;
	}

	public Problem [] getProblems() {
		return problems;
	}

	public void setSolutionGenerator(SolutionGenerator solutionGenerator) {
		this.startSolutionGenerator=solutionGenerator;		
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm=algorithm;
	}
	
	public Algorithm getAlgorithm() {
		return this.algorithm;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

	public String getExportPath() {
		return exportPath;
	}

	public int getNumProblems() {
		return problems.length;
	}

	public Solution[] execute() {
		Solution [] allSolutions = new Solution[problems.length];
		for (int i=0;i<problems.length;i++){
			System.out.println("\nEseguo " + problems[i].getName());
			Problem problem=problems[i];
			algorithm.setProblem(problem);
			Solution startSolution;
			if(startSolutionGenerator!=null){
				startSolutionGenerator.setProblem(problem);
				startSolution = startSolutionGenerator.generate();
				System.out.println("Funzione obiettivo di partenza " + startSolution.getObjectiveFunction());
				if(!startSolution.isAdmissible(problem))
					throw new Error("Errore: la soluzione di partenza non è ammissibile per il problema. Termino.");
				} 
			else
				startSolution=null;
			allSolutions[i]=algorithm.execute(startSolution);
			System.out.println("Funzione obiettivo finale " + allSolutions[i].getObjectiveFunction());
			if (isExportSolutionsOptionEnabled()){
				allSolutions[i].export(exportPath + File.separator + problem.getName() +"_"+ exportSuffix +".txt");
			}
		}
		return allSolutions;
	}
}

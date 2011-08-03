package solutiongenerator;

import data.Problem;
import data.Solution;

public class FileSolutionGenerator extends SolutionGenerator {
	/*
	 * Questa classe genera una soluzione importando un file di una soluzione esportata precedentemente
	 */
	
	private String importFile;
	
	public FileSolutionGenerator(Problem problem) {
		super(problem);
	}

	public FileSolutionGenerator() {
	}

	@Override
	public Solution generate() throws Exception {
		return new Solution(importFile,problem);
	}

	public void setImportFile(String importFile) {
		this.importFile = importFile;
	}

	public String getImportFile() {
		return importFile;
	}
	
	

}

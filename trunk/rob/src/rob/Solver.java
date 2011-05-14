package rob;

public class Solver {
	
	public static void solve(Run [] runs){
		for (int i=0;i<runs.length;i++) {
			System.out.println("\nEseguo Run " + (i+1));
			runs[i].execute();
		}
			
		
	}
	
}

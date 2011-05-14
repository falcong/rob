package rob;


import io.InputFileParser;

public class Rob {
		
	public static void main(String[] inputFileStrings){
		InputFileParser parser = new InputFileParser();
		int length = inputFileStrings.length;
		Run [] runs = new Run[length];
		try {
			for (int i=0;i<length;i++) {
				runs[i]=(Run)parser.parse(inputFileStrings[i]);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(e.getMessage());
		}
		
		
		Solver.solve(runs);
		
	}
}

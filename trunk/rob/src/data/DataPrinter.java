package data;

public class DataPrinter {
    /**
     * Stampa la matrice bidimensionale che riceve in ingresso.
     * 
     * @param matrix - matrice bidimensonale da stampare.
     * @param x - indica il nome delle righe.
     * @param y - indica il nome delle colonne.
     * @param i - indicano da quale riga partire.
     * @param j - indicano da quale colonna partire.
     * @param numTab1 - numero tab prima riga intestazione.
     * @param numTab2 - numero tab prima riga intestazione.
     * @param numTab3 - numero tab inizio ogni riga matrice.
     * @param numTab4 - numero tab fra gli elementi di ogni riga della matrice.
     */
	protected static void printMatrix2D(double matrix[][], String x, String y,
			int i, int j, int numTab1, int numTab2, int numTab3, int numTab4){
		int numRows		= matrix.length; 
		int numColumns	= matrix[0].length;
		
		//intestazione tabella [y-j y-j+1 y-j+2 ...]
		for(int a=j; a<=numColumns-1; a++){
			//la prima volta numTab1 tab poi numTab2
			if(a==j){
				for(int c=1; c<=numTab1; c++){
					System.out.print("\t");
				}
			}else{
				for(int c=1; c<=numTab2; c++){
					System.out.print("\t");
				}
			}
			System.out.print(y+a);
		}
		System.out.println("\n");
		
		//stampo le righe
		for(int a=i; a<=numRows-1; a++){
			//intestazione 
			System.out.print(x+a);
			for(int b=j; b<=numColumns-1; b++){
				//la prima volta numTab3 tab poi numTab4
				if(b==j){
					for(int c=1; c<=numTab3; c++){
						System.out.print("\t");
					}
				}else{
					for(int c=1; c<=numTab4; c++){
						System.out.print("\t");
					}
				}
				System.out.print("("+a+","+b+")="+matrix[a][b]);
			}
			System.out.println("");
		}
		System.out.println("");
	}
}

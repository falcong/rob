package rob;

import java.util.HashSet;

import rob.Supplier;

public class Problem {
	private String name;
	private String type;
	//-1 vuol dire classe non specificata
	private int problemClass;
	//numero dei fornitori
	private int dimension;
	/*
	 * numero massimo degli intervalli di sconto
	 * [viene conteggiata anche la fascia 0-esima in cui non è applicato alcuno sconto]
	 * se vale -1 vuol dire che non è stato specificato
	 */
	private int maxNRange;
	private int numProducts;
	/*
	 *  vettore contenente la domanda per i vari prodotti; demand[k]=domanda del 
	 *  prodotto k-esimo; demand[0] non è usato quindi demand.length=numProducts+1
	 */
	private int demand [];
	/*
	 * numero totale di prodotti che demand mi chiede di acquistare
	 */
	private int totalDemand;
	/*
	 * contiene i fornitori; suppliers[0] non è usato
	 */
	private Supplier suppliers[];
	
	
	public Problem(String name, String type, int problemClass, int maxNRange,
		int demand [], Supplier suppliers[]){
		this.name 			= name;
		this.type 			= type;
		this.problemClass	= problemClass;
		dimension			= suppliers.length-1;
		this.maxNRange		= maxNRange;
		numProducts			= demand.length-1;
		this.demand 		= demand;//
		totalDemand = 0;
		for(int p=1; p<=numProducts; p++){
			totalDemand += demand[p];
		}
		this.suppliers			= suppliers;

	}

	public String getName(){
		return name;
	}

	/*
	 * Restituisce un fornitore scelto a caso non contenuto in suppBlacklist
	 */
	public Supplier getRandomSupplier(HashSet<Integer> suppBlacklist) {
		
		int id = getRandomSupplierId(suppBlacklist);
		if(id==0)
			return null;
		return getSuppliers()[id];
	}

	public int getRandomSupplierId(HashSet<Integer> suppBlacklist) {
		int id;
		if (suppBlacklist.size()>=getDimension())
			return 0;
		boolean ok=false;
		do{
			id=1+(int)(Math.random()*getDimension());
			if (suppBlacklist.contains(id))
				continue;
			else
				ok=true;
		} while(!ok);
		return id;
	}
	
	/*
	 * Metodo overloaded che non richiede in ingresso una blacklist e genera automaticamente una blacklist vuota
	 */
	public Supplier getRandomSupplier() {
		HashSet<Integer> blacklist = new HashSet<Integer>();
		return getRandomSupplier(blacklist);
	}
	
	/*
	 * Metodo che estrae un Supplier diverso da quello dato in ingresso
	 */
/*	public Supplier getRandomSupplierDifferentFrom(Supplier exclude) {
		HashSet<Integer> blacklist = new HashSet<Integer>();
		blacklist.add(exclude.getId());
		return getRandomSupplier(blacklist);
	}*/


/*	public void setDimension(int dimension) {
		this.dimension = dimension;
	}*/

	public int getDimension() {
		return dimension;
	}

/*	public void setNumProducts(int numProducts) {
		this.numProducts = numProducts;
	}*/

	public int getNumProducts() {
		return numProducts;
	}

/*	public void setDemand(int demand[]) {
		this.demand = demand;
	}*/

	public int[] getDemand() {
		return demand;
	}
	
	public int getProductDemand(int product){
		return demand[product];
	}

/*	public void setSuppliers(Supplier suppliers[]) {
		this.suppliers = suppliers;
	}*/

	public Supplier[] getSuppliers() {
		return suppliers;
	}

	public Supplier getSupplier(int id) {
		return suppliers[id];
	}

	/*
	 * restituisce la disponibilità di product presso il fornitore supplier
	 */
	public int getAvailability(int supplier, int product) { 
		int availability = suppliers[supplier].getAvailability(product);
		if(availability==-1){
			return 0;
		}else{
			return availability;
		}
	}
	
	/*
	 * restituisce il numero di fasce di sconto del fornitore supplierId
	 * [la fascia 0-esima non viene contata]
	 */
	public int getNumSegments(int supplierId) {
		return suppliers[supplierId].getNumSegments();
	}

	public int getProblemClass() {
		return problemClass;
	}
	
	/*
	 * restituisce la massima quantità di prodotti acquistabili presso un fornitore
	 */
	public int getMaxQuantityBuyable(int supplier){
		int sum = 0;
		for(int p=1; p<=numProducts; p++){
			int availability = suppliers[supplier].getAvailability(p);
			if(availability==-1){
				availability=0;
			}
			
			sum += Math.min(demand[p], availability);
		}
		
		return sum;
	}
	
	//restituisce la massima fascia di sconto attivabile di supplier
	public int maxSegmentActivable(int supplier){
		return suppliers[supplier].activatedSegment(getMaxQuantityBuyable(supplier));
	}
	
	public boolean cellIsEmptiable(int cell, Solution solution, HashSet<Integer> forbiddenCells){
		
		return true;
	}
	
	public int getSupplier (int cell, int product) {
		return (cell-product)/numProducts+1;
	}
	
	public int getProduct (int cell) {
		return (cell-1)%numProducts+1;
	}
	
	public int getCell(int supplier,int product){
		return (supplier-1)*numProducts+product;
	}
}

package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;
import org.junit.Test;
import data.Problem;


public class ProblemParserTest {
	Problem problem;
	final int PRODUCT_1_ID = 1;
	final int PRODUCT_2_ID = 2;
	final int SUPPLIER1_ID = 1;
	final int SUPPLIER2_ID = 2;
	//PRICE<fornitore><prodotto>
	final int PRICE11 = 101;
	final int PRICE21 = 201;
	final int PRICE22 = 102;


	//metodo parse
	/*
	 * Caso generico.
	 */
	@Test
	public final void testParse1() throws Exception{
		String PROBLEM = "problema6.txt";
		testParse(PROBLEM);
	}
	
	/*
	 * Input malformattato: manca OFFER_SECTION
	 */
	@Test(expected = Exception.class)
	public final void testParse2() throws Exception{
		String PROBLEM = "problema6a.txt";
		testParse(PROBLEM);
	}
	
	private final void testParse(String problemName) throws Exception {
		ProblemParser pp = new ProblemParser(Constants.INPUT_PATH);
		problem = pp.parse(problemName);
		
		//controllo che tutti i dati del problema siano corretti
		//NAME
		assertTrue(problem.getName().equals(problemName));
		
		//CLASS
		final int PROBLEM_CLASS = 1;
		assertTrue(problem.getProblemClass() == PROBLEM_CLASS);
		
		//DIMENSION
		final int DIMENSION = 2;
		assertTrue(problem.getDimension() == DIMENSION);
		
		//DEMAND_SECTION
		checkDemand(); 
		
		//OFFER_SECTION
		checkOffer();
		
		//DISCOUNT_SECTION
		checkDiscount();
	}

	
	/*
	 * Controlla la correttezza dei valori letti nella sezione DEMAND.
	 */
	private void checkDemand() {
		final int NUM_PRODUCTS = 2;
		final int PRODUCT1_DEMAND = 60;
		final int PRODUCT2_DEMAND = 40;
		assertEquals(problem.getNumProducts(), NUM_PRODUCTS);
		assertEquals(problem.getProductDemand(PRODUCT_1_ID), PRODUCT1_DEMAND);
		assertEquals(problem.getProductDemand(PRODUCT_2_ID), PRODUCT2_DEMAND);
	}
	
	
	/*
	 * Controlla la correttezza dei valori letti nella sezione OFFER.
	 */
	private void checkOffer(){
		//controllo i prezzi
		assertEquals(PRICE11, (int)problem.getSupplier(SUPPLIER1_ID).getBasePrice(PRODUCT_1_ID));
		assertEquals(PRICE22, (int)problem.getSupplier(SUPPLIER2_ID).getBasePrice(PRODUCT_2_ID));
		
		//controllo le disponibilità
		//AVAILABILITY<fornitore><prodotto>
		final int AVAILABILITY11 = 51;
		final int AVAILABILITY22 = 42;
		assertEquals(AVAILABILITY11, (int)problem.getSupplier(SUPPLIER1_ID).getAvailability(PRODUCT_1_ID));
		assertEquals(AVAILABILITY22, (int)problem.getSupplier(SUPPLIER2_ID).getAvailability(PRODUCT_2_ID));
	}
	
	
	/*
	 * Controlla la correttezza dei valori letti nella sezione DISCOUNT.
	 */
	private void checkDiscount(){
		//controllo i lower bounds
		//LOWER_BOUND<fornitore> della fascia di sconto 1
		final int LOWER_BOUND1 = 50;
		final int LOWER_BOUND2 = 10;
		final int SEGMENT1_ID = 1;
		assertTrue(problem.getSupplier(SUPPLIER1_ID).getLowerBounds()[SEGMENT1_ID] == LOWER_BOUND1);
		assertTrue(problem.getSupplier(SUPPLIER2_ID).getLowerBounds()[SEGMENT1_ID] == LOWER_BOUND2);
		
		//controllo gli sconti
		//DISCOUNT<fornitore><fascia>
		final int DISCOUNT11 = 4;
		final int DISCOUNT21 = 3;
		//price<fornitore><prodotto><fascia>
		double price111 = PRICE11*(100-DISCOUNT11)/100.0;
		double price211 = PRICE21*(100-DISCOUNT21)/100.0;
		final double TOLERANCE = 0.1;
		assertEquals(price111, problem.getSupplier(SUPPLIER1_ID).getPrice(PRODUCT_1_ID, SEGMENT1_ID), TOLERANCE);
		assertEquals(price211, problem.getSupplier(SUPPLIER2_ID).getPrice(PRODUCT_1_ID, SEGMENT1_ID), TOLERANCE);
	}
}

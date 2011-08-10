package testingjunit;

import static org.junit.Assert.*;
import io.ProblemParser;

import org.junit.Test;

import data.Problem;


public class ProblemParserTest {

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
		Problem problem = pp.parse(problemName);
		
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
		final int NUM_PRODUCTS = 2;
		final int PRODUCT1_DEMAND = 60;
		final int PRODUCT2_DEMAND = 40;
		assertTrue(problem.getNumProducts() == NUM_PRODUCTS);
		final int product1Id = 1;
		assertTrue(problem.getProductDemand(product1Id) == PRODUCT1_DEMAND);
		final int product2Id = 2;
		assertTrue(problem.getProductDemand(product2Id) == PRODUCT2_DEMAND);
		
		//OFFER_SECTION
		//PRICE<fornitore><prodotto>
		final int SUPPLIER1_ID = 1;
		final int SUPPLIER2_ID = 2;
		final int PRICE11 = 101;
		final int PRICE21 = 201;
		final int PRICE22 = 102;
		assertEquals(PRICE11, (int)problem.getSupplier(SUPPLIER1_ID).getBasePrice(product1Id));
		assertEquals(PRICE22, (int)problem.getSupplier(SUPPLIER2_ID).getBasePrice(product2Id));
		
		//AVAILABILITY<fornitore><prodotto>
		final int AVAILABILITY11 = 51;
		final int AVAILABILITY12 = 52;
		final int AVAILABILITY21 = 9;
		final int AVAILABILITY22 = 42;
		//TODO arrivati qui
		assertEquals(PRICE11, (int)problem.getSupplier(SUPPLIER1_ID).getBasePrice(product1Id));
		
		//OFFER_SECTION
		//LOWER_BOUND<fornitore> della fascia di sconto 1
		final int LOWER_BOUND1 = 50;
		final int LOWER_BOUND2 = 10;

		final int SEGMENT1_ID = 1;
		assertTrue(problem.getSupplier(SUPPLIER1_ID).getLowerBounds()[SEGMENT1_ID] == LOWER_BOUND1);
		assertTrue(problem.getSupplier(SUPPLIER2_ID).getLowerBounds()[SEGMENT1_ID] == LOWER_BOUND2);
		
		//DISCOUNT<fornitore><fascia>
		final int DISCOUNT11 = 4;
		final int DISCOUNT21 = 3;
		//price<fornitore><prodotto><fascia>
		double price111 = PRICE11*(100-DISCOUNT11)/100.0;
		double price211 = PRICE21*(100-DISCOUNT21)/100.0;
		final double TOLERANCE = 0.1;
		assertEquals(price111, problem.getSupplier(SUPPLIER1_ID).getPrice(product1Id, SEGMENT1_ID), TOLERANCE);
		assertEquals(price211, problem.getSupplier(SUPPLIER2_ID).getPrice(product1Id, SEGMENT1_ID), TOLERANCE);
	}
}

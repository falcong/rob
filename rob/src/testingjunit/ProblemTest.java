package testingjunit;

import static org.junit.Assert.*;

import java.util.HashSet;

import io.ProblemParser;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Utility;

public class ProblemTest {
	Problem p;
	ProblemParser parser;
	
	@Before
	public void setUp() throws Exception {
		parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		//Problem p = parser.parse("problema1.txt");
		//p = parser.parse("Cap.50.100.5.1.10.1.ctqd");
	}
	
	@Test
	public final void testGetMaxQuantityBuyable(){
		p = parser.parse("problema1.txt");
		assertEquals(76, p.getMaxQuantityBuyable(2));
	}
	
	@Test
	public final void testMaxSegmentActivable(){
		p = parser.parse("problema1.txt");
		assertEquals(2, p.maxSegmentActivable(1));
	}
	
	@Test
	public final void testGetRandomSupplierId(){
		p = parser.parse("problema1.txt");
		int randSupId = p.getRandomSupplierId(new HashSet<Integer>());
		assertTrue(randSupId == 1 || randSupId == 2);
	}

}

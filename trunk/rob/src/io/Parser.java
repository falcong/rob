package io;

public abstract class Parser {
	final int INT_NOT_USED = -1;
	
	public abstract Object parse(String inputFile) throws Exception;
}

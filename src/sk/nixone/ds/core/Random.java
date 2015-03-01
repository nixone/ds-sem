package sk.nixone.ds.core;

/**
 * Wrapper pre ucely buduceho vyvoja, nakolko bude nutne implementovat generatory pre ine nez rovnomerne a normalne normovane rozdelenia.
 * 
 * @author nixone
 *
 */
public class Random extends java.util.Random
{
	public Random() {
		super();
	}
	
	public Random(long seed) {
		super(seed);
	}
}

/*
 * CŽline Bensoussan 
 * CSC303 - Principles of Programming Languages
 * Assignment 3 - Parser
 * Due November 6th, 2012
 */

package main;

/*
 * Class Tokenizer
 */
public class Tokenizer {
	
	private String[] tokenList; //Holds all the tokens of a string
	public Token token; //Holds the current token
	private int index; //Holds the index in array tokenList
	private int total; //Holds the size of the array tokenList
	
	/*
	 * Constructor
	 */
	public Tokenizer(String str){
		this.tokenList = str.split(" "); 
		this.token = new Token("empty");
		index = 0;
		total = this.tokenList.length;
	}
	
	/*
	 * Returns the total number of tokens in the string given
	 */
	public int count(){
		return total;
	}
	
	/*
	 * Returns the index in the array of Tokens
	 */
	public int getIndex(){
		return index;
	}
	
	/*
	 * Returns the next Token if the tokenList
	 */
	public Token nextToken(){		
		this.token = new Token(tokenList[index++]);		
		return this.token;
	}
}

/*
 * CŽline Bensoussan 
 * CSC303 - Principles of Programming Languages
 * Assignment 3 - Parser
 * Due November 6th, 2012
 */

package main;

/*
 * Class Token
 */
public class Token {

	private String value; // Holds the value of the Token
	
	/*
	 * Constructor
	 */
	public Token(String str){
		this.value = str;
	}	 
	
	/*
	 * Returns the value of the Token
	 */
	public String getValue(){
		return this.value;
	}
	
	/*
	 * Returns true if the value of the Token is equal to a given String
	 */
	public boolean is(String str){
		return (value.equals(str));
	}
	
	/*
	 * Prints the value of the Token
	 */
	public void print(){
		System.out.println(value);
	}
}

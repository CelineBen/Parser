/*
 * CŽline Bensoussan 
 * CSC303 - Principles of Programming Languages
 * Assignment 3 - Parser
 * Due November 6th, 2012
 */

package main;

import java.util.regex.*;

/*
 * Class Parser
 */
public class Parser {
	
	private static Tokenizer prog; // Program to parse
	private static Token currentToken; // Current Token to analyze
	private static int indent; // Helper to print tree

	/*
	 * Constructor
	 */
	public Parser(String program){
		prog = new Tokenizer(program);
		currentToken = prog.nextToken();
		indent = 0;		
	}
	
	/*
	 * Parses the program
	 * If error occurs, it prints it.
	 * Example: " Error on token: ( which is token #20 ".
	 */
	public boolean parse(){	
		try{
			parseProgram();
			return true;
		}
		catch(Exception e){
			System.out.println("Error: " + e.getMessage());
			System.out.print("Error on token: " + currentToken.getValue());
			System.out.println(" which is token #" + prog.getIndex());
			return false;
		}
	}
	
	//Prints each element so the result would look like a tree
	private static void printInTree(Token element, int i){
		for(int j = 0; j < i; j++){
			System.out.print("_ ");
		}
		System.out.println(element.getValue());
	}
	
	//Accepts a token if it is the expected kind
	private static void accept(String expected_kind) throws Exception{
		if (currentToken.is(expected_kind)){
			acceptIt();
		}
		else{
			throw new Exception ("Not the expected kind of token");
		}	
	}
	
	//Accepts a token, prints it and goes to the next token
	private static void acceptIt(){
		printInTree(currentToken, indent);
		
		if(prog.getIndex() < prog.count()){
			currentToken = prog.nextToken();
		}
			
	}
	
	/*
	 * Returns true if a given Token is of type ID
	 * Id -> any sequence of character or digits but must start with one of a..zA..Z
	 */
	private static boolean isID(Token test){
		String str = test.getValue();
		Pattern p = Pattern.compile("[a-zA-Z]{1}.*");
		Matcher m = p.matcher(str);
		boolean b = m.matches();
		
		//If Token is "write" or "read", it should not be considered an id
		if(str.equals("write") || str.equals("read")){
			b = false;
		}
		
		return b;
	}			

	/*
	 * Returns true if a given Token is a literal, which is a number.
	 */
	private static boolean isLiteral(Token lit){
		 String str = lit.getValue();
		 Pattern p = Pattern.compile("[0-9]*");
		 Matcher m = p.matcher(str);
		 boolean b = m.matches();
		 return b; 
	}
	
	//program -> stmt_list $$
	private void parseProgram() throws Exception{
		if(isID(currentToken)|| currentToken.is("read")  || currentToken.is("write")){
			Token program = new Token("program");
			printInTree(program, indent++);
			parseStatementList();
			indent--;
		}
		else if(currentToken.is("$$")){
			acceptIt();		
		}
		else{
			throw new Exception ("There is an error in the grammar.");
		}
		
	}
	
	//stmt_list -> stmt stmt_list | empty
	private void parseStatementList() throws Exception{
		if(isID(currentToken)|| currentToken.is("read")  || currentToken.is("write")){
			Token stmt_list = new Token("stmt_list");
			printInTree(stmt_list, indent++);
			parseStatement();
			parseStatementList();
			indent--;
		}
		else if (currentToken.is("$$")){
			Token stmt_list = new Token("stmt_list");
			printInTree(stmt_list, indent--);
			acceptIt();
		}
		else{
			throw new Exception("This is not a statement list");
		}
	}
	
	//stmt ->  id := expr | read id | write expr
	private void parseStatement() throws Exception{		
		if(currentToken.is("read")){
			Token stmt = new Token("stmt");
			printInTree(stmt, indent++);
			acceptIt();
			if(!isID(currentToken)){
				throw new Exception("This is not an expected id");
			}
			else{
				acceptIt();
			}
			indent--;
		}
		else if(currentToken.is("write")){
			Token stmt = new Token("stmt");
			printInTree(stmt, indent++);
			acceptIt();
			parseExpression();
			indent--;
		}
		else if(isID(currentToken)){
			Token stmt = new Token("stmt");
			printInTree(stmt, indent++);
			acceptIt();
			accept(":=");
			parseExpression();
			indent--;
			
		}
		else{
			throw new Exception("This is not a statement");
		}	
	}
	
	//expr -> term term_tail
	private void parseExpression() throws Exception{
		if (currentToken.is("(") || isID(currentToken) || isLiteral(currentToken)){
			Token expr = new Token("expr");
			printInTree(expr, indent++);
			parseTerm();
			parseTermTail();
			indent--;
		}
		else{
			throw new Exception("This is not an expression");
		}
		
	}
	
	//term_tail -> add_op term term_tail | empty
	private void parseTermTail() throws Exception{
		
		//FIRST
		if(currentToken.is("+") || currentToken.is("-")){ 
			Token term_tail = new Token("term_tail");
			printInTree(term_tail, indent++);
			parseAdditionOperator();
			parseTerm();
			parseTermTail();
			indent--;
		}
		
		//FOLLOW 
		else if(isID(currentToken)|| currentToken.is("read")  || currentToken.is("write")){
			Token term_tail = new Token("term_tail");
			printInTree(term_tail, indent);
		}
		else if(currentToken.is(")")){
			Token term_tail = new Token("term_tail");
			printInTree(term_tail, indent);
		}
		else if(currentToken.is("$$")){
			Token term_tail = new Token("term_tail");
			printInTree(term_tail, indent);
		}
		else{
			throw new Exception("This is not a term_tail");
		}
		
	}
	
	//term -> factor factor_tail
	private void parseTerm() throws Exception {
		if (currentToken.is("(") || isID(currentToken) || isLiteral(currentToken)){
			Token term = new Token("term");
			printInTree(term, indent++);
			parseFactor();
			parseFactorTail();	
			indent--;
		}
		else {
			throw new Exception("This is not a term");
		}		
	}
	
	//factor_tail -> mul_op factor factor_tail |  empty
	private void parseFactorTail() throws Exception{
		
		//FIRST 
		if(currentToken.is("*") || currentToken.is("/")){
			Token factor_tail = new Token("factor-tail");
			printInTree(factor_tail, indent++);
			parseMultiplicationOperator();
			parseFactor();
			parseFactorTail();	
			indent--;
		}
		
		//FOLLOW
		else if(isID(currentToken)){ 
			Token factor_tail = new Token("factor-tail");
			printInTree(factor_tail, indent); 
		}
		else if(currentToken.is("read") || currentToken.is("write")){ 
			Token factor_tail = new Token("factor-tail");
			printInTree(factor_tail, indent);
		}
		else if(currentToken.is(")")){ 
			Token factor_tail = new Token("factor-tail");
			printInTree(factor_tail, indent); 
		}
		else if(currentToken.is("+") || currentToken.is("-")){ 
			Token factor_tail = new Token("factor-tail");
			printInTree(factor_tail, indent);
		}
		else if(currentToken.is("$$")){ 
			Token factor_tail = new Token("factor-tail");
			printInTree(factor_tail, indent); 
		}
		else{
			throw new Exception("This is not a factor_tail");
		}
	}
	
	//factor -> ( expr ) | id | literal
	private void parseFactor() throws Exception{
		if(currentToken.is("(")){	
			Token factor = new Token("factor");
			printInTree(factor, indent++);
			acceptIt();
			parseExpression();
			accept(")");
			indent--;		
		}
		else if(isID(currentToken)){
			Token factor = new Token("factor");
			printInTree(factor, indent++);
			acceptIt();
			indent--;
		}
		else if(isLiteral(currentToken)){
			Token factor = new Token("factor");
			printInTree(factor, indent++);
			acceptIt();
			indent--;
		}	
		else{
			throw new Exception("This is not a factor");
		}		
	}
	
	//add_op -> + | -
	private void parseAdditionOperator() throws Exception{
			if(currentToken.is("+")){
				Token op = new Token("add-op");
				printInTree(op, indent++);
				acceptIt();
				indent--;
			}
			else if(currentToken.is("-")){
				Token op = new Token("add-op");
				printInTree(op, indent++);
				acceptIt();
				indent--;
			}
			else
				throw new Exception("This is not a + or - operation");
		}
	
	//mult_op -> * | /
	private void parseMultiplicationOperator() throws Exception{
		if(currentToken.is("*")){
			Token op = new Token("mult-op");			
			printInTree(op, indent++);
			acceptIt();
			indent--;	
		}
		else if(currentToken.is("/")){
			Token op = new Token("mult-op");
			printInTree(op, indent++);
			acceptIt();
			indent--;
		}
		else
			throw new Exception("This is not a * or / operation");
	}

}

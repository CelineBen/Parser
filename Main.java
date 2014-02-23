/*
 * Celine Bensoussan 
 * CSC303 - Principles of Programming Languages
 * Assignment 3 - Parser
 * Due November 6th, 2012
 */

/* ****GRAMMAR****
program -> stmt_list $$
stmt_list -> stmt stmt_list | empty
stmt ->  id := expr | read id | write expr
expr -> term term_tail
term_tail -> add_op term term_tail | empty
term -> factor factor_tail
factor_tail -> mul_op factor factor_tail |  empty
factor -> ( expr ) | id | literal
add_op -> + | -
mult_op -> * | /
*/


package main;

public class Main {
	public static void main(String[] args) {
		
		//Program to test
		String prog = "write bla read abc abc := 5 - 15 * ( 0 / 50 ) $$";
		
		//Initialize parser
		Parser p = new Parser(prog);	
		
		//Parse Program
		p.parse();
	}
	
}





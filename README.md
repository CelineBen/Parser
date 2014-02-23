Parser
======

Parser in Java following this grammar:

****GRAMMAR****
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

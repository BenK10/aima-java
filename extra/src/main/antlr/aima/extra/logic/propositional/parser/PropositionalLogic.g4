grammar PropositionalLogic;

/*
 * Sentence        -> AtomicSentence : ComplexStence
 * AtomicSentence  -> True : False : P : Q : R : ...
 * ComplexSentence -> (Sentence) | [Sentence]
 *                 :  ~Sentence
 *                 :  Sentence & Sentence
 *                 :  Sentence | Sentence
 *                 :  Sentence => Sentence
 *                 :  Sentence <=> Sentence
 *
 * OPERATOR PRECEDENCE: ~, &, |, =>, <=>
 */

/*
=============
GRAMMAR RULES
=============
*/

parse: sentence* EOF;

sentence
    : bracketedsentence
    | atomicsentence
    | NOT sentence
    |<assoc=right> sentence AND sentence
    |<assoc=right> sentence OR sentence
    |<assoc=right> sentence IMPLICATION sentence
    |<assoc=right> sentence BICONDITIONAL sentence
    ;

bracketedsentence
    : '(' sentence ')'
    | '[' sentence ']'
    ;

atomicsentence
    : SYMBOL
    | QUOTED_SYMBOL
    ;

/*
===========
LEXER RULES
===========
*/
    
NOT:              '~';
AND:              '&';
OR:               '|';
IMPLICATION:      '=>';
BICONDITIONAL:    '<=>';

SYMBOL
    :  INITIAL SUBSEQUENT* 
    |  PECULIAR_IDENTIFIER
    ;

QUOTED_SYMBOL
    : '"'  (ESCAPE_SEQUENCE | ~('\\' | '"' ) )* '"'
    | '\'' (ESCAPE_SEQUENCE | ~('\\' | '\'') )* '\''
    ;

COMMENT
    :   '/*' .*? '*/'    -> channel(HIDDEN) // match anything between /* and */
    ;

LINE_COMMENT
    : '//' ~[\r\n]* '\r'? ('\n' | EOF) -> channel(HIDDEN)
    ;

WS  :   [ \t\r\n]+ -> skip
    ; // Define whitespace rule, toss it out

// fragments  
fragment INITIAL : LETTER | SPECIAL_INITIAL;
fragment LETTER : 'a'..'z' | 'A'..'Z';
fragment SPECIAL_INITIAL : '!' | '@' | '#' | '$' | '%' | ':' | '?' | '^' | '_';
fragment SUBSEQUENT : INITIAL | DIGIT | SPECIAL_SUBSEQUENT;
fragment DIGIT : '0'..'9';
fragment SPECIAL_SUBSEQUENT : '.' | '+' | '-' | '@';
fragment PECULIAR_IDENTIFIER : '+' | '-';

fragment ESCAPE_SEQUENCE
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')
    |   UNICODE_ESCAPE
    |   OCTAL_ESCAPE
    ;

fragment UNICODE_ESCAPE
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

fragment OCTAL_ESCAPE
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment HEX_DIGIT
    : ('0'..'9'|'a'..'f'|'A'..'F')
    ;
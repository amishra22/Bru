grammar LabeledExpr;

prog:   stat+ ;

stat:   expr NEWLINE                # printExpr
    |   ID '=' expr NEWLINE         # assign
    |   ID '=' value NEWLINE        # assignvalue
    |   NEWLINE                     # blank
    |   condition NEWLINE           # ifcondition
	|	looping NEWLINE				# loops
    ;

expr:   INT                         # int
    |   ID                          # id
    |   '(' expr ')'                # parens
	|	expr op=('*'|'/') expr      # MulDiv
    |   expr op=('+'|'-') expr      # AddSub
    ;
    
conditionalexpr:	expr op=('<'|'>'|'<='|'>=') expr    		#comparison
				|	expr op=('=='|'!=') expr					#equality
				|	expr op=('&&' | '||') expr					#conditionalANDOR
				;
    
value:  ID                           # idval
	 |  INT							 # intval
	 |	STRING                       # string
	 |	BOOL						 # bool
	 ;

	 
looping: 'loop' '(' ID '=' INT ';' conditionalexpr ';' ID '=' expr ')' '{' stat '}'	#loopcond
		;

	 
condition:	'if' '(' conditionalexpr ')' '{' stat '}'  # if
		 |  'if' '(' conditionalexpr ')' '{' stat '}'    
	        'else' '{' stat '}'             # ifelse
		 ;


MUL :   '*' ; // assigns token name to '*' used above in grammar
DIV :   '/' ;
ADD :   '+' ;
SUB :   '-' ;
NOT :	'!' ;
ID  :   [a-zA-Z][a-zA-Z0-9]* ;      // match identifiers
INT :   [0-9]+ ;         // match integers
STRING: '"'[.]*'"';
BOOL : ['True'| 'False'];
NEWLINE:'\r'? '\n' ;     // return newlines to parser (is end-statement signal)
WS  :   [\r\n\t]+ -> skip ; // toss out whitespace
LES:   '<' ;
GRT:  '>' ;
LESE:  '<=';
GRTE:  '>=';
EE:  '==';
NE:  '!=';
AND: '&&';
OR: '||';
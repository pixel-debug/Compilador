public class Tag {

 public final static int
    //Palavras reservadas
    START   = 256,
    EXIT    = 257,
    INT     = 258, //integer
    STR     = 259, //string
    FLOAT   = 260, //float
    IF      = 261,
    THEN    = 262,
		END     = 263,
		ELSE    = 264,
    DO 	    = 265,
    WHILE   = 266,
    SCAN    = 267,
    PRINT   = 268,
    NOT     = 269, 
    
    //Operadores e pontuação
    PV 	= (int)';',
		VRG = (int)',',
    PPV = 270, //PPV = ASSIGN
		AP 	= (int)'(',
		FP	= (int)')',
    FC  = (int)'!',
    EQ  = 271, // ==
    GT	= (int)'>',
    GE  = 272, // >=
    LT	= (int)'<',
    LE  = 273, // <=
    NE  = 274, // <>
    SUM = (int)'+',		
		MIN = (int)'-',
    OR  = (int)'||',
		MUL	= (int)'*',
		DIV = (int)'/',
    AND = (int)'&&',
    DOT = (int)'.',
    AC  = (int)'{',
    FC  = (int)'}',

		//Outros tokens
		NUM     = 300,
		ID      = 301,
		LIT     = 302,
    VOID    = 303,
		EOF     = 65535;
  }
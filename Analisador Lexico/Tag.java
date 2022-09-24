public enum Tag {
  // Palavras reservadas
  START, 
  EXIT,
  INT, // integer
  STRING, // string
  FLOAT, // float
  IF,
  THEN,
  END,
  ELSE,
  DO,
  WHILE,
  SCAN,
  PRINT,
  NOT,

  // Operadores e pontuação
  PV,   // ;
  VRG,  // ,
  AP,   // (
  FP,   // )
  AC,   // {
  FC,   // !
  EQ,   // ==
  GT,   // >
  GE,   // >=
  LT,   // <
  LE,   // <= 
  NE , // <>
  SUM,  // +
  MIN,  // -
  MUL,  // *
  DIV,  // /
  OR,   // "||",
  AND,  // "&&",
  DOT,  // .
  PPV,  // Assing
  

  // Outros tokens
  NUM,
  ID,
  LIT,
  VOID,
  EOF,

  // Especiais
  UNEXPECTED_EOF,
  INVALID_TOKEN,
  END_OF_FILE,

}
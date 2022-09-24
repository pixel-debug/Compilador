public enum Tag {
  // Palavras reservadas
  START,
  EXIT,
  INT, // integer
  STR, // string
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
  PV, //;
  VRG,
  PPV, // PPV IGN
  AP,
  FP,
  FC,
  EQ, // GT t)'>',
  GE, // > LT t)'<',
  LE, // < NE , // <>
  SUM,
  MIN,
  OR, // "||",
  MUL,
  DIV,
  AND, // "&&",
  DOT,
  AC,

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
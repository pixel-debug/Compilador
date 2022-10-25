package sintatico;

import lexico.*;

public class Parser {

  private Token token;
  private Token lastToken;
  private Lexico lexico;
  private Tag tag;
  //private SintaxError erro;

  public Parser(Lexico lexico) throws Exception {
    this.lexico = lexico;
    advance();
  }

  private void advance() throws Exception {
    System.out.println("lendo proximo token");
    lastToken=token;
    token = lexico.scan();
  }

  private void eat(Tag tag) throws Exception {
    if (token.tag == tag) {
      System.out.println("eat: " + token);
      advance();
    } else {
      error(token);
    }
  }

  public void error(Token token) {
    System.out.println(
      "Erro  na linha " +
      lexico.line +
      " próximo ao token '" +
      token.toString() +
      "'" + token
    );
    System.exit(0);
  }

  // program ::= start [decl-list] stmt-list exit
  public void program() throws Exception {
    switch (token.tag) {
      case START:
        eat(Tag.START);
        // aqui não lembro [ ] era opcional
        declList();
        stmtList();
        eat(Tag.EXIT);
        break;
      default:
        error(token);
        break;
    }
  }

  // decl-list ::= decl {decl}
  // ::= decl {";" decl}
  public void declList() throws Exception {
    System.out.println("Analisando DECL");
    while (verifydecl(token)) {
      decl();
      eat(Tag.PV);
    }
  }

  private boolean verifydecl(Token token){
    if(token.tag== Tag.INT||
    token.tag== Tag.FLOAT||
    token.tag== Tag.STRING) return true;
    return false;
  }

  // decl ::= type ident-list ";"
  public void decl() throws Exception {
    switch (token.tag) {
      case INT:
      case FLOAT:
      case STRING:
        type();
        identList();
        break;
      default:
        error(token);
        break;
    }
  }

  // ident-list ::= identifier {"," identifier}
  public void identList() throws Exception {
    switch (token.tag) {
      case ID:
        identifier();
        while (token.tag == Tag.VRG) {
          eat(Tag.VRG);
          identifier();
        }
        break;
      default:
        error(token);
        break;
    }
  }

  // type ::= int | float | string
  public void type() throws Exception {
    switch (token.tag) {
      case INT:
        eat(Tag.INT);
        break;
      case FLOAT:
        eat(Tag.FLOAT);
        break;
      case STRING:
        eat(Tag.STRING);
        break;
      default:
        error(token);
        break;
    }
  }

  // stmt-list ::= stmt {stmt}
  // ::= stmt {";" stmt}
  public void stmtList() throws Exception {
    System.out.println("Analisando STMT");
    while (verifystmt()) {
      stmt();
      eat(Tag.PV);
    }
  }

  private boolean verifystmt() {
    if(token.tag == Tag.ID ||
    token.tag == Tag.PRINT ||
    token.tag == Tag.SCAN ||
    token.tag == Tag.DO ||
    token.tag == Tag.WHILE ||
    token.tag == Tag.IF ||
    token.tag == Tag.ELSE) return true;
    return false;
  }

  // stmt ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt
  // ";"
  public void stmt() throws Exception {
    switch (token.tag) {
      case ID:
        assignStmt();
        break;
      case IF:
        ifStmt();
        break;
      case WHILE:
        whileStmt();
        break;
      case SCAN:
        readStmt();
        break;
      case PRINT:
        writeStmt();
        break;
      default:
        error(token);
        break;
    }
  }

  // assign-stmt ::= identifier "=" simple_expr
  public void assignStmt() throws Exception {
    switch (token.tag) {
      case ID:
        identifier();
        eat(Tag.PPV);
        simpleExpr();
        break;
      default:
        error(token);
        break;
    }
  }

  // if-stmt ::= if condition then stmt-list end | if condition then stmt-list
  // else stmt-list end
  public void ifStmt() throws Exception {
    switch (token.tag) {
      case IF:
        eat(Tag.IF);
        condition();
        eat(Tag.THEN);
        stmtList();
        switch (token.tag) {
          case END:
            eat(Tag.END);
            break;
          case ELSE:
            eat(Tag.ELSE);
            stmtList();
            eat(Tag.END);
            break;
          default:
            break;
        }
      default:
        error(token);
        break;
    }
  }

  // condition ::= expression
  public void condition() throws Exception {
    expression();
  }

  // while-stmt ::= do stmt-list stmt-sufix
  public void whileStmt() throws Exception {
    switch (token.tag) {
      case DO:
        eat(Tag.DO);
        stmtList();
        stmtSufix();
        break;
      default:
        error(token);
        break;
    }
  }

  // stmt-sufix ::= while condition end
  public void stmtSufix() throws Exception {
    switch (token.tag) {
      case WHILE:
        eat(Tag.WHILE);
        condition();
        eat(Tag.END);
        break;
      default:
        error(token);
        break;
    }
  }

  // read-stmt ::= scan "(" identifier ")"
  public void readStmt() throws Exception {
    switch (token.tag) {
      case SCAN:
        eat(Tag.SCAN);
        eat(Tag.AP);
        identifier();
        eat(Tag.FP);
        break;
      default:
        error(token);
        break;
    }
  }

  // write-stmt ::= print "(" writable ")"
  public void writeStmt() throws Exception {
    switch (token.tag) {
      case PRINT:
        eat(Tag.PRINT);
        eat(Tag.AP);
        writable();
        eat(Tag.FP);
        break;
      default:
        error(token);
        break;
    }
  }

  // writable ::= simple-expr | literal
  public void writable() throws Exception {
    switch (token.tag) {
      case ID:
      case NUM:
      case AC:
      case AP:
      case FC:
      case MIN:
        simpleExpr();
        break;
      case STRING:
        literal();
        break;
      default:
        error(token);
        break;
    }
  }

  // expression ::= simple-expr | simple-expr relop simple-expr
  public void expression() throws Exception {
    switch (token.tag) {
      case ID:
      case NUM:
      case AC:
      case AP:
      case FC:
      case MIN:
        simpleExpr();
        if (token.tag == Tag.EQ ||
            token.tag == Tag.GT ||
            token.tag == Tag.GE ||
            token.tag == Tag.LT ||
            token.tag == Tag.LE ||
            token.tag == Tag.NE) {
          relop();
          simpleExpr();
        }
        break;
      default:
        error(token);
        break;
    }
  }

  // simple-expr ::= term | simple-expr addop term
  public void simpleExpr() throws Exception {
    switch (token.tag) {
      case ID:
      case NUM:
      case AC:
      case AP:
      case FC:
      case MIN:
        term();
        if (token.tag == Tag.SUM ||
            token.tag == Tag.MIN ||
            token.tag == Tag.OR) {
          addop();
          term();
        }
        break;
      default:
        error(token);
        break;
    }
  }

  // term ::= factor-a | term mulop factor-a
  public void term() throws Exception {
    switch (token.tag) {
      case ID:
      case NUM:
      case AC:
      case AP:
      case FC:
      case MIN:
        factorA();
        if (token.tag == Tag.MUL ||
            token.tag == Tag.DIV ||
            token.tag == Tag.AND) {
          mulop();
          factorA();
        }
        break;
      default:
        error(token);
        break;
    }
  }



  // fator-a ::= factor | "!" factor | "-" factor
  public void factorA() throws Exception {
    switch (token.tag) {
      case AP:
        factor();
        break;
      case FC:
        eat(Tag.FC);
        factor();
        break;
      case MIN:
        eat(Tag.MIN);
        factor();
        break;
      case ID:
      case NUM:
        factor();
        break;
      default:
        error(token);
        break;
    }
  }

  // factor ::= identifier | constant | "(" expression ")"
  public void factor() throws Exception {
    switch (token.tag) {
      case ID:
        identifier();
        break;
      case NUM:
        constant();
        break;
      case AP:
        eat(Tag.AP);
        expression();
        eat(Tag.FP);
        break;
      default:
        error(token);
        break;
    }
  }

  // relop ::= "==" | ">" | ">=" | "<" | "<=" | "<>"
  public void relop() throws Exception {
    switch (token.tag) {
      case EQ:
        eat(Tag.EQ);
        break;
      case GT:
        eat(Tag.GT);
        break;
      case GE:
        eat(Tag.GE);
        break;
      case LT:
        eat(Tag.LT);
        break;
      case LE:
        eat(Tag.LE);
        break;
      case NE:
        eat(Tag.NE);
        break;
      default:
        error(token);
        break;
    }

  }

  // addop ::= "+" | "-" | "||"
  public void addop() throws Exception {
    switch (token.tag) {
      case SUM:
        eat(Tag.SUM);
        break;
      case MIN:
        eat(Tag.MIN);
        break;
      case OR:
        eat(Tag.OR);
        break;
      default:
        error(token);
        break;
    }
  }

  // mulop ::= "*" | "/" | "&&"
  public void mulop() throws Exception {
    switch (token.tag) {
      case MUL:
        eat(Tag.MUL);
        break;
      case DIV:
        eat(Tag.DIV);
        break;
      case AND:
        eat(Tag.AND);
        break;
      default:
        error(token);
        break;
    }
  }

  // constant ::= integer_const | float_const | literal
  public void constant() throws Exception {
    switch (token.tag) {
      case NUM:
        num_const();
        break;
      case STRING:
        literal();
        break;
      default:
        error(token);
        break;
    }
  }

  // const ::= digit+
  public void num_const() throws Exception {
    switch (token.tag) {
      case NUM:
        eat(Tag.NUM);
        break;
      default:
        error(token);
        break;
    }
  }


  // literal ::= " { " {caractere} " } "
  public void literal() throws Exception {
    switch (token.tag) {
      case STRING:
        letter();
        break;
      default:
        error(token);
        break;
    }
  }

  // identifier ::= (letter | _ ) (letter | digit )*
  public void identifier() throws Exception {
      switch (token.tag) {
        case ID:
          // VERIFICAR TABELA SIMBOLOS
          eat(Tag.ID);
          break;
        default:
          error(token);
      }
  }

  // letter ::= [A-za-z]
  public void letter() throws Exception {
      switch (token.tag) {
        case STRING:
          eat(Tag.STRING);
          break;
        default:
          error(token);
      }
  }

  //INUTIL // digit ::= [0-9]
  public void digit() throws Exception {
    switch (token.tag) {
      case INT:
        eat(Tag.INT);
        break;
      case FLOAT:
        eat(Tag.FLOAT);
        break;
      default:
        error(token);
        break;
    }
  }

  // caractere ::= um dos caracteres ASCII, exceto quebra de linha

}
package sintatico;

import java.io.IOException;
import java.util.ArrayList;
import lexico.*;
import sintatico.error;

public class parser {

  private Token token;
  private Lexico lexico;
  private Tag tag;
  private error erro;

  public parser(ArrayList<Token> tokenList) {
    this.lexico = lexico;
  }

  private void advance() throws Exception {
    System.out.println("lendo proximo token");
    token = lexico.scan();
  }

  private void eat(Tag tag) throws Exception {
    if (token.tag == tag) {
      System.out.println("eat: " + token);
      advance();
    } else {
      erro.error();
    }
  }

  //program ::= start   [decl-list]   stmt-list  exit
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
        erro.error();
        break;
    }
  }

  //decl-list  ::= decl {decl}
  //           ::= decl {";" decl}
  public void declList() throws Exception {
    decl();
    while (token.tag == tag.PV) {
      eat(tag.PV);
      decl();
    }
  }

  //decl  ::= type ident-list ";"
  public void decl() throws Exception {
    switch (token.tag) {
      case INT:
      case FLOAT:
      case STRING:
        type();
        identList();
        eat(tag.PV);
        break;
      default:
        erro.error();
        break;
    }
  }

  //ident-list  ::= identifier {"," identifier}
  public void identList() throws Exception {
    switch (token.tag) {
      case ID:
        identifier();
        while (token.tag == tag.VRG) {
          eat(tag.VRG);
          identifier();
        }
        break;
      default:
        erro.error();
        break;
    }
  }

  //type  ::= int | float  | string
  public void type() throws Exception {
    switch (token.tag) {
      case INT:
        eat(tag.INT);
        break;
      case FLOAT:
        eat(tag.FLOAT);
        break;
      case STRING:
        eat(tag.STRING);
        break;
      default:
        erro.error();
        break;
    }
  }

  //stmt-list  ::= stmt {stmt}
  //           ::= stmt {";" stmt}
  public void stmtList() throws Exception {
    stmt();
    while (token.tag == tag.PV) {
      eat(tag.PV);
      stmt();
    }
  }

  //stmt  ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt ";"
  public void stmt() throws Exception {
    switch (token.tag) {
      case PPV:
        assignStmt();
        eat(tag.PV);
        break;
      case IF:
        ifStmt();
        break;
      case WHILE:
        whileStmt();
        break;
      case SCAN:
        readStmt();
        eat(tag.PV);
        break;
      case PRINT:
        writeStmt();
        eat(tag.PV);
        break;
      default:
        erro.error();
        break;
    }
  }

  //assign-stmt  ::= identifier   "="   simple_expr
  public void assignStmt() throws Exception {
    switch (token.tag) {
      case ID:
        eat(tag.ID);
        identifier();
        eat(tag.PPV);
        simpleExpr();
        break;
      default:
        erro.error();
        break;
    }
  }

  //if-stmt  ::=  if condition then stmt-list end | if condition then stmt-list else stmt-list end
  public void ifStmt() throws Exception {
    switch (token.tag) {
      case IF:
        eat(tag.IF);
        condition();
        eat(tag.THEN);
        stmtList();
        switch (token.tag) {
          case END:
            eat(tag.END);
            break;
          case ELSE:
            eat(tag.ELSE);
            stmtList();
            eat(tag.END);
            break;
          default:
            break;
        }
      default:
        erro.error();
        break;
    }
  }

  //condition  ::= expression
  public void condition() throws Exception {
    expression();
  }

  //while-stmt  ::= do stmt-list stmt-sufix
  public void whileStmt() throws Exception {
    switch (token.tag) {
      case DO:
        eat(tag.DO);
        stmtList();
        stmtSufix();
        break;
      default:
        erro.error();
        break;
    }
  }

  //stmt-sufix  ::= while condition end
  public void stmtSufix() throws Exception {
    switch (token.tag) {
      case WHILE:
        eat(tag.WHILE);
        condition();
        eat(tag.END);
        break;
      default:
        erro.error();
        break;
    }
  }

  //read-stmt  ::= scan "(" identifier ")"
  public void readStmt() throws Exception {
    switch (token.tag) {
      case SCAN:
        eat(tag.SCAN);
        eat(tag.AP);
        identifier();
        eat(tag.FP);
        break;
      default:
        erro.error();
        break;
    }
  }

  //write-stmt ::= print "(" writable ")"
  public void writeStmt() throws Exception {
    switch (token.tag) {
      case PRINT:
        eat(tag.PRINT);
        eat(tag.AP);
        writable();
        eat(tag.FP);
        break;
      default:
        erro.error();
        break;
    }
  }

  //writable ::= simple-expr | literal
  public void writable() throws Exception {
    switch (token.tag) {
      case ID:
      case INT:
      case FLOAT:
      case AC:
      case AP:
      case FC:
      case MIN:
        simpleExpr();
        break;
      default:
        erro.error();
        break;
    }
  }

  //expression ::= simple-expr | simple-expr relop simple-expr
  public void expression() throws Exception {
    switch (token.tag) {
      case ID:
      case INT:
      case FLOAT:
      case AC:
      case AP:
      case FC:
      case MIN:
        simpleExpr();
        if (
          token.tag == tag.EQ ||
          token.tag == tag.GT ||
          token.tag == Tag.GE ||
          token.tag == Tag.LT ||
          token.tag == Tag.LE ||
          token.tag == tag.NE
        ) {
          relop();
          simpleExpr();
        }
        break;
      default:
        erro.error();
        break;
    }
  }

    //simple-expr  ::= term   | simple-expr   addop   term 
    public void simpleExpr() throws Exception {
      switch (token.tag) {
        case ID:
        case INT:
        case FLOAT:
        case AC:
        case AP:
        case FC:
        case MIN:
          term();
          if (
            token.tag == tag.SUM ||
            token.tag == tag.MIN ||
            token.tag == Tag.OR
          ) {
            addop();
            term();
          }
          break;
        default:
          erro.error();
          break;
      }
    }

//term  ::= factor-a   |   term   mulop   factor-a
public void term() throws Exception {
    switch (token.tag) {
      case ID:
      case INT:
      case FLOAT:
      case AC:
      case AP:
      case FC:
            case MIN:
              factorA();
              if (
                token.tag == tag.MUL ||
                token.tag == tag.DIV ||
                token.tag == Tag.AND ||
              ) {
                mulop();
                factorA();
              }
              break;
            default:
              erro.error();
              break;
          }
        }
}

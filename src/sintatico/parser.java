package sintatico;

import java.io.IOException;
import java.util.ArrayList;
import lexico.*;

public class parser {

  private Token token;
  private Lexico lexico;
  private Tag tag;

  public parser(ArrayList<Token> tokenList) {
    this.lexico = lexico;
  }

  private void advance() {
    System.out.println("lendo proximo token");
    token = lexico.scan();
  }

  private void eat(Tag tag) {
    if (token.tag == tag) {
      System.out.println("eat: " + token);
      advance();
    } else {
      System.out.println("SOBE ERRO");
    }
  }

  //program ::= start   [decl-list]   stmt-list  exit
  public void program() {
    switch (token.tag) {
      case START:
        eat(Tag.START);
        // aqui não lembro [ ] era opcional
        declList();
        stmtList();
        eat(Tag.EXIT);
        break;
      default:
        System.out.println("SOBE ERRO");
        break;
    }
  }

  //decl-list  ::= decl {decl}
  public void declList() {
    decl();
    while (condition) {
      // { } é while, mas como?
    }
  }

  //decl  ::= type ident-list ";"
  public void decl() {
    switch (token.tag) {
      case INT:
      case FLOAT:
      case STRING:
        type();
        identList();
        eat(tag.PV);
        break;
      default:
        System.out.println("SOBE ERRO");
        break;
    }
  }

  //ident-list  ::= identifier {"," identifier}
  public void identList() {
    switch (token.tag) {
      case ID:
        identifier();
        while (token.tag == tag.VRG) {
          eat(tag.VRG);
          identifier();
        }
        break;
      default:
        System.out.println("SOBE ERRO");
        break;
    }
  }

  //type  ::= int | float  | string
  public void type() {
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
        System.out.println("SOBE ERRO");
        break;
    }
  }

  //stmt-list  ::= stmt {stmt}
  public void stmtList() {
    stmt();
    while (condition) {}
  }

  //stmt  ::= assign-stmt ";" | if-stmt | while-stmt | read-stmt ";" | write-stmt ";"
  public void stmt() {
    switch (token.tag) {
      case PPV:
        assignStmt();
        eat(tag.PV);
        break;
      case IF:
        ifStmt();
        eat(tag.PV);
        break;
      case WHILE:
        whileStmt();
        eat(tag.PV);
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
        System.out.println("SOBE ERRO");
        break;
    }
  }

  //assign-stmt  ::= identifier   "="   simple_expr
  public void assignStmt() {
    switch (token.tag) {
      case ID:
        eat(tag.ID);
        identifier();
        eat(tag.PPV);
        simpleExpr();
        break;
      default:
        System.out.println("SOBE ERRO");
        break;
    }
  }

  //if-stmt  ::=  if condition then stmt-list end | if condition then stmt-list else stmt-list end
  public void ifStmt() {
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
        System.out.println("SOBE ERRO");
        break;
    }
  }
  //condition  ::= expression
}

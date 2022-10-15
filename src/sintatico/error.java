package sintatico;

import lexico.Lexico;
import lexico.Token;

public class error {

  private Token token;
  private Lexico lexico;

  public void error() {
    System.out.println(
      "Erro  na linha " +
      lexico.line +
      " próximo ao token '" +
      token.toString() +
      "'"
    );
    System.exit(0);
  }
}

package sintatico;

import lexico.Lexico;
import lexico.Token;

public class SintaxError {

  private Lexico lexico;

  public void error(Token token) {
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

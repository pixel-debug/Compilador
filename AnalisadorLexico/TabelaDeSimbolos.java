import AnalisadorLexico.Token;
import java.util.*;

public class TabelaDeSimbolos {

  // Tabela de símbolos do ambiente.

  private HashMap<Token, Id> table;

  protected TabelaDeSimbolos prev;

  public TabelaDeSimbolos() {
    // cria a TS para o ambiente
    this.table = new HashMap();
    this.prev = null;
  }

  public TabelaDeSimbolos(TabelaDeSimbolos n) {
    // cria a TS para o ambiente
    this.table = new HashMap();

    // associa o ambiente atual ao anterior
    this.prev = n;
  }

  public void put(Token w, Id i) {
    this.table.put(w, i);
  }

  public Id get(Token w) {
    return table.get(w);
  }

  public boolean has(Token w) {
    return table.containsKey(w);
  }

  @Override
  public String toString() {
    String symbols = "";
    for (Token t : table.keySet()) {
      Id id = table.get(t);
      symbols += t + " -> " + id.toString() + "\n";
    }
    return symbols;
  }

}
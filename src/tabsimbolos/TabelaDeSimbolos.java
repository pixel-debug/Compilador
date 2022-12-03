package tabsimbolos;
import java.util.*;

import lexico.*;

/*
 * Estrutura: <Token, Id(Lexema,tipo)> 
 */

public class TabelaDeSimbolos {

  // Tabela de símbolos do ambiente.

  private Hashtable<Token, Id> table;

  protected TabelaDeSimbolos prev;

  public TabelaDeSimbolos() {
    // cria a TS para o ambiente
    this.table = new Hashtable();
    this.prev = null;
  }

  public TabelaDeSimbolos(TabelaDeSimbolos n) {
    // cria a TS para o ambiente
    this.table = new Hashtable();

    // associa o ambiente atual ao anterior
    this.prev = n;
  }

  public void setTable(Hashtable<Token, Id> table) {
    this.table = table;
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
      symbols += t + ": "+ t.getValue() + " -> " + id.toString() + "\n";
    }
    return symbols;
  }

  public Hashtable getTable(){
    return table;
  }
}

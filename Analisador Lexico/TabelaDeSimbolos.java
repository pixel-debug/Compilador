import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TabelaDeSimbolos {

  private Hashtable table; // tabela de símbolos do ambiente
  private TabelaDeSimbolos prev;

  public TabelaDeSimbolos() {
    this.table = new Hashtable();
  }

  /* Este método insere uma entrada na TS do ambiente */
  /* A chave da entrada é o Token devolvido pelo analisador léxico */
  /* Id é uma classe que representa os dados a serem armazenados na TS para */
  /* identificadores */

  public void put(Token w, int id) {
    table.put(w, id);
  }

  /* Este método retorna as informações (Id) referentes a determinado Token */
  /* O Token é pesquisado do ambiente atual para os anteriores */
  public int get(Token w) {
    int aux;
    for (TabelaDeSimbolos s = this; s != null; s = s.prev) {
      if (s.table.get(w) != null) {
        aux = (int) s.table.get(w);
        return aux;
      }
    }
    return 0;
  }

  public TabelaDeSimbolos getInstance() {
    if (prev == null) {
      prev = new TabelaDeSimbolos();
      return prev;
    }
    return prev;
  }

  public void imprimirTabela() {
    System.out.println("\nTabela de Símbolos:\n");
    Set set = table.entrySet();
    Iterator it = set.iterator();
    while (it.hasNext()) {
      // HashMap<K, V>.Entry<Token,String> entry = (HashMap.Entry<Token, String)
      // it.next();
    }
  }
}

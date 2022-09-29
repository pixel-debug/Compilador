import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import TabelaDeSimbolos.TabelaDeSimbolos;

public class Compilador {

  public static void main(String[] args) throws Exception {
    private TabelaDeSimbolos symbols;
    boolean boo;
    try {
      JFileChooser chooser = new JFileChooser();
      int retorno = chooser.showOpenDialog(null);

      if (retorno == JFileChooser.APPROVE_OPTION) {
        Lexico lexer = new Lexico(chooser.getSelectedFile().toString());
        this.symbols = new TabelaDeSimbolos();

        // for para percorrer o arquivo
        Scanner input = new Scanner(chooser.getSelectedFile());
        boo = input.hasNextLine();
        while (boo) {
          Token token = lexer.scan();
          if (token.getToken().equals(Tag.EOF))
            boo = false;
          System.out.println("<" + token + "," + token.getToken() + ">");
        }
        System.out.println(lexer.getLine());
        System.out.println(lexer.getWords());

        System.out.println("\n\nTabela de símbolos:");
        System.out.println(this.symbols.toString());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}

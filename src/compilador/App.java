package compilador;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFileChooser;

import lexico.*;
import tabsimbolos.TabelaDeSimbolos;

public class App {

  public static void main(String[] args) throws Exception {
    TabelaDeSimbolos symbols;
    boolean boo;
    try {
      JFileChooser chooser = new JFileChooser();
      int retorno = chooser.showOpenDialog(null);

      if (retorno == JFileChooser.APPROVE_OPTION) {
        symbols = new TabelaDeSimbolos();
        Lexico lexer = new Lexico(chooser.getSelectedFile().toString(),symbols);

        // for para percorrer o arquivo
        Scanner input = new Scanner(chooser.getSelectedFile());
        boo = input.hasNextLine();
        while (boo) {
          Token token = lexer.scan();
          if (token.getToken().equals(Tag.EOF))
            boo = false;
          if(token.getToken()!=Tag.INVALID_TOKEN)
            System.out.println("<" + token + "," + token.getToken() + ">");
          else
            System.out.println("Analisador Lexico: Token invalido na linha "+lexer.getLine());
        }
        System.out.println("Total de linhas: "+lexer.getLine());

        System.out.println("\n\nTabela de símbolos:");
        System.out.println(symbols.toString());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
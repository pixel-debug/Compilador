import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class Compilador {

  public static void main(String[] args) throws IOException {
    boolean boo;
    try {
      JFileChooser chooser = new JFileChooser();
      int retorno = chooser.showOpenDialog(null);

      if (retorno == JFileChooser.APPROVE_OPTION) {
        Lexico lexer = new Lexico(chooser.getSelectedFile().toString());
        //for para percorrer o arquivo
        Scanner input = new Scanner(chooser.getSelectedFile());
        boo = input.hasNextLine();
        while (boo) {
          String token = lexer.scan().toString();
          if (token.equals("65535")) boo = false;
          System.out.println(token);
        }
        System.out.println(lexer.getWords());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}

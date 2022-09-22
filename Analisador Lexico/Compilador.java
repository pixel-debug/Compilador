import javax.swing.JFileChooser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class Compilador {

    public static void main(String[] args) throws IOException {

        try {
            JFileChooser chooser = new JFileChooser();
            int retorno = chooser.showOpenDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {
                Lexico lexer = new Lexico(chooser.getSelectedFile().toString());
                //for para percorrer o arquivo
                Scanner input = new Scanner(chooser.getSelectedFile());
                while (input.hasNextLine()) {
                    String token = lexer.scan().toString();
                    System.out.println(token);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        

    }
}
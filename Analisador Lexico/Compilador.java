import javax.swing.JOptionPane;
import java.io.File;
import java.util.Scanner;


public class Compiler {

    public static void main(String[] args) throws IOException {

        try {
            JFileChooser chooser = new JFileChooser();
            int retorno = chooser.showOpenDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {
                Lexico lexer = new Lexico(chooser.getSelectedFile());
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
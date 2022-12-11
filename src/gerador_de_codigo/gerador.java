package gerador_de_codigo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import lexico.*;

public class gerador {
    private String codigo;
    private Token token;

    public gerador(){
        codigo = " ";
        // o gerador vai pegar o código fonte e levar pro assembly
    }

    public void geraCodigo(){
        PrintWriter write = null;
        try{
            write = new PrintWriter("codigoIntermediario.txt");
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        write.write(codigo);
        write.close();
    }
}


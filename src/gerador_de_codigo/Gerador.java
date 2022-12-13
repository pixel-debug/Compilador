package gerador_de_codigo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.sound.midi.Soundbank;

import lexico.*;

public class Gerador {
    private String codigo;
    private Token token;
    private String variavel;
    private String variavel2;

    private Lexico lexico;
    private int linha;
    private String destino;

    public Gerador(Lexico lexico) {
        codigo = "";
        this.lexico = lexico;
        // o gerador vai pegar o código fonte e levar pro assembly
    }

    private void geraCodigo() {
        PrintWriter write = null;
        try {
            write = new PrintWriter("codigoIntermediario.asm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        write.write(codigo);
        write.close();
    }

    public void comeco() throws Exception {
        token = lexico.scan(); // come o start
        declaracao();
        stmt();
        fim();
    }

    private void declaracao() throws Exception {
        while (token.tag == Tag.INT || token.tag == Tag.FLOAT || token.tag == Tag.STRING) {
            while (token.tag != Tag.PV) {
                token = lexico.scan();
                if (token.tag == Tag.ID) {
                    variavel = token.toString();
                    System.out.println("primeira variavel = " + variavel);
                }
            }
        }
        token = lexico.scan();
    }

    private void stmt() throws Exception {
        System.out.println(token);
        switch (token.tag) {
            case ID:
                assign();
                break;
            case IF:
                se();
                codigo += "\n";
                break;
            case DO:
                enquanto();
                codigo += "\n";
                break;
            case SCAN:
                le();
                break;
            case PRINT:
                escreve();
                break;
            default:
                break;
        }
    }

    private void assign() throws Exception {
        System.out.println("ASSIGN");
        System.out.println("entrou no assign = " + token);
        variavel = token.toString();
        token = lexico.scan(); // come o PPV
        variavel2 = lexico.scan().toString();
        codigo += "\tlw \t\t" + variavel + " " + variavel2 + "\n";
    }

    private void se() throws Exception {
        codigo += "\n";
        System.out.println("\nIF");
        token = lexico.scan(); // come o if
        token = lexico.scan(); // come o (
        System.out.println(token);
        condicao();
        System.out.println("final do if = "+token);
        token = lexico.scan();

        while (token.tag == Tag.ID || token.tag == Tag.PRINT ||
                token.tag == Tag.SCAN || token.tag == Tag.DO || token.tag == Tag.IF) {
            stmt();
            token = lexico.scan();

        }
        System.out.println("depois do then= "+token);
        token = lexico.scan();
        System.out.println(token);
        if (token.tag == Tag.ELSE) {
            codigo += "\tjump \n";
            token = lexico.scan();
            stmt();
        }

    }

    private void enquanto() throws Exception {
        System.out.println("\nDO");
        codigo += "loop " + "\n";
        token = lexico.scan(); // come o DO
        System.out.println("comi o do, o proximo é = " + token);
        stmt();
        token = lexico.scan();
        while (token.tag == Tag.ID || token.tag == Tag.PRINT ||
                token.tag == Tag.SCAN || token.tag == Tag.DO || token.tag == Tag.IF) {
            stmt();
            token = lexico.scan();

        }
        token = lexico.scan();
        System.out.println("token do while " + token);
        condicao();
        token = lexico.scan(); // come o END
        codigo += "jump " + "\n";
        System.out.println("ultimo do DO " + token);
    }

    private void condicao() throws Exception {
        System.out.println("Condicao = " + token);
        variavel2 = token.toString();
        simpleExpr();
        
        switch (token.tag) {
            case EQ:
                variavel = lexico.scan().toString();
                codigo += "\tcmp \t" + variavel + " " + variavel2 + "\n";
                codigo += "\tje "+destino +"\n";
                break;
            case GE:
                variavel = lexico.scan().toString();
                codigo += "\tcmp " + "\tt1 " + " " + variavel + " " + variavel2 + "\n";
                codigo += "\tjge "+destino +"\n";
                break;
            case GT:
                variavel = lexico.scan().toString();
                codigo += "\tcmp " + "\tt1 " + " " + variavel + " " + variavel2 + "\n";
                codigo += "\tjg "+destino +"\n";
                break;
            case NE:
                variavel = lexico.scan().toString();
                codigo += "\tcmp " + variavel + " " + variavel2 + "\n";
                codigo += "\tjne "+destino +"\n";
                break;
            case LT:
                variavel = lexico.scan().toString();
                codigo += "\tcmp " + "\tt1 " + " " + variavel + " " + variavel2 + "\n";
                codigo += "\tjl "+destino +"\n";
                break;
            case LE:
                variavel = lexico.scan().toString();
                codigo += "\tcmp " + "\tt1 " + " " + variavel + " " + variavel2 + "\n";
                codigo += "\tjle "+destino +"\n";
                break;
            default:
                break;
        }

        token = lexico.scan();
        simpleExpr();

    }

    private void expressao() {
        System.out.println("nada aqui");

    }
    // o scan tá OK
    private void le() throws Exception {
        System.out.println("\nSCAN");
        token = lexico.scan(); // come o scan
        token = lexico.scan(); // come o (
        variavel = token.toString();
        codigo += "scan\n";
        if (token.tag == Tag.LIT) {
            codigo += "\tpush \t\t" + variavel + "\n";
            codigo += "\tcall \t\tscan\n";
        } else {
            codigo += "\tatoi \t\t" + variavel + "\n";
            codigo += "\tcall \t\tscan\n";
        }
        token = lexico.scan(); // come o )
        token = lexico.scan(); // come o pv
    }

    // o print ta OK
    private void escreve() throws Exception {
        System.out.println("\nPRINT");
        token = lexico.scan(); // come o print
        token = lexico.scan(); // come o (

        variavel = token.toString();
        simpleExpr();
        codigo += "print \t\t\t" + variavel + "\n";
        token = lexico.scan(); // come a variavel
    }

    private void simpleExpr() throws Exception {
        System.out.println("token que entrou no simpl = " + token);
        factor();
             
        switch(token.tag){
            case SUM:
                variavel = lexico.scan().toString();// come o +
                if (token.tag != Tag.STRING)
                    codigo += "\tadd " + "\tt0 " + " " + variavel2 + " " + variavel + "\n";
                else
                    codigo += "\tconcat " + "\tt0 " + " " + variavel2 + " " + variavel + "\n";
                break;
            case MIN:
                variavel = lexico.scan().toString();
                codigo += "\tsub " + "\tt0 " + " " + variavel2 + " " + variavel + "\n";
                break;  
            case OR:
                token = lexico.scan(); // come o OR
                codigo += "\tor \n";
                break;
            case AND:
                token = lexico.scan();
                codigo += "\tand \n";
                break;
            case MUL:
                variavel = lexico.scan().toString();// come o MUL
                codigo += "\tmul " + "\tt0 " + " " + variavel2 + " " + variavel + "\n";
                break;
            case DIV:
                variavel = lexico.scan().toString();// come o DIV
                codigo += "\tdiv " + "\tt0 " + " " + variavel2 + " " + variavel + "\n";
                break;
            default:
                break;
        }
        factor();
    }

    private void factor()throws Exception {
        switch(token.tag){
            case ID:
            case NUM:
            case LIT:
                variavel2 = token.toString();
                token = lexico.scan();
                break;
            case AP:
                token = lexico.scan();
                condicao();
                token = lexico.scan();
            default:
                break;
        }
    }

    private void fim() {
        System.out.println("gerando código em assembly");
        geraCodigo();
    }
}

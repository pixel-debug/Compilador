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
    private int destino;

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
        token = lexico.scan(); // come o PPV
        codigo += "\tlw \t\t" + variavel + " " + token + "\n";
    }

    private void se() throws Exception {
        System.out.println("\nIF");
        token = lexico.scan(); // come o if
        token = lexico.scan(); // come o (
        System.out.println(token);
        condicao();
        System.out.println(token);
        token = lexico.scan(); // come o )

        while (token.tag == Tag.ID || token.tag == Tag.PRINT ||
                token.tag == Tag.SCAN || token.tag == Tag.DO || token.tag == Tag.IF) {
            stmt();
        }
        token = lexico.scan();
        if (token.tag == Tag.ELSE) {
            codigo += "jump \n";
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
        simpleExpr();
        System.out.println("var ="+variavel2);
        switch (token.tag) {
            case EQ:
                variavel = lexico.scan().toString();
                codigo += "\t\tbeq \t" + variavel2 + " " + variavel + "\n";
                break;
            case GE:
                variavel = lexico.scan().toString();
                codigo += "\t\tslt " + "\tt1 " + " " + variavel2 + " " + variavel + "\n";
                codigo += "\t\tbeq " + "\tt1 $zero" + "\n";
                break;
            case GT:
                variavel = lexico.scan().toString();
                codigo += "\t\tslt " + "\tt1 " + " " + variavel2 + " " + variavel + "\n";
                codigo += "\t\tbne " + "\tt1 $zero" + "\n";
                break;
            case NE:
                variavel = lexico.scan().toString();
                codigo += "greater " + variavel2 + " " + variavel + "\n";
                codigo += "je " + "\n";
                break;
            case LT:
                variavel = lexico.scan().toString();
                codigo += "\t\tslt " + "\tt1 " + " " + variavel + " " + variavel2 + "\n";
                codigo += "\t\tbne " + "\tt1 $zero" + "\n";
                break;
            case LE:
                variavel = lexico.scan().toString();
                codigo += "\t\tslt " + "\tt1 " + " " + variavel + " " + variavel2 + "\n";
                codigo += "\t\tbeq " + "\tt1 $zero" + "\n";
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

    private void le() throws Exception {
        System.out.println("\nSCAN");
        token = lexico.scan(); // come o scan
        token = lexico.scan(); // come o (
        System.out.println("token do scan " + token);
        variavel = token.toString();
        if (token.tag == Tag.LIT) {
            codigo += "\tpush \t\t" + variavel + "\n";
            codigo += "\tcall scan\n";
        } else {
            codigo += "\tatoi \t\t" + variavel + "\n";
            codigo += "\tcall scan\n";
        }
        token = lexico.scan(); // come o )
        token = lexico.scan(); // come o pv

        System.out.println("ultimo token do scan = " + token);
    }

    // o print ta OK
    private void escreve() throws Exception {
        System.out.println("\nPRINT");
        token = lexico.scan(); // come o print
        token = lexico.scan(); // come o (
        System.out.println("token do print = " + token);
        variavel = token.toString();
        simpleExpr();

        System.out.println("variavel do print = " + variavel);

        codigo += "\tprint \t\t" + variavel + "\n";
        token = lexico.scan(); // come a variavel

        System.out.println("ultimo token do print = " + token);
    }

    private void simpleExpr() throws Exception {
        System.out.println("token que entrou no simpl = " + token);
        factor();
        if (token.tag == Tag.SUM) {
            variavel = lexico.scan().toString();// come o +
            if (token.tag != Tag.STRING)
                codigo += "add " + "t0 " + " " + token + " " + variavel + "\n";
            else
                codigo += "concat " + "t0 " + " " + token + " " + variavel + "\n";
        } else if (token.tag == Tag.MIN) {// come o -
            variavel = lexico.scan().toString();
            codigo += "sub " + "t0 " + " " + token + " " + variavel + "\n";
        } else if (token.tag == Tag.OR) {
            variavel = lexico.scan().toString(); // come o OR
            codigo += "or " + "t0 " + " " + token + " " + variavel + "\n";
        } else if (token.tag == Tag.AND) {
            variavel = lexico.scan().toString();// come o AND
            codigo += "and " + "t0 " + " " + token + " " + variavel + "\n";
        } else if (token.tag == Tag.MUL) {
            variavel = lexico.scan().toString();// come o MUL
            codigo += "mul " + "t0 " + " " + token + " " + variavel + "\n";
        } else if (token.tag == Tag.DIV) {
            variavel = lexico.scan().toString();// come o DIV
            codigo += "div " + "t0 " + " " + token + " " + variavel + "\n";
        }
        factor();
    }

    private void factor()throws Exception {
        switch(token.tag){
            case ID:
                variavel2 = token.toString();

                codigo += "\tpushin " +token.toString() + "\n";
                token = lexico.scan();
                break;
            case NUM:
                variavel2 = token.toString();

                codigo += "\tpushin " +token.toString() + "\n";
                token = lexico.scan();
                break;
            case LIT:
                variavel2 = token.toString();

                codigo += "\tpushin " +token.toString() + "\n";
                token = lexico.scan();
                break;
            case AP:
                token = lexico.scan();
                expressao();
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

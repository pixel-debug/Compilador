package semantico;

import java.util.*;

import lexico.*;
import tabsimbolos.*;
import sintatico.*;

/* 
    - corrigir tabela de simbolos
    - implementar concatenação
    - resultado final da expressao
    - métodos para setar a currentExpression
    - chamar no sintátic
*/

public class Semantico {
    private Hashtable table = new Hashtable();
    private Tag result;
    private String currentType;
    private Tag currentExpression;
    private Lexico lexico;
    private TabelaDeSimbolos symbols;

    public Semantico() {
        result = Tag.VOID;
        currentType = " ";
        currentExpression = Tag.VOID;
        this.symbols = symbols;
    //    this.table = symbols.getTable();
    }

    






    /*
     * SEMÂNTICO PARA OS IDENTIFICADORES
     */


    /* Operações para os identificadores */
    private void errorId(int line) {
        System.out.println("Error: identificador não declarado na linha " + line + ". Abortando ...");
        System.exit(0);
    }

    // Operações para a tabela de símbolos
    private String typeToString(Token token) {
        String type = "STRING";
        if (token.tag == Tag.FLOAT)
            type = "FLOAT";
        else if (token.tag == Tag.INT)
            type = "INTEGER";
        return type;
    }

    // atualiza o tipos na tabela
    // verifica se há duas ocorrências iguais
    public void updateSimbolTable(Token token, Token lasToken, int line) {
        if (table.containsKey(token)) {
            if (lasToken.tag == Tag.INT || lasToken.tag == Tag.FLOAT || lasToken.tag == Tag.STRING) {
                System.out.println("Error: redefinition of '" + token.toString() + "' na linha " + line);
                System.exit(0);
            }
        } else {
            if (lasToken.tag == Tag.INT || lasToken.tag == Tag.FLOAT || lasToken.tag == Tag.STRING) {
                table.put(token, new Id(typeToString(lasToken)));
            } else {
                errorId(line);
            }
        }
    }

    /*
     * SEMÂNTICO PARA AS OPERAÇÕES DE STRING
     */

    private void errorOp(int line, String operacao) {
        System.out.println(line + " :" +
                "error: operação inválida para 'literal' " + operacao + "' '");
    }

    // apenas a concatenação {+} é permitida
    public void checkStringOperation(Token token, int line) {
        if (result == Tag.STRING) {
            switch (token.tag) {
                case MIN:
                    errorOp(line, "-");
                    break;
                case DIV:
                    errorOp(line, "/");
                    break;
                case MUL:
                    errorOp(line, "*");
                    break;
                default:
                    break;
            }
        }
    }

    /*
     * SEMÂNTICO PARA AS OPERAÇÕES DENTRO DE EXPRESSÕES
    */

    // tipo atual da expressão
    // garante que tipos iguais sejam operados
    public void setCurrentType(Token token, int line) {
        if (currentType == " ") {
            if (!table.containsKey(token)) {
                errorId(line);
            } else {
                currentType = table.get(token).toString();
                System.out.println("\n" + currentType);
            }
        }
    }

    /* Operações para a expressões */
    private void errorLog(int line, String type, String expectedType) {
        System.out.println(line + " :" +
                " .Error: tipo " + type + "incompatível." +
                " Esperado: " + expectedType);
    }

    public void checkExprType(Token token, int line) {
        System.out.println("Current expression tag : " + currentExpression);
        System.out.println("\n" + table.get(token));
        if (currentExpression == Tag.VOID) {
            if (table.get(token).toString().equals("INTEGER")) {
                currentExpression = Tag.INT;
            } else if (table.get(token).toString().equals("FLOAT")) {
                currentExpression = Tag.FLOAT;
            } else {
                currentExpression = Tag.STRING;
            }
            System.out.println("Current expression atribution : " + currentExpression);

        } 
        // Diego, aqui tem que analisar a questão do assign,
        // variaveis inteiras recebem inteiros
        // variaveis floats recebem floats
        // porém, a tag deles é a mesma (NUM)
        // Aqui analisa a expressão também, se o tipo atual for diferente quebra
        else if (currentExpression != Tag.VOID) {
            if (token.getToken() == Tag.INT) {
                if (currentExpression != Tag.INT)
                    errorLog(line, typeToString(token), "integer");
            } else if (token.getToken() == Tag.FLOAT) {
                if (currentExpression != Tag.FLOAT)
                    errorLog(line, typeToString(token), "float");
            } else {
                if (currentExpression != Tag.STRING)
                    errorLog(line, typeToString(token), "literal");
            }
        }
    }

    public void resertType(){
        currentExpression = Tag.VOID;
    }
}
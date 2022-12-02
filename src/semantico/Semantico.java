package semantico;

import java.util.*;

import lexico.*;
import tabsimbolos.*;

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
        // this.table = symbols.getTable();
    }

    /*
     * OPERAÇÕES SEMÂNTICAS PARA OS IDENTIFICADORES
     */

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
            System.out.println("Error: redefinition of '" + token.toString() + "' na linha " + line);
            System.exit(0);
        } else {
            table.put(token, new Id(typeToString(lasToken)));

        }
    }

    // verifica se o ID já foi declarado
    public boolean checkId(Token token, int line) {
        if (!table.containsKey(token) && token.tag == Tag.ID) {
            errorId(line);
        } else if (table.containsKey(token) && token.tag == Tag.ID) {
            return true;
        }
        return false;
    }

    /*
     * OPERAÇÕES SEMÂNTICAS PARA AS OPERAÇÕES DE STRING
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
     * OPERAÇÕES SEMÂNTICAS PARA AS OPERAÇÕES DENTRO DE EXPRESSÕES
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

    public void setExprType(Token token, int line){
        if (checkId(token, line)) {
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
        }
    }
    public void checkExprType(Token token, int line) {
        if (currentExpression != Tag.VOID) {
            System.out.println("\nType in expr = " + currentExpression);
            System.out.println("\n\n" + token.getValue());
            if (token.getValue() instanceof Integer) {
                if (currentExpression != Tag.INT)
                    errorLog(line, token.toString(), "integer");
            } else if (token.getValue() instanceof Float) {
                if (currentExpression != Tag.FLOAT)
                    errorLog(line, token.toString(), "float");
            } else {
                if (currentExpression != Tag.STRING)
                    errorLog(line, token.toString(), "literal");
            }

        }
    }

    public void resertType() {
        currentExpression = Tag.VOID;
    }
}
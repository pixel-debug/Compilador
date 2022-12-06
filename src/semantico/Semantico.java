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
    }

    /*
     * OPERAÇÕES SEMÂNTICAS PARA OS IDENTIFICADORES
     */

    public Hashtable getTable() {
        return table;
    }

    private void errorId(int line) {
        System.out.println("\033[1;31mError:\033[0m identificador não declarado na linha " + line + ". Abortando ...");
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
    public void updateSimbolTable(Token token, Token lastToken, int line) {
        if (table.containsKey(token)) {
            System.out.println("\033[1;31mError:\033[0m redefinition of '" + token.toString() + "' na linha " + line);
            System.exit(0);
        } else {
            table.put(token, new Id(typeToString(lastToken)));
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
        System.out.println(
                "\033[1;31mError\033[0m na linha" +
                        line + " :operação inválida para 'literal' "
                        + operacao + "' '");
        System.exit(0);

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
            }
        }
    }

    /* Operações para a expressões */
    private void errorLog(int line, String type, String expectedType, String sendType) {
        System.out.println(
                "\033[1;31mError\033[0m na linha" +
                        line + " :tipo " + type + " incompatível." +
                        " Esperado: " + expectedType +
                        " Recebido: " + sendType);
        System.exit(0);

    }

    public void setExprType(Token token, int line) {
        if (checkId(token, line)) {
            if (currentExpression == Tag.VOID) {
                if (table.get(token).toString().equals("INTEGER")) {
                    currentExpression = Tag.INT;
                } else if (table.get(token).toString().equals("FLOAT")) {
                    currentExpression = Tag.FLOAT;
                } else {
                    currentExpression = Tag.STRING;
                }
            }
        }
    }

    public void checkExprType(Token token, int line) {
        if (currentExpression != Tag.VOID) {
            if (token.tag == Tag.NUM) {
                if (token.getType().equals("int")) {
                    if (currentExpression != Tag.INT)
                        errorLog(line, token.toString(), currentExpression.toString(), "INTEGER");
                } else if (token.getType().equals("float")) {
                    if (currentExpression != Tag.FLOAT)
                        errorLog(line, token.toString(), currentExpression.toString(), "FLOAT");
                }
            } else if (token.tag == Tag.LIT) {
                if (currentExpression != Tag.STRING)
                    errorLog(line, token.toString(), currentExpression.toString(), "LITERAL");
            } else if (token.tag == Tag.ID && checkId(token, line)) {
                if (table.get(token).toString().equals("INTEGER")) {
                    if (currentExpression != Tag.INT)
                        errorLog(line, token.toString(), currentExpression.toString(), "INTEGER");
                } else if (table.get(token).toString().equals("FLOAT")) {
                    if (currentExpression != Tag.FLOAT)
                        errorLog(line, token.toString(), currentExpression.toString(), "FLOAT");
                } else {
                    if (currentExpression != Tag.STRING)
                        errorLog(line, token.toString(), currentExpression.toString(), "LITERAL");
                }
            }
        }
    }

    public void resertType() {
        currentExpression = Tag.VOID;
    }
}
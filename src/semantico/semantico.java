package semantico;

import java.util.*;
import lexico.*;
import tabsimbolos.*;

public class Semantico {
    private HashMap<Token, Id> table;
    private Tag result;
    private Tag currentType;
    private Tag currentExpression;

    public Semantico() {
        result = Tag.VOID;
        currentType = Tag.VOID;
        currentExpression = Tag.VOID;
    }

    /* Operações para a tabela de símbolos */
    private String typeToString(Token token) {
        String type = "string";
        if (token.tag == Tag.FLOAT)
            type = "float";
        else if (token.tag == Tag.INT)
            type = "integer";
        return type;
    }

    private void updateSimbolTable(Token token, int line) {
        // é preciso atualizar a tabela de simbolos
        // para receber a string tipo ao inves do lexema
        if (table.containsKey(token.getToken())) {
            System.out.println(line + " :" + "error: redefinition of " + token.toString() + "' '");
            System.exit(0);
        } else {
            table.put(token, currentType);
        }
    }

    /* Operações para a expressões */
    private void errorLog(int line, String type, String expectedType) {
        System.out.println(line + " :" +
                "error: tipo " + type + "incompatível." +
                " Esperado: " + expectedType);
    }

    public void checkExprType(Token token, int line) {
        if (currentExpression == Tag.VOID) {
            if (token instanceof Num) {
                currentExpression = Tag.INT;
            } else if (token instanceof NumFloat) {
                currentExpression = Tag.FLOAT;
            } else {
                currentExpression = Tag.STRING;
            }
        } else if (currentExpression != Tag.VOID || result != Tag.VOID) {
            if (token instanceof Num) {
                if (currentExpression != Tag.INT)
                    errorLog(line, typeToString(token), "integer");
            } else if (token instanceof NumFloat) {
                if (currentExpression != Tag.FLOAT)
                    errorLog(line, typeToString(token), "float");
            } else {
                if (currentExpression != Tag.STRING)
                    errorLog(line, typeToString(token), "literal");
            }
        }
    }

    /* Operações para as strings */
    private void errorOp(int line, String operacao) {
        System.out.println(line + " :" +
                "error: operação inválida para 'literal' " + operacao + "' '");
    }

    public void checkStringOperation(Token token, int line) {
        if (result == Tag.STRING) {
            switch (token.tag) {
                case SUM:
                    // concatenar
                    break;
                case MIN:
                    errorOp(line, "-");
                    break;
                case DIV:
                    errorOp(line, "/");
                    break;
                case MUL:
                    errorOp(line, "*");
                    break;
            }
        }
    }

    /* Operações para os identificadores */
    private void errorId(int line) {
        System.out.println(line + " :" + 
            "error: identificador não declarado. Abortando ...");
            System.exit(0);
    }

    public void checkId(Id id, int line) {
        if(currentExpression == Tag.VOID){
            if(!table.containsKey(id)){
                errorId(line);
            }
            else{
                currentExpression = table.get(id.getLexeme());
            }
        }
        else if(currentExpression != Tag.VOID || result != Tag.VOID){
            if(!table.containsKey(id)){
                errorId(line);
            }
            else{
                Id lastTerm = table.get(id);
                if(currentExpression != lastTerm)
                    errorLog(line, typeToString(lastTerm), "identifier");
            }
        }
    }

}
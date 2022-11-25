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
    private HashMap<Token, Id> table;
    private Tag result;
    private String currentType;
    private Tag currentExpression;
    private Lexico lexico;
    

    public Semantico() {
        result = Tag.VOID;
        currentType =  " ";
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

    //NUM SERIA MELHOR LER O PROXIMO TOKEN POR AQUI NÃO??
    //Ai verifica o semaitico da mesma forma do sintatico
    /*private void advance() throws Exception {
        System.out.println("Lendo proximo token");
        token = lexico.scan();
      }
    */

    public void updateSimbolTable(Token token, int line) {
        if (table.containsKey(token)) {
            System.out.println(line + " :" + "error: redefinition of " + token.toString() + "' '");
            System.exit(0);
        } else {
            table.put(token, new Id(typeToString(token)));
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
    
    /* Operações para os identificadores */
    private void errorId(int line) {
        System.out.println(line + " :" + 
            "error: identificador não declarado. Abortando ...");
            System.exit(0);
    }

    public void checkId(Token id, int line) {
        if(currentType == " "){
            if(!table.containsKey(id)){
                errorId(line);
            }
            else{
                currentType = table.get(id).getValue(); 
            }                                                   
        }
        else if(!currentType.equals(" ") || result != Tag.VOID){
            if(!table.containsKey(id)){
                errorId(line);
            }
            else{ 
                String lastTerm = table.get(id).getValue();
                if(!Objects.equals(currentType, lastTerm))
                    errorLog(line, lastTerm, "identifier");
            } 
        }
    }

    public void setCurrentType(Token token, int line) {
        if(result == Tag.VOID){
            if(!table.containsKey(token)){
                errorId(line);
            }else{
                result = token.getToken();
            }
        }
    }

}
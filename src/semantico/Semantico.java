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
        currentType =  " ";
        currentExpression = Tag.VOID;
        this.symbols = symbols;

    }
    

    /* Operações para a expressões */
    private void errorLog(int line, String type, String expectedType) {
        System.out.println(line + " :" +
                " .Error: tipo " + type + "incompatível." +
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
        System.out.println("Error: identificador não declarado na linha " + line + ". Abortando ...");
            System.exit(0);
    }

    public void checkId(Token token, int line) {
        if(currentType == " "){
            if(!table.containsKey(token)){
                errorId(line);
            }
            else{
                currentType = table.get(token).toString();
                System.out.println("TIPO NA RODA " + currentType);
            }                                                   
        }
        else if(!currentType.equals(" ") || result != Tag.VOID){
            if(!table.containsKey(token)){
                errorId(line);
            }
            else{ 
                String lastTerm = table.get(token).toString();
                System.out.println(lastTerm);
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

    /* Operações para a tabela de símbolos */
    private String typeToString(Token token) {
        String type = "string";
        if (token.tag == Tag.FLOAT)
            type = "float";
        else if (token.tag == Tag.INT)
            type = "integer";
        return type;
    }

    public void updateSimbolTable(Token token, Token lasToken, int line) {
        if (table.containsKey(token)) {
            System.out.println("Error: redefinition of '" + token.toString() + "' na linha " + line );
            System.exit(0);
        } else {
            table.put(token, new Id(typeToString(lasToken)));
            System.out.println(table);
        }
    }

}
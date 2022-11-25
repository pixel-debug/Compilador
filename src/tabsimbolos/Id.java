package tabsimbolos;

import lexico.*;
import java.util.Objects;


public class Id {

    private String lexeme;
    private Tag type;

    public Id(String lexeme) {
        this.lexeme = lexeme;
        this.type = null;
    }

    public Id(String lexeme, Tag type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public Tag getType() {
        return type;
    }

    public void setType(Tag type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (Objects.nonNull(type))
            return "{ lexeme: " + lexeme + ", type: " + type + " }";
        else
            return "{ lexeme: " + lexeme + " }";
    }

}
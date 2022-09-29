public class Id {

    private String lexeme;
    private int type;

    public Id(String lexeme) {
        this.lexeme = lexeme;
        this.type = -1;
    }

    public Id(String lexeme, int type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (type >= 0)
            return "{ lexeme: " + lexeme + ", type: " + type + " }";
        else
            return "{ lexeme: " + lexeme + " }";
    }

}
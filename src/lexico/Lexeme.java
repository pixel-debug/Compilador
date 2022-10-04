package lexico;
public class Lexeme extends Token {

  private String lexeme = "";

  public static final Lexeme and = new Lexeme("&&", Tag.AND);
  public static final Lexeme or = new Lexeme("||", Tag.OR);
  public static final Lexeme eq = new Lexeme("==", Tag.EQ);
  public static final Lexeme le = new Lexeme("<=", Tag.LE);
  public static final Lexeme ge = new Lexeme(">=", Tag.GE);

  public Lexeme(String s, Tag tag) {
    super(tag);
    lexeme = s;
  }

  public String getLexeme() {
    return lexeme;
  }

  @Override
  public String toString() {
    return "" + lexeme;
  }
}

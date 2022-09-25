import java.io.*;
import java.util.*;
import java.util.Hashtable;

public class Lexico {

  private int line = 1; // contador de linhas
  private char ch = ' '; // caractere lido do arquivo
  private FileReader file;
  private Hashtable words = new Hashtable();

  /* Método para inserir palavras reservadas na HashTable */
  private void reserve(Word w) {
    words.put(w.getLexeme(), w); // lexema é a chave para entrada na
    // HashTable
  }

  public Hashtable getWords() {
    return words;
  }

  /* Método construtor */
  public Lexico(String fileName) throws FileNotFoundException {
    try {
      file = new FileReader(fileName);
    } catch (FileNotFoundException e) {
      System.out.println("Arquivo não encontrado");
      throw e;
    }
    // Insere palavras reservadas na HashTable
    reserve(new Word("start", Tag.START));
    reserve(new Word("exit", Tag.EXIT));
    reserve(new Word("int", Tag.INT));
    reserve(new Word("string", Tag.STRING));
    reserve(new Word("float", Tag.FLOAT));
    reserve(new Word("if", Tag.IF));
    reserve(new Word("then", Tag.THEN));
    reserve(new Word("end", Tag.END));
    reserve(new Word("else", Tag.ELSE));
    reserve(new Word("do", Tag.DO));
    reserve(new Word("while", Tag.WHILE));
    reserve(new Word("scan", Tag.SCAN));
    reserve(new Word("print", Tag.PRINT));
    reserve(new Word("not", Tag.NOT));
  }

  /* Lê o próximo caractere do arquivo */
  private void readch() throws IOException {
    ch = (char) file.read();
  }

  /* Lê o próximo caractere do arquivo e verifica se é igual a c */
  private boolean readch(char c) throws IOException {
    readch();
    if (ch != c)
      return false;
    ch = ' ';
    return true;
  }

  public int getLine() {
    return line;
  }

  // VERIFICAR OS COMENTARIOS
  public Token scan() throws Exception {
    boolean comentario = false;
    int comentarioLine = 0;
    // Desconsidera delimitadores na entrada
    for (;; readch()) {
      if (ch=='/'){
        readch();
          if (ch == '*') {
            while (true) {
              readch();
              if(ch=='\n') this.line++;
              if (ch == '*') {
                readch();
                if (ch == '/') break;
              }
              if((int)ch==65535){
                  throw new Exception("Um comentário não foi fechado");
              }
            }
          }
          if (ch == '/') {
            while (true) {
              readch();
              if (ch == '\n') {
                break;
              }
            }
          } else {
            return new Token(Tag.DIV);
          }}
      if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b')
        continue;
      else if (ch == '\n')
        this.line++; // conta linhas
      else if (ch == 65535) {
        return new Token(Tag.EOF);
      } else
        break;
    }

    if (!comentario) {
      switch (ch) {
        // Operadores
        case '&':
          if (readch('&'))
            return Word.and;
          else
            return new Token(Tag.AND);
        case '|':
          if (readch('|'))
            return Word.or;
          else
            return new Token(Tag.OR);
        case '=':
          if (readch('='))
            return Word.eq;
          else
            return new Token(Tag.PPV);
        case '<':
          if (readch('='))
            return Word.le;
          else
            return new Token(Tag.LT);
        case '>':
          if (readch('='))
            return Word.ge;
          else
            return new Token(Tag.GT);
        case ',':
          readch();
          return new Token(Tag.VRG);
        case ';':
          readch();
          return new Token(Tag.PV);
        case '+':
          readch();
          return new Token(Tag.SUM);
        case '-':
          readch();
          return new Token(Tag.MIN);
        case '*':
          readch();
          return new Token(Tag.MUL);
        case '(':
          readch();
          return new Token(Tag.AP);
        case ')':
          readch();
          return new Token(Tag.FP);
        case '"':
          StringBuilder sb = new StringBuilder();
          while(true){
            readch();
            if(ch=='"') break;
            else sb.append(ch);
          }
          String s = sb.toString();
          readch();
          return new Word(s, Tag.STRING);
          /*
           * case '.':
           * readch();
           * return new Token(Tag.DOT);
           */
          // DEVE - SE TRATAR A STRING (TAG.LIT)

      }

      // Números
      if (Character.isDigit(ch)) {
        int value = 0;
        do {
          value = 10 * value + Character.digit(ch, 10);
          readch();
        } while (Character.isDigit(ch));
        return new Num(value);
      }

      // Identificadores
      if (Character.isLetter(ch)) {
        StringBuilder sb = new StringBuilder();
        do {
          sb.append(ch);
          readch();
        } while (Character.isLetterOrDigit(ch));
        String s = sb.toString();
        Word w = (Word) words.get(s);
        if (w != null)
          return w; // palavra já existe na HashTable
        w = new Word(s, Tag.ID);
        words.put(s, w);
        return w;
      }
    }
    // Caracteres não especificados
    Token t = new Token(Tag.INVALID_TOKEN);
    ch = ' ';
    return t;
  }
}

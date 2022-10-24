package lexico;

import java.io.*;
import java.util.*;
import java.util.Hashtable;
import tabsimbolos.*;

public class Lexico {

  public int line = 1; // contador de linhas
  private char ch = ' '; // caractere lido do arquivo
  private FileReader file;
  private Hashtable words = new Hashtable();
  private TabelaDeSimbolos symbols;

  /* Método para inserir palavras reservadas na HashTable */
  private void reserve(Lexeme w) {
    words.put(w.getLexeme(), w); // lexema é a chave para entrada na
    // HashTable
  }

  public Hashtable getWords() {
    return words;
  }

  /* Método construtor */
  public Lexico(String fileName, TabelaDeSimbolos symbols)
    throws FileNotFoundException {
    try {
      file = new FileReader(fileName);
      this.symbols = symbols;
    } catch (FileNotFoundException e) {
      System.out.println("Arquivo não encontrado");
      throw e;
    }
    // Insere palavras reservadas na HashTable
    reserve(new Lexeme("start", Tag.START));
    reserve(new Lexeme("exit", Tag.EXIT));
    reserve(new Lexeme("int", Tag.INT));
    reserve(new Lexeme("string", Tag.STRING));
    reserve(new Lexeme("float", Tag.FLOAT));
    reserve(new Lexeme("if", Tag.IF));
    reserve(new Lexeme("then", Tag.THEN));
    reserve(new Lexeme("end", Tag.END));
    reserve(new Lexeme("else", Tag.ELSE));
    reserve(new Lexeme("do", Tag.DO));
    reserve(new Lexeme("while", Tag.WHILE));
    reserve(new Lexeme("scan", Tag.SCAN));
    reserve(new Lexeme("print", Tag.PRINT));
    reserve(new Lexeme("not", Tag.NOT));
  }

  /* Lê o próximo caractere do arquivo */
  private void readch() throws IOException {
    ch = (char) file.read();
  }

  /* Lê o próximo caractere do arquivo e verifica se é igual a c */
  private boolean readch(char c) throws IOException {
    readch();
    if (ch != c) return false;
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
      if (ch == '/') {
        readch();
        if (ch == '*') {
          while (true) {
            readch();
            if (ch == '\n') this.line++;
            if (ch == '*') {
              readch();
              if (ch == '/') break;
            }
            if ((int) ch == 65535) {
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
        }
      }
      if (
        ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b'
      ) continue; else if (ch == '\n') this.line++; // conta linhas
      else if (ch == 65535) {
        return new Token(Tag.EOF);
      } else break;
    }

    if (!comentario) {
      switch (ch) {
        // Operadores
        case '&':
          if (readch('&')) return Lexeme.and; else return new Token(Tag.AND);
        case '|':
          if (readch('|')) return Lexeme.or; else return new Token(Tag.OR);
        case '=':
          if (readch('=')) return Lexeme.eq; else return new Token(Tag.PPV);
        case '<':
          if (readch('=')) return Lexeme.le; else return new Token(Tag.LT);
        case '>':
          if (readch('=')) return Lexeme.ge; else return new Token(Tag.GT);
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
        case '{':
          StringBuilder sb = new StringBuilder();
          while (true) {
            readch();
            if (ch == '}') break; else sb.append(ch);
            if ((int) ch == 65535) {
              throw new Exception("Um string não foi fechado");
            }
          }
          String s = sb.toString();
          readch();
          return new Lexeme(s, Tag.STRING);
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
        if (ch != '.') return new Num(value);

        float aux = 10;
        float float_value = 0;
        while (true) {
          readch();
          if (!Character.isDigit(ch)) break;
          float_value += (Character.digit(ch, 10) / 10.0);
          aux = aux * 10;
        }
        return new NumFloat(float_value);
      }

      // Identificadores
      if (Character.isLetter(ch) || ch == '_') {
        StringBuilder sb = new StringBuilder();
        do {
          sb.append(ch);
          readch();
        } while (Character.isLetterOrDigit(ch) || ch == '_');
        String s = sb.toString();
        Lexeme w = (Lexeme) words.get(s);
        if (w != null) return w; // palavra já existe na HashTable

        w = new Lexeme(s, Tag.ID);
        words.put(s, w);

        if (!symbols.has(w)) {
          symbols.put(w, new Id(w.getLexeme()));
        }

        return w;
      }
    }
    // Caracteres não especificados
    Token t = new Token(Tag.INVALID_TOKEN);
    ch = ' ';
    return t;
  }
}

package lexico;

import java.util.Objects;

public class Token {
  public final Tag tag; //constante que representa o token
  public final Integer valueInt;
  public final Float valueFloat;

  public Token (Tag t){
    tag = t;
    valueFloat = null;
    valueInt = null;
  }
  public Token (Tag t, int value){
    tag = t;
    valueInt = value;
    valueFloat = null;
  }

  public Token (Tag t, float value){
    tag = t;
    valueFloat = value;
    valueInt = null;
  }

  public String toString(){
    return "" + tag;
  }

   public Tag getToken(){
    return this.tag;
  }

  public Float getValue(){
    return Objects.isNull(valueInt) ? (float) valueInt : valueFloat;
  }
}
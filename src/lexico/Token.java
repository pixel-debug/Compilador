package lexico;

import java.util.Objects;

public class Token {
  public final Tag tag; //constante que representa o token
  private final Integer valueInt;
  private final Float valueFloat;

  public Token (Tag t){
    tag = t;
    valueFloat = null;
    valueInt = null;
  }
  public Token (Tag t, Number value, boolean isFloat){
    tag = t;
    if(!isFloat){
      valueInt = Integer.parseInt(value.toString());
      valueFloat = null;
    }
    else{
      valueInt = null;
      valueFloat = Float.parseFloat(value.toString());;
    }
  }

  public String toString(){
    return "" + tag;
  }

   public Tag getToken(){
    return this.tag;
  }

  public Number getValue(){
    return Objects.isNull(this.valueInt) ? this.valueFloat : this.valueInt;
  }
}
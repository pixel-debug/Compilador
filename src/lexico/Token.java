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

  public String getValue(){
    if (Objects.nonNull(this.valueInt)){
      return this.valueInt.toString();
    } else if(Objects.nonNull(this.valueFloat)){
      return this.valueFloat.toString();
    }
    else return " ";
  }

  public String getType(){
    if (Objects.nonNull(this.valueInt)){
      return "int";
    } else if(Objects.nonNull(this.valueFloat)){
      return "float";
    }
    else return " ";
  }
}
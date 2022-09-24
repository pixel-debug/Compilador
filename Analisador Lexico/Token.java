public class Token {
  public final Tag tag; //constante que representa o token
  public Token (Tag t){
    tag = t;
  }

  public String toString(){
    return "" + tag;
  }

   public Tag getToken(){
    return this.tag;
  } 
}
public class Num extends Token{
  public final int value;
  
  public Num(int value){
    super(Tag.INT);
    this.value = value;
  }
  
  @Override
  public String toString(){
    return "" + value;
  }
}
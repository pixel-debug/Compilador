public class NumFloat extends Token {
    public final float value;

    // Construtor
    public NumFloat(float v) {
        super(Tag.FLOAT);
        this.value = value;
    }

    public String toString() {
        return "" + value;
    }
}
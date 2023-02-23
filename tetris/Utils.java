import java.awt.*;

public final class Utils {
    public static final int I=0;
    public static final int T=1;
    public static final int J=2;
    public static final int L=3;
    public static final int O=4;
    public static final int S=5;
    public static final int Z=6;

    public static final int squareSize=40;

    public static Color getColor(int type){
        return switch (type) {
            case Utils.I -> Color.BLUE;
            case Utils.T -> Color.GREEN;
            case Utils.J -> Color.RED;
            case Utils.S -> Color.YELLOW;
            case Utils.Z -> Color.PINK;
            case Utils.L -> Color.MAGENTA;
            case Utils.O -> Color.ORANGE;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}

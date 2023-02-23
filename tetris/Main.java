import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
            JFrame frame= new JFrame();
            Game game= new Game();
            game.setFocusable(true);
            frame.add(game);
            frame.setSize(600,829);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setResizable(false);

    }
}

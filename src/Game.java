import javax.swing.JFrame;
import java.awt.*;
public class Game extends JFrame{
    public Game(){
        this.add(new Graphics());
        this.setTitle("Snake");
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.red);
    }
}
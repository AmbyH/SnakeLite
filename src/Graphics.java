import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Graphics extends JPanel implements ActionListener {
    static final int width = 1200;
    static final int height = 800;
    static final int part = 25;
    static final int screen = (width * height) / (part * part);
    final Font font = new Font("SANS_SERIF", Font.PLAIN, 20);
    private Food food;
    private int[] snakeX = new int[screen];
    private int[] snakeY = new int[screen];
    private int snakeLength;
    public int score;
    private int highScore;
    private final Timer timer = new Timer(100, this);
    private int start = 0;
    private char direction = 'R';
    private boolean moving = false;

    public Graphics() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {  // check if the snake needs to switch directions
                if(moving) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_UP, KeyEvent.VK_W:
                            if (direction != 'D') {
                                direction = 'U';
                            }
                            break;
                        case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                            if (direction != 'U') {
                                direction = 'D';
                            }
                            break;
                        case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                            if (direction != 'R') {
                                direction = 'L';
                            }
                            break;
                        case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                            if (direction != 'L') {
                                direction = 'R';
                            }
                            break;
                    } }
                else {
                    run();
                }
            }
        });
    }

    //to start the game, set up snake and score
    public void run() {
        snakeX = new int[screen];
        snakeY = new int[screen];
        snakeLength = 4;
        score = 0;
        direction = 'R'; //the snake generates top left
        moving = true;
        food();
        start++;
        timer.start();
    }

    // instructions
    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (start == 0) {
            String inst1 = "Welcome to Snake.";
            String inst2 = "Eat all the food that appears! If you touch the edge or yourself, the game ends. ";
            String inst3 = "Press any key to start. Have fun!";
            g.setColor(Color.white);
            g.setFont(new Font("SANS_SERIF", Font.BOLD, 30));
            g.drawString(inst1, (width - getFontMetrics(g.getFont()).stringWidth(inst1)) / 2, height / 2 - 80);
            g.setFont(font);
            g.drawString(inst2, (width - getFontMetrics(g.getFont()).stringWidth(inst2)) / 2, height / 2 - 30);
            g.drawString(inst3, (width - getFontMetrics(g.getFont()).stringWidth(inst3)) / 2, height / 2 + 25);
        }
        else if(moving) {
            g.setColor(Color.red);
            g.fillOval(food.getCoordinateX(), food.getCoordinateY(), part, part);
            g.setColor(Color.green); // to fill snake
            for(int i = 0; i < snakeLength; i++) {
                g.fillRect(snakeX[i], snakeY[i], part, part);
            }
            String scoreDisplay = String.format("Score: %d", score); // to display score
            g.setColor(Color.white);
            g.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
            g.drawString(scoreDisplay, 10, 30);
        }
        else {
            highScore(); //show  high score
            String endScore = String.format("Score: %d          High Score: %d", score, highScore);
            String message = "Uh oh! Game over! Press any key to try again."; //to restart game
            g.setFont(font);
            g.setColor(Color.white);
            g.drawString(endScore, (width - getFontMetrics(g.getFont()).stringWidth(endScore)) / 2, height / 2 - 30);
            g.drawString(message, (width  -getFontMetrics(g.getFont()).stringWidth(message)) / 2, height / 2 + 30);
        }
    }

    public void move() { // move snake based on coordinates
        for(int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i-1];
            snakeY[i] = snakeY[i-1];
        }
        if(direction == 'U')
            snakeY[0] = snakeY[0] - part;
        if(direction == 'D')
            snakeY[0] = snakeY[0] + part;
        if(direction =='L')
            snakeX[0] = snakeX[0] - part;
        if(direction == 'R')
            snakeX[0] = snakeX[0] + part;
    }

    public void food() { // from food class
        food = new Food();
    }

    public void eatFood() throws UnsupportedAudioFileException, LineUnavailableException, IOException { // + to snake length and score
        if(snakeX[0] == food.getCoordinateX() && (snakeY[0] == food.getCoordinateY())) {
            snakeLength = snakeLength + 1;
            score = score + 1;
            food(); // new food once eaten
            eatMusic();
        }
    }

    public void highScore() {
        if(score > highScore) {
            highScore = score;
        }
    }

    // if snake hits itself or the edge
    public void hit() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        for(int i = snakeLength; i > 0; i--) {
            if((snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) {
                moving = false;
                deathMusic();
                break;
            }
        }
        if(snakeX[0] < 0 || snakeX[0] > width - part || snakeY[0] < 0 || snakeY[0] > height - part) {
            moving = false;
            deathMusic();
        }
        if(!moving) {
            timer.stop();
        }
    }

    public static void bgMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("/Users/a/IdeaProjects/SnakeLite/src/Monkey Spinning OST.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    public static void eatMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("/Users/a/IdeaProjects/SnakeLite/src/Minecraft Eating SFX.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    public static void deathMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("/Users/a/IdeaProjects/SnakeLite/src/Brawl Stars Lose OST.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(moving) {
            move();
            try {
                hit();
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                eatFood();
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        repaint();
    }
}
import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import java.util.*;



public class SnakeGame extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 600;

    private static final int SCREEN_HEIGHT = 600;

    private static final int UNIT_SIZE = 20;

    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    private static final int DELAY = 50; // Speed up the snake by reducing delay

    private final int[] x = new int[GAME_UNITS];

    private final int[] y = new int[GAME_UNITS];

    private int bodyParts = 6;

    private int applesEaten;

    private int appleX;

    private int appleY;

    private char direction = 'R';

    private boolean running = false;

    private javax.swing.Timer timer; // Use javax.swing.Timer to avoid ambiguity



    public SnakeGame() {

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        this.setBackground(Color.black);

        this.setFocusable(true);

        this.addKeyListener(new MyKeyAdapter());

        startGame();

    }



    public void startGame() {

        newApple();

        running = true;

        timer = new javax.swing.Timer(DELAY, this); // Create javax.swing.Timer instance

        timer.start();

    }



    public void resetGame() {

        bodyParts = 6;

        applesEaten = 0;

        direction = 'R';

        running = true;

        newApple();

        for (int i = 0; i < bodyParts; i++) {

            x[i] = 0;

            y[i] = 0;

        }

        timer.start();

    }



    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        draw(g);

    }



    public void draw(Graphics g) {

        if (running) {

            g.setColor(Color.red);

            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);



            for (int i = 0; i < bodyParts; i++) {

                if (i == 0) {

                    g.setColor(Color.green);

                } else {

                    g.setColor(new Color(45, 180, 0));

                }

                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

            }

        } else {

            gameOver(g);

        }

    }



    public void newApple() {

        // Generate random coordinates for the apple

        appleX = new Random().nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;

        appleY = new Random().nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;



        // Increase the size of the apple to be consumed when any part of the snake runs over it

        appleX += UNIT_SIZE / 2;

        appleY += UNIT_SIZE / 2;

    }



    public void move() {

        // Move the body parts of the snake

        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i - 1];

            y[i] = y[i - 1];

        }



        // Move the head of the snake by one unit in the current direction

        switch (direction) {

            case 'U':

                y[0] -= UNIT_SIZE;

                break;

            case 'D':

                y[0] += UNIT_SIZE;

                break;

            case 'L':

                x[0] -= UNIT_SIZE;

                break;

            case 'R':

                x[0] += UNIT_SIZE;

                break;

        }

    }



    public void checkApple() {

        // Check if any part of the snake's body overlaps with the apple

        for (int i = 0; i < bodyParts; i++) {

            if (x[i] == appleX && y[i] == appleY) {

                bodyParts++; // Increase snake length

                applesEaten++; // Increase score

                newApple(); // Generate a new apple

                break; // Exit loop since we only want to eat one apple per move

            }

        }

    }



    public void checkCollisions() {

        // Check if the snake collides with itself

        for (int i = bodyParts; i > 0; i--) {

            if (x[0] == x[i] && y[0] == y[i]) {

                running = false;

                break;

            }

        }



        // Check if the snake collides with the walls

        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {

            running = false;

        }



        if (!running) {

            timer.stop();

        }

    }



    public void gameOver(Graphics g) {

        g.setColor(Color.red);

        g.setFont(new Font("Ink Free", Font.BOLD, 75));

        FontMetrics metrics = getFontMetrics(g.getFont());

        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.red);

        g.setFont(new Font("Ink Free", Font.BOLD, 40));

        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        g.setColor(Color.red);

        g.setFont(new Font("Ink Free", Font.BOLD, 40));

        g.drawString("Press Enter to Restart", (SCREEN_WIDTH - metrics.stringWidth("Press Enter to Restart")) / 2, SCREEN_HEIGHT / 2 + 100);

    }



    @Override

    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();

            checkApple();

            checkCollisions();

        }

        repaint();

    }



    public class MyKeyAdapter extends KeyAdapter {

        @Override

        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:

                    if (direction != 'R') {

                        direction = 'L';

                    }

                    break;

                case KeyEvent.VK_RIGHT:

                    if (direction != 'L') {

                        direction = 'R';

                    }

                    break;

                case KeyEvent.VK_UP:

                    if (direction != 'D') {

                        direction = 'U';

                    }

                    break;

                case KeyEvent.VK_DOWN:

                    if (direction != 'U') {

                        direction = 'D';

                    }

                    break;

                case KeyEvent.VK_ENTER:

                    if (!running) {

                        resetGame();

                    }

                    break;

            }

        }

    }



    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake");

        SnakeGame game = new SnakeGame();

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

    }

}



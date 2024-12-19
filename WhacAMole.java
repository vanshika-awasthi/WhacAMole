import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Random;

public class WhacAMole {
    int boardWidth = 600;
    int boardHeight = 700; // 100px for the text panel on top

    JFrame frame = new JFrame("Mario: Whac A Mole"); // this is going to be our window
    JLabel textLabel = new JLabel(); // panel for text label
    JLabel highScoreLabel = new JLabel(); // Label for the high score
    JPanel textPanel = new JPanel(); // panel for the score label
    JPanel boardPanel = new JPanel(); // panel for the game
    JPanel controlPanel = new JPanel(); // Panel for the restart button

    JButton[] board = new JButton[9]; // initialized an array to store nine buttons
    JButton restartButton = new JButton("Restart Game"); // Restart button
    ImageIcon moleIcon;
    ImageIcon plantIcon;

    JButton currMoleTile; // track current mole tile
    JButton currPlantTile; // track current plant tile

    Random random = new Random();
    Timer setMoleTimer;
    Timer setPlantTimer;

    int score = 0; // Changed from local to instance variable
    int highScore = 0; // Track the high score

    WhacAMole() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // within the window we will have X button that will terminate the program
        frame.setLayout(new BorderLayout());

        // Set up text label
        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setForeground(Color.black); // added color to the font for contrast
        textLabel.setHorizontalAlignment(JLabel.CENTER); // align the text to center
        textLabel.setText("Score: 0");

        // Set up high score label
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        highScoreLabel.setForeground(Color.black);
        highScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        highScoreLabel.setText("High Score: 0");

        // Set up the text panel
        textPanel.setLayout(new GridLayout(2, 1)); // Two rows: score and high score
        textPanel.add(textLabel);
        textPanel.add(highScoreLabel);
        textPanel.setBackground(Color.gray); // background color of text panel
        frame.add(textPanel, BorderLayout.NORTH); // Score panel goes on top (NORTH)

        // Set up the game board panel with GridLayout
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.black); // background color of game panel
        frame.add(boardPanel, BorderLayout.CENTER); // Add the board to the center of the frame

        // Resizing the plant image
        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        // Resizing the mole image
        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        // Add nine buttons to the board
        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);

            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton clickedTile = (JButton) e.getSource();
                    if (clickedTile == currMoleTile) {
                        score += 10; // Modify instance variable
                        textLabel.setText("Score: " + score);
                    } else if (clickedTile == currPlantTile) {
                        if (score > highScore) {
                            highScore = score; // Update high score
                            highScoreLabel.setText("High Score: " + highScore);
                        }
                    
                        textLabel.setText("Game Over! Score: " + score);
                        setMoleTimer.stop();
                        setPlantTimer.stop();
                        for (int i = 0; i < 9; i++) {
                            board[i].setEnabled(false);
                        }
                    }
                }
            });
        }

        // Timer for mole
        setMoleTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currMoleTile != null) {
                    currMoleTile.setIcon(null);
                    currMoleTile = null;
                }
                int num = random.nextInt(9);
                JButton tile = board[num];
                if (currPlantTile == tile) return; // Skip if occupied by plant
                currMoleTile = tile;
                currMoleTile.setIcon(moleIcon);
            }
        });
        setMoleTimer.start();

        // Timer for plant
        setPlantTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currPlantTile != null) {
                    currPlantTile.setIcon(null);
                    currPlantTile = null;
                }
                int num = random.nextInt(9);
                JButton tile = board[num];
                if (currMoleTile == tile) return; // Skip if occupied by mole
                currPlantTile = tile;
                currPlantTile.setIcon(plantIcon);
            }
        });
        setPlantTimer.start();

        // Set up the control panel
        controlPanel.setLayout(new FlowLayout());
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        controlPanel.add(restartButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Restart game logic
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        frame.setVisible(true);
    }

    // Method to reset the game
    public void resetGame() {
        score = 0;
        textLabel.setText("Score: 0");
        currMoleTile = null;
        currPlantTile = null;
        for (int i = 0; i < 9; i++) {
            board[i].setEnabled(true);
            board[i].setIcon(null);
        }
        setMoleTimer.start();
        setPlantTimer.start();
    }
}
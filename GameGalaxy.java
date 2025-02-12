import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;

class MazeGame {
    int WIDTH = 30;
    int HEIGHT = 30;
    char WALL = '*';
    char PATH = ' ';
    char PLAYER = 'P';
    char EXIT = 'E';

    char[][] maze;
    int playerX, playerY;

    JFrame frame;
    JPanel panel;

    MazeGame() {
        generateMaze();
        placePlayer();
        placeExit();
        createAndShowGUI();
    }

    void generateMaze() {
        maze = new char[HEIGHT][WIDTH];

        for (int i = 0; i < HEIGHT - 1; i++) {
            for (int j = 0; j < WIDTH - 1; j++) {
                maze[i][j] = WALL;
            }
        }

        carvePaths(1, 1);
    }

    void carvePaths(int x, int y) {
        Random random = new Random();
        int[][] directions = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } };

        for (int i = 0; i < directions.length; i++) {
            int[] temp = directions[i];
            int swapIndex = random.nextInt(directions.length);
            directions[i] = directions[swapIndex];
            directions[swapIndex] = temp;
        }

        for (int[] dir : directions) {
            int dx = x + dir[0] * 2;
            int dy = y + dir[1] * 2;

            if (isInBounds(dx, dy) && maze[dy][dx] == WALL) {
                maze[y + dir[1]][x + dir[0]] = PATH;
                maze[dy][dx] = PATH;
                carvePaths(dx, dy);
            }
        }
    }

    boolean isInBounds(int x, int y) {
        return x > 0 && y > 0 && x < WIDTH - 1 && y < HEIGHT - 1;
    }

    void placePlayer() {
        playerX = 1;
        playerY = 1;
        maze[playerY][playerX] = PLAYER;
    }

    void placeExit() {
        maze[HEIGHT - 3][WIDTH - 3] = EXIT;
    }

    void createAndShowGUI() {
        frame = new JFrame("Maze Game");
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                renderMaze(g);
            }
        };

        panel.setPreferredSize(new Dimension(WIDTH * 20, HEIGHT * 20));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        panel.setFocusable(true);
    }

    void renderMaze(Graphics g) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (maze[i][j] == WALL) {
                    g.setColor(Color.BLACK);
                } else if (maze[i][j] == PLAYER) {
                    g.setColor(Color.BLUE);
                } else if (maze[i][j] == EXIT) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * 20, i * 20, 20, 20);
            }
        }
    }

    void handleKeyPress(KeyEvent e) {
        int newX = playerX, newY = playerY;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                newY--;
                break;
            case KeyEvent.VK_LEFT:
                newX--;
                break;
            case KeyEvent.VK_DOWN:
                newY++;
                break;
            case KeyEvent.VK_RIGHT:
                newX++;
                break;
            default:
                return;
        }

        if (maze[newY][newX] == WALL) {
            System.out.println("You hit a wall!");
        } else if (maze[newY][newX] == EXIT) {
            System.out.println("You win!");
            JOptionPane.showMessageDialog(frame, "You win!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            maze[playerY][playerX] = PATH;
            playerX = newX;
            playerY = newY;
            maze[playerY][playerX] = PLAYER;
        }
        panel.repaint();
    }

    void play() {
        SwingUtilities.invokeLater(MazeGame::new);
    }
}

class SnackGame {
    char[][] board = new char[40][40];
    char snack = 'O';
    char empty = ' ';
    char head = 'X';
    char food = 'F';
    int[] headPos = new int[2];
    int snakeLength = 1;
    String direction = "RIGHT";
    int[][] snakeBody = new int[1600][2];
    JPanel panel;
    JFrame frame = new JFrame();
    Timer gameStartTimer;

    SnackGame() {
        // Initialize the board and snake
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                board[i][j] = empty;
            }
        }
        headPos[0] = 20;
        headPos[1] = 20;
        snakeBody[0][0] = headPos[0];
        snakeBody[0][1] = headPos[1];
        board[headPos[0]][headPos[1]] = head;
        generateFood();
        createAndShowGUI();

        gameStartTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the game by starting the movement timer
                startGame();
                gameStartTimer.stop(); // Stop the delay timer
            }
        });
        gameStartTimer.start(); // Start the delay timer
    }

    void startGame() {
        // Start the timer for automatic snake movement after the delay
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSnake(); // Move the snake every 100 ms
                panel.repaint(); // Repaint the board
            }
        });
        timer.start(); // Start the movement timer
    }

    void showBoard(Graphics g) {
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (board[i][j] == empty) {
                    g.setColor(Color.WHITE);
                } else if (board[i][j] == head) {
                    g.setColor(Color.BLUE);
                } else if (board[i][j] == snack) {
                    g.setColor(Color.GREEN);
                } else if (board[i][j] == food) {
                    g.setColor(Color.RED);
                }
                g.fillRect(j * 20, i * 20, 20, 20);
            }
        }
    }

    void createAndShowGUI() {
        frame = new JFrame("Snack Game");
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                showBoard(g);
            }
        };

        panel.setLayout(new GridLayout(40, 40));
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        panel.setFocusable(true);
    }

    void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && !direction.equals("DOWN")) {
            direction = "UP";
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && !direction.equals("UP")) {
            direction = "DOWN";
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) {
            direction = "LEFT";
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) {
            direction = "RIGHT";
        }
    }

    void moveSnake() {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeBody[i][0] = snakeBody[i - 1][0];
            snakeBody[i][1] = snakeBody[i - 1][1];
        }

        switch (direction) {
            case "UP":
                headPos[0]--;
                break;
            case "DOWN":
                headPos[0]++;
                break;
            case "LEFT":
                headPos[1]--;
                break;
            case "RIGHT":
                headPos[1]++;
                break;
        }

        snakeBody[0][0] = headPos[0];
        snakeBody[0][1] = headPos[1];

        if (board[headPos[0]][headPos[1]] == food) {
            snakeLength++;
            generateFood();
        }

        // Check for collision with itself (game over logic can be added here)

        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                board[i][j] = empty;
            }
        }

        for (int i = 0; i < snakeLength; i++) {
            int x = snakeBody[i][0];
            int y = snakeBody[i][1];
            if (i == 0) {
                board[x][y] = head;
            } else {
                board[x][y] = snack;
            }
        }
    }

    void generateFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(40);
            y = random.nextInt(40);
        } while (board[x][y] != empty);
        board[x][y] = food;
    }

    void eatfood() {
        snakeLength++;
        generateFood();
    }

    public static void main(String[] args) {
        new SnackGame();
    }
}

class TicTacToe {

    JFrame frame;
    JPanel panel;
    JButton[][] buttons = new JButton[3][3];
    char[][] board = { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } };
    char currentPlayer = 'X';
    boolean gameWon = false;
    int moves = 0;

    public TicTacToe() {
        createAndShowGUI();
    }

    void ticTacToe() {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }

    void createAndShowGUI() {
        frame = new JFrame("Tic Tac Toe");
        panel = new JPanel(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(String.valueOf(board[i][j]));
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.blue);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                panel.add(buttons[i][j]);
            }
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    class ButtonClickListener implements ActionListener {
        int row, col;

        ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameWon || !buttons[row][col].getText().equals(String.valueOf(board[row][col]))) {
                return;
            }

            makeMove(row, col, currentPlayer);
            buttons[row][col].setText(String.valueOf(currentPlayer));

            if (checkWin(currentPlayer)) {
                JOptionPane.showMessageDialog(frame, "Player " + currentPlayer + " wins!");
                gameWon = true;
                return;
            }

            moves++;
            if (moves >= 9) {
                JOptionPane.showMessageDialog(frame, "It's a draw!");
                return;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';

            if (currentPlayer == 'O' && !gameWon) {
                makeComputerMove();
            }
        }
    }

    void makeMove(int row, int col, char player) {
        board[row][col] = player;
    }

    void makeComputerMove() {
        Random random = new Random();
        int row, col;
        while (true) {
            row = random.nextInt(3);
            col = random.nextInt(3);
            if (board[row][col] != 'X' && board[row][col] != 'O') {
                makeMove(row, col, currentPlayer);
                buttons[row][col].setText(String.valueOf(currentPlayer));
                break;
            }
        }

        if (checkWin(currentPlayer)) {
            JOptionPane.showMessageDialog(frame, "Computer wins! Better luck next time.");
            gameWon = true;
        }

        currentPlayer = 'X';
    }

    boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }
}

class HangMan {
    Scanner sc = new Scanner(System.in);

    void hangMan() {
        String[] words = { "hello", "world", "java", "programming", "computer" };
        Random random = new Random();
        String word = words[random.nextInt(words.length)];
        char[] guessed = new char[word.length()];
        Arrays.fill(guessed, '_');
        int attempts = 6;

        while (attempts > 0) {
            System.out.println("Word: " + new String(guessed));
            System.out.println("Attempts left: " + attempts);
            System.out.print("Guess a letter: ");
            char guess = sc.next().charAt(0);

            boolean correct = false;
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess) {
                    guessed[i] = guess;
                    correct = true;
                }
            }

            if (!correct) {
                attempts--;
            }

            if (word.equals(new String(guessed))) {
                System.out.println("You win! The word was: " + word);
                return;
            }
        }
        System.out.println("You lose! The word was: " + word);
    }
}

class Quiz {
    Scanner sc = new Scanner(System.in);

    void quiz() {
        String[] questions = { "What is the capital of France?", "What is 2 + 2?", "What is the largest planet?" };
        String[] answers = { "Paris", "4", "Jupiter" };
        int score = 0;

        for (int i = 0; i < questions.length; i++) {
            System.out.println(questions[i]);
            System.out.print("Your answer: ");
            String answer = sc.nextLine();
            if (answer.equalsIgnoreCase(answers[i])) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect! The correct answer is: " + answers[i]);
            }
        }

        System.out.println("Your score: " + score + "/" + questions.length);
    }
}

class GameGalaxy {
    Scanner sc = new Scanner(System.in);

    int selection() {
        System.out.println("1. Maze Game");
        System.out.println("2. Tic Tac Toe");
        System.out.println("3. HangMan");
        System.out.println("4. Quiz");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        return choice;
    }

    public static void main(String[] args) {
        boolean playing = true;
        while (playing) {
            GameGalaxy gameGalaxy = new GameGalaxy();

            int choice = gameGalaxy.selection();
            switch (choice) {
                case 1:
                    MazeGame game = new MazeGame();
                    game.play();
                    break;
                case 2:
                    TicTacToe ticTacToe = new TicTacToe();
                    ticTacToe.ticTacToe();
                    break;
                case 3:
                    HangMan hangMan = new HangMan();
                    hangMan.hangMan();
                    break;
                case 4:
                    Quiz quiz = new Quiz();
                    quiz.quiz();
                    break;
                case 5:
                    playing = false;

                    break;

                case 6:
                    SnackGame snackGame = new SnackGame();
                    // snackGame.main(args);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}

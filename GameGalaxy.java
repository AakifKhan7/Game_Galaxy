import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
            System.exit(0);
        } else {
            maze[playerY][playerX] = PATH;
            playerX = newX;
            playerY = newY;
            maze[playerY][playerX] = PLAYER;
        }
        panel.repaint();
    }

    
    void play(){
        SwingUtilities.invokeLater(MazeGame::new);
    }
}

class TicTacToe {
    void ticTacToe() {
        Random random = new Random();
        char[][] board = { { '1', '2', '3' }, { '4', '5', '6' }, { '7', '8', '9' } };
        char currentPlayer = 'X';
        boolean gameWon = false;
        int moves = 0;
        Scanner sc = new Scanner(System.in);

        while (!gameWon && moves < 9) {
            printBoard(board);

            // Player or Computer move
            if (currentPlayer == 'X') {
                System.out.println("Player " + currentPlayer + "'s turn:");
                int position;
                while (true) {
                    System.out.print("Enter a position (1-9): ");
                    position = sc.nextInt();
                    if (position >= 1 && position <= 9 && isValidMove(board, position)) {
                        makeMove(board, position, currentPlayer);
                        break;
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                }
            } else {
                System.out.println("Computer's turn (Player O):");
                int position;
                while (true) {
                    position = random.nextInt(9) + 1; // Generate random position (1-9)
                    if (isValidMove(board, position)) {
                        makeMove(board, position, currentPlayer);
                        break;
                    }
                }
            }

            // Check for win
            gameWon = checkWin(board, currentPlayer);
            if (gameWon) {
                printBoard(board);
                if (currentPlayer == 'X') {
                    System.out.println("Congratulations! You win!");
                } else {
                    System.out.println("Computer wins! Better luck next time.");
                }
                return;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            moves++;
        }
        printBoard(board);
        System.out.println("It's a draw!");
    }

    void printBoard(char[][] board) {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
                if (j < 2)
                    System.out.print(" | ");
            }
            System.out.println();
            if (i < 2)
                System.out.println("----------");
        }
        System.out.println();
    }

    boolean isValidMove(char[][] board, int position) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        if(board[row][col] <= '9' && board[row][col] >= '1') {
            return true;
        }
        return false;
    }

    void makeMove(char[][] board, int position, char player) {
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;
        board[row][col] = player;
    }

    boolean checkWin(char[][] board, char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) || // Row
                (board[0][i] == player && board[1][i] == player && board[2][i] == player)) { // Column
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) || // Diagonal
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);   // Anti-diagonal
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
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
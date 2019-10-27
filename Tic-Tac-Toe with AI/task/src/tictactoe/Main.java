/**
 * Known problems: StackOverflowError
 * Reason: Despite the fact that methods are static, they probably duplicate the whole
 * set of variables each time when you invoke those methods.
 */

package tictactoe;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Main class contains all needed methods to run the program.
 * It has 6 methods and the main() method.
 */


public class Main {
    /**
     * The main methods contains some variables which are transmitted to other methods.
     * It also does some actions which are performed only once when the program is launched.
     * Executes a Start Menu method and runs a loop which keeps the game working until a
     * round a over. Then executes the Start Menu method again.
     * @param args
     */
    public static void main(String[] args) {
        /**
         * @param gameMode  Defines the game mode if an array variable is true
         *                  0 - game loop is over (false - no, true - yes)
         *                  1 - Player 1 is user (puts crosses 'X')
         *                  2 - Player 1 is easy AI
         *                  3 - Player 1 is medium AI
         *                  4 - Player 1 is hard AI
         *                  5 - Player 2 is user (puts circles 'O')
         *                  6 - Player 2 is easy AI
         *                  7 - Player 2 is medium AI
         *                  8 - Player 2 is hard AI
         *                  9 - program exit (false - no, true - yes)
         */

        Random random = new Random(System.currentTimeMillis());
        int xCoordAI = 0;
        int yCoordAI = 0;
        boolean[] gameMode = new boolean[10];
        boolean isFirstPlayer = false;
        gameMode[9] = false;


        char[][] arrayField = new char[3][3];
        arrayField = cleanField(arrayField);

        while (!gameMode[9]) {
            gameMode = startMenu(gameMode);

            while (!gameMode[0]) {
                fieldPrinting(arrayField);
                if (gameMode[1]) { isFirstPlayer = true;
                    arrayField = newMove(arrayField, isFirstPlayer); }
                if (gameMode[2]) { isFirstPlayer = true;
                    arrayField = easyDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer); }
                if (gameMode[3]) { isFirstPlayer = true;
                    arrayField = mediumDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer); }
                if (gameMode[4]) { isFirstPlayer = true; //hard difficult - unused
                    arrayField = newMove(arrayField, isFirstPlayer); }

                fieldPrinting(arrayField);

                if (!gameState(arrayField).equals("Game not finished")) {
                    System.out.println(gameState(arrayField));
                    gameMode[0] = true;
                    break;
                }

                if (gameMode[5]) { isFirstPlayer = false;
                    arrayField = newMove(arrayField, isFirstPlayer); }
                if (gameMode[6]) { isFirstPlayer = false;
                    arrayField = easyDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer); }
                if (gameMode[7]) { isFirstPlayer = false;
                    arrayField = mediumDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer); }
                if (gameMode[8]) { isFirstPlayer = false; //hard difficult - unused
                    arrayField = easyDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer); }

                if (!gameState(arrayField).equals("Game not finished")) {
                    fieldPrinting(arrayField);
                    System.out.println(gameState(arrayField));
                    gameMode[0] = true;
                    break;
                }
            }
            arrayField = cleanField(arrayField);

        }
    }

    /**
     *
     * Paradoxes: in any case of the switch statement a line like
     * if (gameMode[1] || firstPlayerOver) always returns "true" though both params
     * are "false" from the beginning.
     *
     * @param gameMode
     * @return
     */
    public static boolean[] startMenu(boolean[] gameMode) {
        Scanner sc = new Scanner(System.in);
        boolean startFound = false;
        boolean firstPlayerOver = false;
        for (int i = 0; i < 9; i++) {
            gameMode[i] = false;
        }
        System.out.print("Input command: ");
        String command = sc.nextLine().trim();

        Pattern threeWords = Pattern.compile("start\\s+\\w+\\s+\\w+");
        Pattern currentWord = Pattern.compile("[a-z]+");
        Matcher threeWordsReader = threeWords.matcher(command);
        Matcher currentWordReader = currentWord.matcher(command);
        if (threeWordsReader.matches()) {
            while (currentWordReader.find()) {
                String word1 = currentWordReader.group();
                switch (word1) {
                    case "user": {
                        if (gameMode[1] || firstPlayerOver) {
                            gameMode[5] = true;
                        }
                        else { gameMode[1] = true;}
                        firstPlayerOver = true;
                        break;
                    }
                    case "easy": {
                        if (gameMode[2] || firstPlayerOver) {
                            gameMode[6] = true;
                        }
                        else { gameMode[2] = true;}
                        firstPlayerOver = true;
                        break;
                    }
                    case "medium": {
                        if (gameMode[3] || firstPlayerOver) {
                            gameMode[7] = true;
                        }
                        else { gameMode[3] = true;}
                        firstPlayerOver = true;
                        break;
                    }
                    case "hard": {
                        if (gameMode[4] || firstPlayerOver) {
                            gameMode[8] = true;
                        }
                        else { gameMode[4] = true;}
                        firstPlayerOver = true;
                        break;
                    }
                    case "start": {
                        gameMode[0] = false;
                        if (startFound) {
                            System.out.println("Found multiple \"start\"s! Please, enter again.");
                            startMenu(gameMode);
                        }
                        startFound = true;
                        break;
                    }
                    default: {
                        System.out.println("Bad parameters! Please, enter again.");
                        startMenu(gameMode);
                        break;
                    }
                }
            }
            return gameMode;
        }
        else if(command.trim().equals("exit")) {
            gameMode[0] = true;
            gameMode[9] = true;
            return gameMode;
        }
        else {
            System.out.println("Bad parameters! Please, enter again.");
            startMenu(gameMode);
        }
        return gameMode;
    }

    /**
     * Prints the actual state of the game field.
     * @param arrayField A two-dimensional char array which represents the game field with crosses and noughts.
     */
    public static void fieldPrinting(char[][] arrayField) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.println("| " + arrayField[i][0] + " " + arrayField[i][1] + " " + arrayField[i][2] + " |");
        }
        System.out.println("---------");
    }

    /**
     * Represents an easy AI difficulty. Two random coordinates are picked to put either a 'X' or a 'O',
     * depending on which player makes a turn (1st or 2nd). If they are occupied, the methods starts again
     * to pick another pair of random coordinates.
     * @param arrayField
     * @param random A random number generator which picks random numbers within a specific range.
     * @param xCoordAI
     * @param yCoordAI
     * @param isFirstPlayer A variale that indicates if the 1st player is an easy AI.
     * @return Returns a game field with one more occupied cell that shows that a turn has been made.
     */
    public static char[][] easyDifficulty(char[][] arrayField, Random random, int xCoordAI, int yCoordAI, boolean isFirstPlayer) {

        xCoordAI = 1 + random.nextInt(3);
        yCoordAI = 1 + random.nextInt(3);
        if (Character.isWhitespace(arrayField[3-xCoordAI][yCoordAI-1])) {
            System.out.println("Making move level \"easy\"");
            if (isFirstPlayer) {
                arrayField[3 - xCoordAI][yCoordAI - 1] = 'X';
                return arrayField;
            }
            else {
                arrayField[3 - xCoordAI][yCoordAI - 1] = 'O';
                return arrayField;
            }
        }
        else {
            easyDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer);
        }
        return arrayField;
    }

    /**
     *
     * @param arrayField
     * @param random
     * @param xCoordAI
     * @param yCoordAI
     * @param isFirstPlayer
     * @return
     */
    public static char[][] mediumDifficulty(char[][] arrayField, Random random, int xCoordAI, int yCoordAI, boolean isFirstPlayer) {

        // a group of committing variables to count symbols and whitespaces in all kinds of lines - begin (2)
        int foundHorizX;
        int foundHorizO;
        int spaceHorizCoordX;
        int spaceHorizCoordY;
        int foundVertX;
        int foundVertO;
        int spaceVertCoordX;
        int spaceVertCoordY;
        int foundLeftDiagX;
        int foundLeftDiagO;
        int spaceLeftDiagCoordX; //  / - diagonal
        int spaceLeftDiagCoordY;
        int foundRightDiagX;
        int foundRightDiagO;
        int spaceRightDiagCoordX; //  \ - diagonal
        int spaceRightDiagCoordY;
        // a group of committing variables to count symbols and whitespaces in all kinds of lines - end (2)

        // a double FOR-loop to determine special conditions and do a specified move (put a symbol in a needed place) - begin (3)
        for (int i = 0; i < 3; i++) {
            foundHorizX = 0;
            foundHorizO = 0;
            spaceHorizCoordX = 4;
            spaceHorizCoordY = 4;
            foundVertX = 0;
            foundVertO = 0;
            spaceVertCoordX = 4;
            spaceVertCoordY = 4;
            foundLeftDiagX = 0;
            foundLeftDiagO = 0;
            spaceLeftDiagCoordX = 4; //  / - diagonal
            spaceLeftDiagCoordY = 4;
            foundRightDiagX = 0;
            foundRightDiagO = 0;
            spaceRightDiagCoordX = 4; //  \ - diagonal
            spaceRightDiagCoordY = 4;

            for (int j = 0; j < 3; j++) {
                // counting symbols of every line - begin
                if (arrayField[i][j] == 'X') { // rows (horizontal lines)
                    foundHorizX++;
                }
                if (arrayField[i][j] == 'O') { // rows (horizontal lines)
                    foundHorizO++;
                }
                if (arrayField[i][j] == ' ') { // rows (horizontal lines)
                    spaceHorizCoordX = i;
                    spaceHorizCoordY = j;
                }
                if (arrayField[j][i] == 'X') { //columns (vertical lines)
                    foundVertX++;
                }
                if (arrayField[j][i] == 'O') { //columns (vertical lines)
                    foundVertO++;
                }
                if (arrayField[j][i] == ' ') { //columns (vertical lines)
                    spaceVertCoordX = j;
                    spaceVertCoordY= i;
                }
            }

            if (arrayField[i][i] == 'X') { //right diagonal \
                foundRightDiagX++;
            }
            if (arrayField[i][i] == 'O') { //right diagonal \
                foundRightDiagO++;
            }
            if (arrayField[i][i] == ' ') { //right diagonal \
                spaceRightDiagCoordX = i;
                spaceRightDiagCoordY = i;
            }
            if (arrayField[2-i][i] == 'X') { //left diagonal /
                foundLeftDiagX++;
            }
            if (arrayField[2-i][i] == 'O') { //left diagonal /
                foundLeftDiagO++;
            }
            if (arrayField[2-i][i] == ' ') { //left diagonal /
                spaceLeftDiagCoordX = 2-i;
                spaceLeftDiagCoordY = i;
            }
            // counting symbols of every line - end

            //checking conditions and doing a specified move - begin
            if ((foundHorizX == 2 || foundHorizO == 2) && spaceHorizCoordX != 4) {
                if (isFirstPlayer) {
                    arrayField[spaceHorizCoordX][spaceHorizCoordY] = 'X';
                   // System.out.println("Putting X in a horiz line ");
                }
                else {
                    arrayField[spaceHorizCoordX][spaceHorizCoordY] = 'O';
                   // System.out.println("Putting O in a horiz line ");
                }
                return arrayField;
            }
            if ((foundVertX == 2 || foundVertO == 2) && spaceVertCoordX != 4) {
                if (isFirstPlayer) {
                    arrayField[spaceVertCoordX][spaceVertCoordY] = 'X';
                   // System.out.println("Putting X in a vert line ");
                }
                else {
                    arrayField[spaceVertCoordX][spaceVertCoordY] = 'O';
                   // System.out.println("Putting O in a vert line ");
                }
                return arrayField;
            }
            if ((foundLeftDiagX == 2 || foundLeftDiagO == 2) && spaceLeftDiagCoordX != 4) {
                if (isFirstPlayer) {
                    arrayField[spaceLeftDiagCoordX][spaceLeftDiagCoordY] = 'X';
                   // System.out.println("Putting X in a left diag line ");
                }
                else {
                    arrayField[spaceLeftDiagCoordX][spaceLeftDiagCoordY] = 'O';
                   // System.out.println("Putting O in a left diag line ");
                }
                return arrayField;
            }
            if ((foundRightDiagX == 2 || foundRightDiagO == 2) || spaceRightDiagCoordX != 4) {
                if (isFirstPlayer) {
                    arrayField[spaceRightDiagCoordX][spaceRightDiagCoordY] = 'X';
                   // System.out.println("Putting X in a right diag line ");
                }
                else {
                    arrayField[spaceRightDiagCoordX][spaceRightDiagCoordY] = 'O';
                   // System.out.println("Putting O in a right diag line ");
                }
                return arrayField;
            }
            //checking conditions and doing a specified move - end
        }
        // a double FOR-loop to determine special conditions and do a specified move (put a symbol in a needed place) - end (3)

        //finding a random cell beforehand - begin (1)
        boolean randomMove = false;
        xCoordAI = random.nextInt(3);
        yCoordAI = random.nextInt(3);
        // System.out.println("Random nums: " + xCoordAI + " " + yCoordAI);
        if (Character.isWhitespace(arrayField[xCoordAI][yCoordAI])) {
            randomMove = true;
        }
        else {
            mediumDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer);
        }
        //finding a random cell beforehand - end (1)

        // doing a random move - begin (4)
        if (randomMove) {
            if (isFirstPlayer) {
                arrayField[xCoordAI][yCoordAI] = 'X';
              // System.out.println("Putting X in a random place ");
            }
            else {
                arrayField[xCoordAI][yCoordAI] = 'O';
               // System.out.println("Putting O in a random place ");
            }
        }
        //System.out.println("Reached the end of the method - now return ");
        return arrayField;
        // doing a random move - end (4)
    }

    /**
     * Would present a hard AI difficult by implementing a minimax algorithm. The method is unused because
     * I find it difficult to implement it.
     * @param arrayField
     * @param random
     * @param xCoordAI
     * @param yCoordAI
     * @param isFirstPlayer
     * @return
     */
    public static char[][] hardDifficulty(char[][] arrayField, Random random, int xCoordAI, int yCoordAI, boolean isFirstPlayer) {

        return arrayField;
    }

    /**
     * Reads a human player input and makes a turn if his/her input respects the defined string patterns.
     * The method uses 3 regexes and matchers to determine the kind of input. If the wrong one (or a pair of) is matched,
     * a respective massage is shown on the screen that describes that kind of error and asks to enter the coordinates
     * once again. If the entered coordinates are appropriate, a turn is made.
     * @param arrayField
     * @param isFirstPlayer
     * @return
     */
    public static char[][] newMove(char[][] arrayField, boolean isFirstPlayer) {
        //Keep in mind that the first coordinate goes from left to right
        //and the second coordinate goes from bottom to top. Also,
        //notice that coordinates start with 1 and can be 1, 2 or 3.

        StringBuilder inputContainer = new StringBuilder();
        Pattern correctInput = Pattern.compile("[1-3]\\s+[1-3]");
        Pattern wordsInput = Pattern.compile("[a-zA-z]+");
        Pattern wrongCoords = Pattern.compile("\\d\\s+\\d");
        Matcher correctInputReader = correctInput.matcher(inputContainer);
        Matcher wordsInputReader = wordsInput.matcher(inputContainer);
        Matcher wrongCoordsReader = wrongCoords.matcher(inputContainer);

        Scanner scanCoords = new Scanner(System.in);

        System.out.print("Enter the coordinates: ");
        inputContainer.append(scanCoords.nextLine().trim());

        Scanner scanInput = new Scanner(inputContainer.toString());

        if (correctInputReader.matches()) {
            int newXcoord = scanInput.nextInt();
            int newYcoord = scanInput.nextInt();

            if (!Character.isWhitespace(arrayField[3-newYcoord][newXcoord-1])) {
                System.out.println("This cell is occupied! Choose another one!");
                inputContainer.delete(0, inputContainer.length());
                newMove(arrayField, isFirstPlayer);
            }
            else {
                if (isFirstPlayer) {
                    arrayField[3-newYcoord][newXcoord-1] = 'X';
                    inputContainer.delete(0, inputContainer.length());
                    return arrayField;
                }
                else {
                    arrayField[3-newYcoord][newXcoord-1] = 'O';
                    inputContainer.delete(0, inputContainer.length());
                    return arrayField;
                }
            }
        }
        else if (wordsInputReader.matches()) {
            System.out.println("You should enter numbers! Try again!");
            inputContainer.delete(0, inputContainer.length());
            newMove(arrayField, isFirstPlayer);
        }
        else if (wrongCoordsReader.matches() && !correctInputReader.matches()) {
            System.out.println("Coordinates should be from 1 to 3!");
            inputContainer.delete(0, inputContainer.length());
            newMove(arrayField, isFirstPlayer);
        }
        else {
            System.out.println("Wrong input! Try again!");
            inputContainer.delete(0, inputContainer.length());
            newMove(arrayField, isFirstPlayer);
        }
        inputContainer.delete(0, inputContainer.length());
        return arrayField;
    }

    /**
     * Scans the game field and determines the actual game state. Firstly, it counts all X's, O's and empty cells
     * in a loop. Then checks all possible cases and returns the text that describes the actual state.
     * @param arrayField
     * @return
     */
    public static String gameState(char[][] arrayField) {
        int counterX = 0;
        int counterO = 0;
        int counterEmpty = 0;
        boolean xWins = false;
        boolean oWins = false;
        //counting X, O and spaces - begin
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (arrayField[i][j]) {
                    case('X'): {
                        counterX++;
                        break;
                    }
                    case('O'): {
                        counterO++;
                        break;
                    }
                    default: {
                        counterEmpty++;
                        break;
                    }

                }
            }
        }
        //counting X, O and spaces - end

        //if the balance of X and Y is broken, show "Impossible"
        if (Math.abs(counterX-counterO)>1 || Math.abs(counterO-counterX)>1) {
            return "Impossible";
        }

        //loop to figure out if X or Y wins - begin
        for (int i=0; i<3; i++) {
            //checking horizontal lines for X
            if (arrayField[0][i] == 'X' && arrayField[1][i] == 'X' && arrayField[2][i] == 'X') {
                xWins = true;
            }
            //checking vertical lines for X
            if (arrayField[i][0] == 'X' && arrayField[i][1] == 'X' && arrayField[i][2] == 'X') {
                xWins = true;
            }
            //checking diagonal line / for X
            if (arrayField[2][0] == 'X' && arrayField[1][1] == 'X' && arrayField[0][2] == 'X') {
                xWins = true;
            }
            //checking diagonal line \ for X
            if (arrayField[0][0] == 'X' && arrayField[1][1] == 'X' && arrayField[2][2] == 'X') {
                xWins = true;
            }

            //checking horizontal lines for O
            if (arrayField[0][i] == 'O' && arrayField[1][i] == 'O' && arrayField[2][i] == 'O') {
                oWins = true;
            }
            //checking vertical lines for O
            if (arrayField[i][0] == 'O' && arrayField[i][1] == 'O' && arrayField[i][2] == 'O') {
                oWins = true;
            }
            //checking diagonal line / for O
            if (arrayField[2][0] == 'O' && arrayField[1][1] == 'O' && arrayField[0][2] == 'O') {
                oWins = true;
            }
            //checking diagonal line \ for O
            if (arrayField[0][0] == 'O' && arrayField[1][1] == 'O' && arrayField[2][2] == 'O') {
                oWins = true;
            }
        }
        //loop to figure out if X or Y wins - end

        //if found 2 lines in the field, show "Impossible"
        if (xWins && oWins) {
            return "Impossible";
        }

        //shows that a player has won if all required conditions are respected
        if (xWins && !oWins) {
            return "X wins";
        }
        if (!xWins && oWins) {
            return "O wins";
        }

        //if found no lines and no empty cells, show "Draw"
        if (!xWins && !oWins && counterEmpty == 0) {
            return "Draw";
        }

        //if none of above returns are made, show "Game not finished"
        return "Game not finished";
    }

    /**
     * Cleans the game field by filling it with whitespaces.
     * @param arrayField
     * @return
     */
    public static char[][] cleanField (char[][] arrayField) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arrayField[i][j] = ' ';
            }
        }
        return arrayField;
    }
}

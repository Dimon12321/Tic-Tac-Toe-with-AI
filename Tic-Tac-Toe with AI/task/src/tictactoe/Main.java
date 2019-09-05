import java.util.*;

public class Main {
    public static void main(String[] args) {

        boolean isFirstPlayer = false;
        Random random = new Random(System.currentTimeMillis());
        int xCoordAI = 0;
        int yCoordAI = 0;

        //char[][] arrayField = {{'X','O',' '},{'X','O','X'},{'O','X',' '}};
        char[][] arrayField = {{'X',' ',' '},{'O','X',' '},{' ',' ','O'}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(arrayField[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        arrayField = mediumDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(arrayField[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * The "medium" level difficulty makes a move using the following process:
     * 1. If it can win in one move (if it has two in a row), it places a third to get three in a row and win.
     * 2. If the opponent can win in one move, it plays the third itself to block the opponent to win.
     * 3. Otherwise, it makes a random move.
     *
     * (1) Firstly, it picks a pair of random coordinates where it will put a symbol in case no special conditions are found.
     * If that cell is occupied, the method is recalled to pick a pair of new random coordinates.
     *
     * (2) Then, a group of variables are initialized to count symbols and whitespaces in a horizontal line (contains "Horiz" in their names),
     * a vertical line (contains "Vert" in their names), a diagonal line from bottom to top and from top to bottom
     *
     * (3) In a FOR-loop, each symbol is counted in each line. If found a whitespace, its coordinates are memorized (in spaceHorizCoordX, for example).
     * If a special condition is found, a symbol is put in a memorized coordinates of a whitespace to make a full line.
     * Then a new arrayField is returned.
     *
     * (4) If no special conditions are found during the whole loop, a symbol is put in the array in a random place (by specified
     * xCoordAI and yCoordAI). Then a new arrayField is returned.
     * @param arrayField an array of chars 3x3, represents the game field
     * @param random time-based RNG to pick a random cell on the game field
     * @param xCoordAI random coordinate X
     * @param yCoordAI random coordinate Y
     * @param isFirstPlayer variable taken from outside that shows if AI should put a cross/nought
     * @return returns a changed arrayField with a new cross/nought put
     */

    public static char[][] mediumDifficulty(char[][] arrayField, Random random, int xCoordAI, int yCoordAI, boolean isFirstPlayer) {

        //finding a random cell beforehand - begin (1)
        boolean randomMove = false;
        xCoordAI = random.nextInt(3);
        yCoordAI = random.nextInt(3);
        System.out.println("Random nums: " + xCoordAI + " " + yCoordAI);
        if (Character.isWhitespace(arrayField[xCoordAI][yCoordAI])) {
            randomMove = true;
        }
        else {
            mediumDifficulty(arrayField, random, xCoordAI, yCoordAI, isFirstPlayer);
        }
        //finding a random cell beforehand - end (1)

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
                    System.out.println("Putting X in a horiz line ");
                }
                else {
                    arrayField[spaceHorizCoordX][spaceHorizCoordY] = 'O';
                    System.out.println("Putting O in a horiz line ");
                }
                return arrayField;
            }
            if ((foundVertX == 2 || foundVertO == 2) && spaceVertCoordX != 4) {
                if (isFirstPlayer) {
                    arrayField[spaceVertCoordX][spaceVertCoordY] = 'X';
                    System.out.println("Putting X in a vert line ");
                }
                else {
                    arrayField[spaceVertCoordX][spaceVertCoordY] = 'O';
                    System.out.println("Putting O in a vert line ");
                }
                return arrayField;
            }
            if ((foundLeftDiagX == 2 || foundLeftDiagO == 2) && spaceLeftDiagCoordX != 4) {
                if (isFirstPlayer) {
                    arrayField[spaceLeftDiagCoordX][spaceLeftDiagCoordY] = 'X';
                    System.out.println("Putting X in a left diag line ");
                }
                else {
                    arrayField[spaceLeftDiagCoordX][spaceLeftDiagCoordY] = 'O';
                    System.out.println("Putting O in a left diag line ");
                }
                return arrayField;
            }
            if ((foundRightDiagX == 2 || foundRightDiagO == 2) || spaceRightDiagCoordX != 4) {
                if (isFirstPlayer) {
                    arrayField[spaceRightDiagCoordX][spaceRightDiagCoordY] = 'X';
                    System.out.println("Putting X in a right diag line ");
                }
                else {
                    arrayField[spaceRightDiagCoordX][spaceRightDiagCoordY] = 'O';
                    System.out.println("Putting O in a right diag line ");
                }
                return arrayField;
            }
            //checking conditions and doing a specified move - end
        }
        // a double FOR-loop to determine special conditions and do a specified move (put a symbol in a needed place) - end (3)

        // doing a random move - begin (4)
        if (randomMove) {
            if (isFirstPlayer) {
                arrayField[xCoordAI][yCoordAI] = 'X';
                System.out.println("Putting X in a random place ");
            }
            else {
                arrayField[xCoordAI][yCoordAI] = 'O';
                System.out.println("Putting O in a random place ");
            }
        }
        System.out.println("Reached the end of the method - now return ");
        return arrayField;
        // doing a random move - end (4)

    }
}
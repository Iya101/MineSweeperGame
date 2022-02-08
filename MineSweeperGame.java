package cs1302.game;

import java.lang.Math;

import java.util.ArrayList;

import java.util.Scanner;

import java.io.FileNotFoundException;

import java.io.*;

import java.lang.NumberFormatException;

import java.math.RoundingMode;

import java.text.DecimalFormat;



/**
 * This is a class called Minesweeper game, Once an instance is made of it in a driver file,
    you can call the play method and play the game.
 */



public class MinesweeperGame {


    /**
     *Represents the Scanner named stdin utilized to take command line input,
       and read commands from a sample file.
     */

    private final Scanner stdIn;

    /**
     * This variable holds the seed file passed in.
     *
     */

    String chosenDisplay = "";

    /**
     * This variable holds the value for rows of the 2d array.
     */

    public int row = 0;

    /**
     * This variable holds the value for columns of the 2d array.
     */

    public int col = 0;



    /**
     * This variable holds the value for the amount of mines passed in the seed file.
     */

    private int mineTotal = 0;

    /**
     * This is a boolean variabe that allows the no fog command to be turned on.
     */


    private boolean noFog = false;



    /**
     * This variable declares the 2d array that will contain
     * the locations of the mines passed in the seed file.
     */

    char [][] mineLocations;

    /**
     * This is a variable that will be incremented as each round occurs,
     * it will later be used to calculate the user's score.
     */

    int roundsCompleted = 0;

    /**
     * This 2d array of type String is the actual array used to display the grid to the player.
     */

    String [][] userMap;



    /**
     * This is the ArrayList utilized to parse the information
     *from the seed file and assign it to the relevant variables.
     */

     ArrayList <Integer> myList = new ArrayList<Integer>();



    /**
     *
     * @param stdIn
     * @param seedPath
     * This is the {@code MinesweeperGame } constructor that initializes the passed in parameters.
     * This method also reads in the seed path
     *, saves each line to an array utilizing the .split() string method.
     * Then it converts each element of the string array into an integer.
     * each element is then added to an array list called {@code myList}.
     * using the .get() method from the array list,
     * each index from the array list is assigned to its proper attribute.
     * This method also initializes the game board array that is shown to the actual user.
     * This method also sets the mine locations provided by the seed file.
     * It also ensured that the coordinates are in bounds, or else it will close the program.
     * @throws FileNotFoundException
     * @throws NumberFormatException
     */


    MinesweeperGame(Scanner stdIn, String seedPath) { //scanner part

        this.stdIn = stdIn;

        chosenDisplay = seedPath;

        row = 0;

        col = 0;

        mineTotal = 0;

        try {

            String data = "";

            File configFile = new File(seedPath);

            Scanner configScanner = new Scanner(configFile);

            int error = 0;

            while (configScanner.hasNextLine()) {

                data = configScanner.nextLine();

                //trims the beginning and end of a string that ends of white space, and splits the string into an array with whitespace.

                String[] myArray = data.trim().split("\\s+");

                try {
                    //iterates through the array containing the current line read in.
                    //It parses each element into an integer and adds it to an array list
                    //for debugging purposes we marked each potential string index as error

                    for (int i = 0; i < myArray.length; i++) {

                        error = i;

                        myList.add(Integer.parseInt(myArray[i]));
                    }

                }  catch (NumberFormatException e) {

                    for(int i = 0; i < myArray.length; i++) {

                        System.err.println("Seed File Malformed Error: your input was improper,  one of your tokens was not a number. Located in: " + error);

                        System.exit(3);
                    }
                }
            }

            //checks that the input passed into the array list is valid, and fits the parameters
            checkSeedInput();

            row = myList.get(0);

            col = myList.get(1);

            mineTotal = myList.get(2);

            mineLocations = new char[row][col];

            userMap = new String[row][col];

            //intializes the board that is shown to the user, and also initalizes the minelocations board.

            for (int i = 0; i < userMap.length; i++) {

                for (int j = 0; j < userMap[i].length; j++) {

                    mineLocations[i][j] = ' ';

                    userMap[i][j] = " ";
                }
            }
            //starts off the forloop below at 3 so I can acess the mine coordinates

            int i = 3;

            int mineAmount = 0;
            //iterates through the indexes containing the mine coordinates and assigns their location
            //the i = i + 2 allows me to acces every other starting index of a mine coordinate
            //otherwise some indexes will be repeated.

            while (i < myList.size() && mineAmount < mineTotal) {

                mineLocations[myList.get(i)][myList.get(i+1)] = 'm';

                mineAmount++;

                i = i + 2;
            }

        } catch (FileNotFoundException e) {

            System.err.println(e);

            e.printStackTrace();

            System.exit(2);
        }
    }

    /**
     * Prints the mine field using the userMap 2d array.
     * This method also checks if the nofog command is activated.
     * if nofog is true and the mineLocations 2d array at the exact i,j coordinates equals a mine,
     * then two arrows will be displayed on the userMap 2d array where the mines are located.
     *
     */




    public void printMineField() {

        for (int i = 0; i < userMap.length; i++) {
            System.out.print(i + " |");

            for (int j = 0; j < userMap[i].length; j++) {

                if(noFog && mineLocations[i][j] == 'm') {

                    System.out.print(  "<"+userMap[i][j] + ">|" );


                } else {

                    System.out.print( " "+userMap[i][j] + " |");
                }
            }

            System.out.println();
        }
        System.out.print(" ");
        for (int i = 0; i < col; i++){
            System.out.print("   "+i);
        }
        noFog = false;


    }


    /**
     * prints out the game over banner and quits the game with an exit status of zero.
     */


    public void printLoss(){
        System.out.println("Oh no... You revealed a mine!");
        System.out.println();
        System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __");
        System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
        System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |");
        System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|");
        System.out.println("  |___/");
        System.exit(0);



    }


    /**
     * This method checks if the coordinates passed into this method comntain a mine.
     * This method also checks the number of adjacent mines, and then places the counter variable in the userMap array.
     * Proceeding this the method places an r on the coordinates passed in the mineLocations array.
     * This method also consumes a round so the roundCounter variable is incremented.
     * This method will also check if the isWon method returns true, if so it will print the score.
     * @param i
     * @param k
     */

    public void revealIt(int i, int k){

        if(mineLocations[i][k] == 'm'){

            printLoss();




        }
        int count = 0;
        //checks adjacent coordinates of coordinates passed in
        for (int m = i-1; m < i+2; m++) {

            for(int j = k-1; j < k+2; j++) {

                if(m < row && m > -1 && j < col && j > -1){

                    if(mineLocations[m][j] == 'm'){

                      count++;


                    }

                }

            }
        }


        userMap[i][k] = String.valueOf(count); // Since this loop checks adjacent squares for mines,
                                               // I turned the counter variable into a string and displayed that to the user.

        mineLocations[i][k] = 'r';

        roundsCompleted++;



    }

    /**
     * This method checks that the coordinates passed in as parameters are not out of bounds.
     * If the parameters are within bounds then place and "F" on the userMap 2d array.
     * This method will also check if the isWon method returns true, if so it will print the score.
     * This method will also increment the roundsCompleted variable.
     * @param i
     * @param k
     */



    public void markIt(int i, int k) {
        //checks if coordinates passed in are within bounds
        if(i > -1 && i < row && k > -1 && k < col) {

            userMap[i][k] = "F";
            //this if statement is used to see if a user has one the game.
            //I added the F to mine locations if there wasnt a mine where the user selected
            //This allowed me in the end to check if all squares had either been revealed or marked in the end.

            if(mineLocations[i][k] != 'm') {

                mineLocations[i][k] = 'F';

            }

            roundsCompleted++;




        }

    }


    /**
     * This method checks that the coordinates passed in as parameters are not out of bounds.
     * If the parameters are within bounds then place and "?" on the userMap 2d array.
     * This method will also check if the isWon method returns true, if so it will print the score.
     * This method will also increment the roundsCompleted variable.
     * @param i
     * @param k
     */



    public void guessIt(int i, int k){
        if(i > -1 && i < row && k > -1 && k < col) {

            userMap[i][k] = "?";
            //same thing I was doing in markIt ^^

            if(mineLocations[i][k] != 'm') {

                mineLocations[i][k] = '?';

            }


            roundsCompleted++;
        }


    }

    /**
     * prints out the game welcome banner.
     */




    public void printWelcome() {
        System.out.println("        _");
        System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __");
        System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|");
        System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
        System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
        System.out.println("                                     ALPHA EDITION |_| v2021.fa ");
        System.out.println();

    }

    /**
     *This method reads in input from the user.
     * It reads the text or input line by line and uses the .split() method.
     * The method then utilizes the individual indexes of the array to check which command is being given.
     * Proceeding this the method passes in the array containing the three words that make up the command to the isValidInput() method.
     * for the commands mark and reveal, it checks if the isWon method has returned true.
     */


    public void promptUser() {



            String userInput = stdIn.nextLine();

            String[] myTempArray = userInput.trim().split("\\s+");



            //takes in each individual first index of the command given and passes it to the correct method.
            switch (myTempArray[0].toLowerCase()) {
                case "r":
                case "reveal":
                    //ensures that any command is valid within myTempArray
                    if (isValidInput(myTempArray)) {

                        revealIt(Integer.parseInt(myTempArray[1]), Integer.parseInt(myTempArray[2]));

                        //for mark it and reveal after each method call, there is a check to see if the user has won.
                        if (isWon()) {
                           printWin();
                           System.exit(0);
                        }
                    }
                    break;


                case "m":
                case "mark":

                    if (isValidInput(myTempArray)) {

                        markIt(Integer.parseInt(myTempArray[1]), Integer.parseInt(myTempArray[2]));

                        if (isWon()) {
                            printWin();
                            System.exit(0);
                        }
                    }

                    break;


                case "g":
                case "guess":

                    if (isValidInput(myTempArray)) {

                        guessIt(Integer.parseInt(myTempArray[1]), Integer.parseInt(myTempArray[2]));


                    }
                    break;

                case "h":
                case "help":
                    if(myTempArray.length == 1) {

                        System.out.println("Commands Available...\n" +
                                " - Reveal: r/reveal row col\n" +
                                " -   Mark: m/mark   row col\n" +
                                " -  Guess: g/guess  row col\n" +
                                " -   Help: h/help\n" +
                                " -   Quit: q/quit");

                        roundsCompleted++;

                    } else {

                        System.err.println("Invalid Command: Command not recognized!");
                    }
                    break;

                case "q":
                case "quit":
                    if(myTempArray.length == 1) {

                        System.out.println("Quitting the game...");

                        System.out.println("Bye!");

                        System.exit(0);

                    } else {
                        System.err.println("Invalid Command: Command not recognized!");
                    }
                    break;

                case "nofog":
                    //ensures that for commands like nofog or quit or help that the length is only 1
                    if(myTempArray.length == 1) {

                        noFog = true;

                        roundsCompleted++;
                    } else {

                        System.err.println("Invalid Command: Command not recognized!");
                    }

                    break;




                default:

                    System.err.println("Invalid Command: Command not recognized!");


            }


        }


    /**
     * This method checks if the user has met all the necessary conditions needed to win the game.
     * The conditions it checks are: if the the user has revealed all the squares, and placed a mark on where all the potential mines are located.
     * The method also checks if the array at a certain location contains a mine, it then increments the mineCounter variable.
     * This variable is used at the end to see if the user has found all the mines and flagged them, it then checks this value against the mineTotal variable.
     * @return true if the conditions are met, else false if not.
     */





    public boolean isWon() {

        int mineCounter = 0;

        if (roundsCompleted >= row * col) {

            for (int i = 0; i < userMap.length; i++) {

                for (int j = 0; j < userMap.length; j++) {
                    //makes sure that the board that is hidden from the user is completely revealed
                    //or that they have a mine in them
                    if (mineLocations[i][j] == 'r' || mineLocations[i][j] == 'm') {

                        if (mineLocations[i][j] == 'm' && userMap[i][j].equals("F") ) {

                            mineCounter++;

                        } //This if statement checks to see if the user was able to find the correct amount of mines.


                    } else {
                        return false;

                    }

                }
            }
            //if the user found all the mines then declare this as true.
            if (mineCounter == mineTotal) {
                return true;
            } else {
                return false;
            }


        }  else {
            return false;
        }


    }


    /**
     * This method checks that as input comes in line by line and is saved into an array to be parsed into an integer, that the string its self is in bounds.
     * Specifically it checks that the length of the commands received are equal to three.
     * It also checks that the coordinate values are not just null.
     * if the coordinates pass those previous checks they are parsed into an integer and checked to ensure they are in bounds.
     * This method also ensures that the individual coordinates afrter being parsed are in bounds as well.
     * @param myTempArray
     * @return true if the coordinates are in bounds, false if not.
     *
     * @throws NumberFormatException if the numbers entered are not formatted correctly.
     */

    public boolean isValidInput(String [] myTempArray) {

        //this checks that the command length is equal to three for mark, guess, and reveal
        if( myTempArray.length != 3) {

            System.err.println();

            System.err.println(" Invalid user input: your command did not have enough parameters or too many parameters. Your command:  " + myTempArray[0] + " requires only an x and y value.");

            return false;
        }
        //if any commands come in as spaces or just not entered
        if (myTempArray[1] == null || myTempArray[2]== null) {

            System.err.println();

            System.err.println("Invalid user input: your index(es) are invalid, or missing:  " + myTempArray[1] + "," + myTempArray[2]);

            return false;
        }


       try {
           //if the command passes the previous checks above it is then checked to be in bounds again.
           int x = Integer.parseInt(myTempArray[1]);

           int y  = Integer.parseInt(myTempArray[2]);

           if(x < 0 || x >= row || y < 0 || y >= col ){

               System.err.println();

               System.err.println("Invalid user input:  your index(es) are out of bounds for the coordinate grid provided:  " + myTempArray[1] + "," + myTempArray[2]);

               return false;
           } else {
               return true;
           }

       }catch (NumberFormatException e) {

           System.err.println();

           System.err.println("Invalid user input:  your index(es) are invalid:  " + myTempArray[1] + "," + myTempArray[2]);

           return false;


       }

    }

    /**
     * prints out the win banner and calculates the users score.
     */

    public void printWin() {

        DecimalFormat Format = new DecimalFormat("###.##");
        //the rounding aspect of this project was done using this method, I had to specify that it needed to round up
        //instead of down.
        Format.setRoundingMode(RoundingMode.CEILING);


        double score =  (100.0 * row * col) / roundsCompleted;









            System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"");
            System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
            System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"");
            System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
            System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"");
            System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
            System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"");
            System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
            System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
            System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
            System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
            System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒");
            System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
            System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
            System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
            System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
            System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!");
            System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!");
            System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " + Format.format(score));








    }

    /**
     * This method officially runs the game and allows the user to enact all necessary methods.
     * It is encompassed by a while loop that moves through each of the lines in the sample input file, or simply user input.
     */

    public void play () {

        printWelcome();

        printMineField();

        System.out.println();

        while (stdIn.hasNextLine()) {

            promptUser();

            System.out.println("Rounds Completed: " + roundsCompleted);

            System.out.println();

            printMineField();

            System.out.println();



        }
    }

    /**
     * This method validates that all of the integers inputted within the seed file exist and are in bounds.
     * It will check and ensure that at least one mine is given, with at least one provided coordinate.
     * The method will also check that the coordinates for the mine field are within bounds as well.
     * If either error occours the method exits with a status of 3 and provides information about the malformed seed file.
     */

    public void checkSeedInput() {
        //if the seed file entered after being parsed is not at least length 5 then it is wrong.
        if(myList.size() < 5){

            System.err.println();

            System.err.println("Seed File Malformed Error: a token is expected but is not found");

            System.exit(3);;
        }

        if(myList.get(0) < 5 || myList.get(0) > 10 ||  myList.get(1) < 5 || myList.get(1) > 10) {

            System.err.println();

            System.err.println("Seed File Malformed Error: rows and columns need to be at least 5 and no greater than 10 Rows: " + myList.get(0) + " col: " +myList.get(1));

            System.exit(3);






        }
        //ensures that the amount of mines passed in are not greater than the coordinates of the grid multiplied minus 1.

        if(myList.get(2) < 1 || myList.get(2) > myList.get(0) * myList.get(1)-1) {

            System.err.println();

            System.err.println("Seed File Malformed Error:  mine total must be at least 1, and no greater than " + (myList.get(0) * myList.get(1)-1)+ " your mine total is: " + myList.get(2));

            System.exit(3);

        }

        //make sure you are making you are getting a valid seed file.



        if( myList.size() < (3 + myList.get(2) * 2)) {

            System.err.println();

            System.err.println("Seed File Malformed Error: a token is expected but is not found, missing index(s).");

            System.exit(3);

        }



        int i = 3;

        while (i < myList.size()) {
            //ensures that the mine coordinates passed in are in bounds,
           // not smaller than 0 and no bigger than the grid size.

            if(myList.get(i) < 0 || myList.get(i) > myList.get(0) || myList.get(i+1) < 0 || myList.get(i+1) > myList.get(1)) {

                System.err.println();

                System.err.println("Seed File Malformed Error: Location of mines is not in bound at coordinates: " + myList.get(i) + "," + myList.get(i+1));

                System.exit(3);

            }

            i = i + 2;
        }








    }




}

package cs1302.game;

import cs1302.game.MinesweeperGame;

import java.util.Scanner;

public class MinesweeperDriver{

     public static void main(String[] args) {



          if (args.length != 1) {

            System.err.println();

            System.err.println("Invalid usage error, unexpected amount of command line arguments");

            System.exit(1);
        }



        Scanner stdin = new Scanner(System.in);

        //takes in the first passed in command line argument.
        MinesweeperGame m1 = new  MinesweeperGame( stdin , args[0]);

        m1.play();

    }


}

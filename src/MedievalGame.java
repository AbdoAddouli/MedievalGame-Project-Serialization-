import java.util.Scanner;
import java.util.Objects;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class MedievalGame {

    /* Instance Variables */
    private Player player;

    /* Main Method */
    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);
        MedievalGame game = new MedievalGame();

        game.player = game.start(console);

        game.addDelay(500);
        System.out.println("\nLet's take a quick look at you to make sure you're ready to head out the door.");
        System.out.println(game.player);

        game.addDelay(1000);
        System.out.println("\nWell, you're off to a good start, let's get your game saved so we don't lose it.");
        game.save();

        game.addDelay(2000);
        System.out.println("We just saved your game...");
        System.out.println("Now we are going to try to load your character to make sure the save worked...");

        game.addDelay(1000);
        System.out.println("Deleting character...");
        String charName = game.player.getName();
        game.player = null;

        game.addDelay(1500);
        game.player = game.load(charName, console);
        System.out.println("Loading character...");

        game.addDelay(2000);
        System.out.println("Now let's print out your character again to make sure everything loaded:");

        game.addDelay(500);
        System.out.println(game.player);
    } // End of main

    /* Instance Methods */
    private Player start(Scanner console) {
        Player player;
        Art.homeScreen(); // Retaining this method call
        System.out.println("Welcome to your latest adventure!");
        System.out.println("Tell me traveler, have you been here before?");
        System.out.print("   Enter 'y' to load a game, 'n' to create a new game: ");
        String answer = console.next().toLowerCase();

        while (true) {
            addDelay(500);
            if (answer.equals("y")) {
                System.out.println("\nAhh... I knew I remembered you, what was your name again? Let me see if I can find your backpack:");
                return load(console.next(), console);
            } else if (answer.equals("n")) {
                System.out.print("\nWell then, don't be shy, go ahead and tell me your name: ");
                String possibleName = console.next();
                while (true) {
                    System.out.println("Welcome " + possibleName + ", am I pronouncing that correctly? (Enter 'y' to confirm, 'n' to enter a new name)");
                    String nameResponse = console.next().toLowerCase();
                    if (Objects.equals(nameResponse, "y"))
                        break;
                    System.out.println("So sorry, can you spell it for me again?");
                    possibleName = console.next();
                }
                player = new Player(possibleName);
                return player;
            } else {
                System.out.print("Sorry adventurer, I only speak the common tongue. Please enter 'y' to load a game or 'n' to start a new game: ");
                answer = console.next().toLowerCase();
            }
        }
    } // End of start

    private void save() {
        String fileName = player.getName() + ".svr";
        try (FileOutputStream userSaveFile = new FileOutputStream(fileName);
             ObjectOutputStream playerSaver = new ObjectOutputStream(userSaveFile)) {
            playerSaver.writeObject(player);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("The game couldn't be saved.");
        }
    } // End of save

    private Player load(String playerName, Scanner console) {
        Player loadedPlayer;
        try (FileInputStream userSaveFile = new FileInputStream(playerName + ".svr");
             ObjectInputStream playerLoader = new ObjectInputStream(userSaveFile)) {
            loadedPlayer = (Player) playerLoader.readObject();
            System.out.println("Character loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            addDelay(1500);
            System.out.println("\nThere was a problem loading your character, we've created a new player with the name you entered.");
            System.out.println("If you're sure the spelling is correct, your character file may no longer exist, please reload the game if you'd like to try again.");
            System.out.println("In the meantime, we'll create you a new character with the name: " + playerName);
            addDelay(2000);
            loadedPlayer = new Player(playerName);
        }
        return loadedPlayer;
    } // End of load

    // Adds a delay to the console so it seems like the computer is "thinking"
    // or "responding" like a human, not instantly like a computer.
    private void addDelay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Serializable Player class
class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "'}";
    }
}

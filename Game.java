import java.util.HashSet;


/**
 *  This class is the main class of the "Tomb Raider" application.
 *  "Tomb Raider" is a very simple, text based adventure game.  Users
 *  can walk around some scenery and may find holy grail or scary monsters.
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Hilary Ogalagu, Shinhyeok Kang
 * @version 2020.04.28
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private HashSet<Item> items;  // items the player keeps  
    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        currentRoom = createRooms();
        parser = new Parser();
        items = new HashSet<Item>();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    public Room createRooms()
    {
        Room start, temple, jungle, dungeon, holychamber1, holychamber2, holychamber3, aligator, demon, mummy;
        Item holygrail = new Item("Holy Piece");
        // create the rooms
        start = new Room("in the starting point of the game");
        temple = new Room("in a Temple, go north or south to earn reward");
        mummy = new Room("eaten by mummies,exit the game or restart and try again");
        jungle = new Room("in the jungle,go north or south to earn reward");
        dungeon = new Room("in a dungeon,go west or east to earn reward");
        demon = new Room("eaten by demons,exit the game or restart and try again");
        aligator = new Room("eaten by aligator,exit the game or restart and try again");
        holychamber1 = new Room("rewarded with holygrail.congratulations");
        holychamber1.placeItem(holygrail);
        holychamber2 = new Room("rewarded with holygrail.congratulations");
        holychamber2.placeItem(holygrail);
        holychamber3 = new Room("rewarded with holygrail.congratulations");
        holychamber3.placeItem(holygrail);
       
       
       
        // initialise room exits
        start.setExit("east", temple);
        start.setExit("north", dungeon);
        start.setExit("west", jungle);

        temple.setExit("west", start);
        temple.setExit("north",mummy);
        temple.setExit("south",holychamber1);
       
       

        jungle.setExit("east", start);
        jungle.setExit("north", holychamber3);
        jungle.setExit("south", aligator);

        dungeon.setExit("south", start);
        dungeon.setExit("east", demon);
        dungeon.setExit("west", holychamber2);
       
       
        //demon.setExit("west", dungeon);
        holychamber2.setExit("east", dungeon);
        holychamber3.setExit("north", jungle);
        holychamber1.setExit("north", temple);
        //aligator.setExit("south", jungle);
       
        return start;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
               
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World Tomb Raider!");
        System.out.println("Tomb Raider is a new, incredibly difficult adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

         if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
           
        }
     
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You are tomb raider who want to find 3 pieces of holy grail. You are alone. You wander");
        System.out.println("around the danger places to find all pieces..");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}

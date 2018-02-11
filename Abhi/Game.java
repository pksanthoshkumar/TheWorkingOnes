
import java.util.*;
/** 
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Abhiram Santhosh
 * @version October 20, 2017
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom; //the current room the player is in
    private Room previousRoom; //the previous room the player was in
    private Room tempRoom; //a temp holding room, used when we need to interchange the previous and current rooms
    private Room chargedRoom; //the room where the beamer was charged
    private Stack<Room> roomStack; //a stack of the rooms visted, acts as a "history" of the game
    private Stack<Item> inventory; //inventory of items
    private ArrayList<Room> gameRooms;
    private boolean eaten = false; //flag to see if player has eaten
    private int takeCounter = 0;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        inventory = new Stack<Item>(); //the player can only hold 1 item
        parser = new Parser();
        roomStack = new Stack<Room>();
        gameRooms = new ArrayList<Room>(); //arraylist of rooms
        roomStack.push(currentRoom); //push the starting area onto the stack
    }

    /**
     * Create all the rooms and link their exits together.
     * Also create all objects and place them in thier respective rooms
     */
    private void createRooms()
    {

        Room outside, theatre, pub, lab, office, transporter;
        Item ball, projector, beerBottle, microscope, computer, cookie, beamer;
        // create the rooms
        // cteate the items
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporter = new TransporterRoom("A teleporting room");

        gameRooms.add(outside);
        gameRooms.add(theatre);
        gameRooms.add(pub);
        gameRooms.add(lab);
        gameRooms.add(office);
        gameRooms.add(transporter);
        
        //initialise items in rooms
        ball = new Item("ball",1.0);
        cookie = new Item("cookie", 0.5);
        projector = new Item("projector", 15.0);
        beerBottle = new Item("bottle", 2.0);
        microscope = new Item("computer", 6.0);
        computer = new Item("desk", 10.0);
        beamer = new Beamer("beamer",5.0);

        //add items to the rooms
        outside.addItem(ball);
        outside.addItem(beamer);
        outside.addItem(cookie);
        theatre.addItem(projector);
        pub.addItem(beerBottle);
        pub.addItem(cookie);
        lab.addItem(microscope);
        lab.addItem(beamer);
        office.addItem(computer);
        office.addItem(cookie);

        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("North",transporter);
        
        theatre.setExit("west", outside);
        
        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        
        currentRoom = outside;  // start game outside

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
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly fun adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
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
        } else if (commandWord.equals("look")){
            look(command);
        } else if(commandWord.equals("eat")){
            eat(command);
        } else if (commandWord.equals("back")){
            back(command);
        } else if (commandWord.equals("stackBack")){
            stackBack(command);
        } else if (commandWord.equals("take")){
            pickUp(command);
        }else if (commandWord.equals("drop")){
            drop(command);
        }else if (commandWord.equals("charge")){
            charge(command);
        } else if (commandWord.equals("fire")){
            fire(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        for(String word:parser.getCommands()){
            System.out.print(word);
            System.out.print(" ");
        }
        System.out.println();
    }

    /**
     * Get a description of the room
     * 
     * @param command, passed command "look"
     */ 
    private void look(Command command){

        if(command.getSecondWord() == null){
            //comand is only look
            System.out.println(currentRoom.getLongDescription());
            if(!inventory.empty()){
                System.out.println("you are holding a "+ (inventory.peek()).itemName());
            }

        } else {

            System.out.println("I don't understand...");
        }

    }

    /**
     * Go back a room to the previous room
     * 
     * @param command, passed command to be processed.
     */ 
    private void back(Command command){

        if(previousRoom == null ){
            System.out.println("You can't go back");
        } else if (!command.hasSecondWord()) {
            //there is no other word
            tempRoom = currentRoom;
            currentRoom = previousRoom;
            previousRoom = tempRoom;
            roomStack.push(currentRoom);
            System.out.println(currentRoom.getLongDescription());

        }else{
            System.out.println("back what?");
        }

    }

    /**
     * Accesses the stack of rooms visited, and deletes the previous room
     * and sets the current room to the room at the top of the stack.
     * Acts like an "undo" for rooms.
     * 
     * @param command, the command to be processed.
     */
    private void stackBack(Command command){
        //use a stack to stack up rooms visited and then go back
        if(previousRoom == null || roomStack.empty()){
            //if the previous room is null (we're at the start) or
            //the stack is empty,i.e we've ran out
            System.out.println("You can't go back");
        } else if (!command.hasSecondWord()) {
            //there is no other word
            previousRoom = roomStack.pop();
            if(roomStack.empty()){
                //right after the room is popped, check again if the stack is empty
                System.out.println("You can't go back!");
            } else {
                currentRoom = roomStack.peek();
                System.out.println(currentRoom.getLongDescription());
            }

        } 
    }

    /**
     * Player eats some food and is full.
     * 
     * @param command, the command to be processed.
     */
    private void eat(Command command){

        if(!command.hasSecondWord()){
            //comand is only eat
            if(((inventory.peek()).itemName()).equals("cookie")){
                //item held is a cookie
                System.out.println("You have eaten and are now full");
                eaten = true;
                inventory.pop();

            } else {
                System.out.println("You have nothing to eat!"); 
            }

        } else {

            System.out.println("Eat what?");
        }

    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * @param command, command to be processed.
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
            
            if(currentRoom instanceof TransporterRoom){
                //the current room is the transporter room
                ((TransporterRoom)currentRoom).getExit("East");
                ((TransporterRoom)currentRoom).passedRooms(gameRooms);
                
            }
            previousRoom = currentRoom;
            currentRoom = nextRoom;
            roomStack.push(currentRoom);
            System.out.println(currentRoom.getLongDescription());
            if(!inventory.empty()){
                System.out.println("you are holding a "+ (inventory.peek()).itemName());
            }

        }
    }

    private void drop(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Drop what?");
            return;
        } else {
            if(!inventory.empty()){
                System.out.println("You have dropped a " + (inventory.peek()).itemName());
                currentRoom.addItem(inventory.pop());
                
            } else {
                System.out.println("You aren't holding anything");
            }
        }
    }

    private void pickUp(Command command){

        if(command.hasSecondWord()) {
            if(!((currentRoom.getLongDescription()).contains(command.getSecondWord()))){
                System.out.println("That item isn't in this room"); 
            } else {            
                outerloop:
                for(Item i: currentRoom.items()){
                    //look through a room's items                        
                    if((command.getSecondWord()).equals(i.itemName())){
                        //we have the item 
                        //I need to make sure that it is a cookie first, if it isn't then bypass the rest of the code
                        if((i.itemName()).equals("cookie") || eaten && takeCounter <= 5){                                                 
                            if(inventory.empty()){
                                inventory.push(i);
                                currentRoom.removeItem(i);
                                System.out.println("You have a " + i.itemName() + " in your inventory");
                                takeCounter++;
                                break outerloop;
                            } else if(!inventory.empty()){
                                //inventory has something in it
                                currentRoom.addItem(inventory.pop());
                                //add the item into the room
                                inventory.push(i);
                                //put the item of choice into your inventory
                                System.out.println("You have a " + i.itemName() + " in your inventory");
                                currentRoom.removeItem(i);
                                takeCounter++;
                                break outerloop;
                                //remove the item from the room
                            }                            
                        } else {
                            System.out.println("You need to eat");
                            takeCounter = 0;
                            eaten = false;
                            break outerloop;
                        }         
                    }    
                } 
            }
        } else {
            System.out.println("Pick up what?");
        }  
    }

    private void charge(Command command){

        if(command.hasSecondWord()){
            System.out.println("Charge what?");
        } else {

            if(((inventory.peek()).getClass() == Beamer.class)) {

                //item is a beamer
                if(((Beamer)inventory.peek()).isCharged()){
                    System.out.println("You need to fire before charging again");
                } else {
                    //store the charged room as the room we were in
                    chargedRoom = currentRoom;
                    System.out.println("Hello");
                    //since we already know this has to be a beamer, cast the object as a beamer and use that method.
                    ((Beamer)inventory.peek()).charge();
                }

            } else {
                System.out.println("You don't have a beamer in your inventory");
            }

        }
    }

    private void fire(Command command){

        if(command.hasSecondWord()){
            System.out.println("Fire what?");
        } else {

            if(((inventory.peek()).getClass() == Beamer.class)){
                //check if the item in the inventory is a beamer
                if(((Beamer)inventory.peek()).isCharged()){
                    currentRoom = chargedRoom;
                    ((Beamer)inventory.peek()).fired();
                    currentRoom.getLongDescription();
                }else{
                    System.out.println("You need to charge your beamer, scotty");
                }
            } 

        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            while(!roomStack.isEmpty()){
                //while the stack isn't empty, empty all elements
                roomStack.pop();
                //note that this is just to make sure that the stack is empty if the game is closed
                //before stackBack reaches the last room (outside)
            }

            while(!inventory.isEmpty()){
                inventory.pop();
            }
            return true;  // signal that we want to quit
        }
    }

}

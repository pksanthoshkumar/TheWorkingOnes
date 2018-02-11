import java.util.*;
/**
 * Write a description of class TransporterRoom here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TransporterRoom extends Room
{
    // instance variables - replace the example below with your own
    private int x;
    private HashMap<String, Room> exits;
    /**
     * Constructor for objects of class TransporterRoom
     */
    public TransporterRoom(String description)
    {
        super(description);
    }
    
    /**
     * Choose a random room.
     *
     * @return The room we end up in upon leaving this one.
     */
    private Room findRandomRoom()
    {
        //randomly search the arraylist of rooms
        
        return null;
    }
    
    public Room getExit(String direction)
    {
        return findRandomRoom();
    }
}

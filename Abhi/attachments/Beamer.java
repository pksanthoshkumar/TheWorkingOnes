
/**
 * Write a description of class Beamer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Beamer extends Item
{
    // instance variables - replace the example below with your own
    private boolean charged;

    /**
     * Constructor for objects of class Beamer
     */
    public Beamer(String description, double weight)
    {
        super(description,weight);
    }
    
    public void charge(){  

        charged = true;
    }
    
    public void fired(){  

        charged = false;
    }
    
    public boolean isCharged()
    {
        // return true if the beamer is charged
        return charged;
    }
}

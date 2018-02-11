
/**
 * Write a description of class Item here.
 *
 * @author Abhiram
 * @version 21-10-17
 */
public class Item
{
    
    private String description; //describes the item
    private double weight; //Item's weight
    private String name; //name of object

    /**
     * Constructor for items
     * 
     * @param String description, item description, double weight, item weight
     */
    public Item(String description, double weight)
    {
        this.description = description;
        this.weight = weight;
        this.name = this.description;
    }

    /**
     * Get the item's description
     *
     * @return returnString, a string representation of the item.
     */
    public String itemDescription(){
        String returnString = "";
        returnString += this.description + ", it weighs "+ String.valueOf(weight) + " kg";
        return returnString;
    }
    
    public String itemName(){
        return this.name;
    }
    
}


import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReceipientFactory {
    private static ReceipientFactory instance = null;
    
    private ReceipientFactory(){
    }

    public static ReceipientFactory makeFactory(){
        if (instance == null){
            return new ReceipientFactory();
        }
        else{
            return instance;
        }
    }

    //Method to choose which Receipient Class to initiate
    public boolean sendReceipient(String input) throws IndexOutOfBoundsException, DateTimeParseException{
        String[] result = input.split("[:,]");     //Split in to type AND name,email,birthday
        for(int i = 0; i < result.length; i++){
            result[i] = result[i].strip();
        }
        if (result[0].equalsIgnoreCase("official")){
                new Official(result[1], result[2], result[3]);
        }
        
        else if (result[0].equalsIgnoreCase("office_friend")){
            LocalDate.parse(result[4]);
            Receipient officeFriend = new Office_Friends(result[1], result[2], result[3], result[4]);
            BirthdayFactory.getFactory().addBD(officeFriend);
        }

        else if (result[0].equalsIgnoreCase("personal")){
            LocalDate.parse(result[4]);
            Receipient personal = new Personal(result[1], result[2], result[3], result[4]);
            BirthdayFactory.getFactory().addBD(personal);
        }
        else{ 
            System.out.println("Type mismatch, Try Agaian!\n");
            return false;
        }
        return true; //Return whether a Receipient is made or not
    }
}
    
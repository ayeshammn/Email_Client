import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//Class to Handle Birthday Wishes
public class BirthdayFactory {
    final private static Hashtable<String, List<Receipient>> bdList = new Hashtable<>(); 
    static private String subject = "Greetings";
    private static BirthdayFactory instance;
    
    private BirthdayFactory(){

    }
    
    public static BirthdayFactory getFactory(){
        if (instance == null){
            return new BirthdayFactory();
        }
        else{
            return instance;
        }
    }

    //Adding Birthdays of Receipients to a HashTable
    public void addBD(Receipient receipient){
        String key = receipient.getBirthday().substring(5); //Take Month-Day as the key
        boolean found = bdList.containsKey(key); 
        //If key is already in there
        if (found){
            bdList.get(key).add(receipient); //Get the value related to the existing key
        }
        else{
            List<Receipient> tempL = new ArrayList<Receipient>(); //create a temperory List
            synchronized(bdList){
                tempL.add(receipient);
                bdList.put(key, tempL);
            }
        }
    }
    
    //Method to send Birthday Wishes through mail
    static void greetings(Receipient receipient){ 
        (new Email(receipient.getMail(), subject, receipient.wish())).SendEmail();  
    }

    //Method to get List of Receipients which have birthdays on a given date
    public List<Receipient> getReceipients(String date){
        return bdList.get(date);
    }

    //Method to send Birthday Mails at Initialization
    public void sendBirthdays(){
        LocalDate currdate = LocalDate.now();
        
        //Get list of receipients who have their Birthdays today
        List<Receipient> tempL = bdList.get(currdate.toString().substring(5));
        
        
        if (tempL != null){
            synchronized(bdList) {
                for (Receipient r: tempL){
                    greetings(r);
                }
            }
        }
    }
}

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class EmailClient{
    private static ReceipientFactory rFactory;
    private static BirthdayFactory bFactory;
    static File receipientFile;
    static File emailFile;
    private static int totReceipients = 0; //Static Memeber to store no. of receipients
    
    //Initalizing the Email Client
    public static void initialize() throws IOException{
        rFactory = ReceipientFactory.makeFactory();
        bFactory = BirthdayFactory.getFactory();
        
        receipientFile = new File("ClientList.txt");
        receipientFile.createNewFile(); //Create file if it does not exist
        read();

        emailFile = new File("Emails.ser");
        emailFile.createNewFile();  
        Email.deserialize(emailFile);
        new SendBirthdays().start();
    }

    public static void main(String[] args) {
        String option;
        String invalidInput  = "-Invalid Input Format,Try Again-";
        String help = "Enter option type: \n"
                    + "0 - for help\n"
                    + "1 - Adding a new recipient\n"
                    + "2 - Sending an email\n"
                    + "3 - Printing out all the recipients who have birthdays today\n"
                    + "4 - Printing out details of all the emails sent\n"
                    + "5 - Printing out the number of recipient in the application\n"
                    + "-1 - To exit Program";
    
    
    
    try {
        initialize();
    } catch (IOException e1) {
        System.out.println();
    }

    System.out.println(help);
    boolean running = true;
    Scanner scanner = new Scanner(System.in);
    Scanner messageScan = new Scanner(System.in).useDelimiter("/end"); //Special Scanner to read the messeage potion of the Email
    
    try{
        while(running){
            System.out.print("Enter New Command: ");
            
            option = scanner.next(); //Main option to choose

            switch (option) {
                
                //print the help text
                case "0":
                    System.out.println(help);
                    break;
                
                //Add Receipient
                case "1":
                    System.out.println("Enter as Follows without triangular brackets\n" +
                                    "           Official: <Name>,<Email>,<Designation>\n" + 
                                    "           Office_friend: <Name>,<Email>,<Designation>,<YYYY-MM-DD>\n" +
                                    "           Personal: <Name>,<Nickname>,<Email>,<YYYY-MM-DD>");
                    
                    //scan empty string
                    scanner.nextLine();
                    String input = scanner.nextLine().strip();
                    System.out.print("Adding Receipient.....");
                    try{
                        boolean added = rFactory.sendReceipient(input);
                        totReceipients++;
                        
                        //If Receipient Object is added write to the client text file
                        if (added){
                            write(input);
                            System.out.println("Succesfull\n");
                        }
                    } 
                    catch (IndexOutOfBoundsException x){
                        System.out.println(invalidInput + "\n");
                    } 
                    catch (DateTimeParseException y){ 
                        System.out.println("-Date Format Invalid-\n");
                    }
                    break;
            
                case "2":
                    String to, subject, message;

                    scanner.nextLine();
                    System.out.print("To: ");
                    
                    to = (scanner.nextLine()).strip();
                    if(!to.contains("@") || !to.contains(".") || to.contains(" ")){ //For some Inavlid Mail Address
                        System.out.println("-Email Format Invalid-");
                        break;
                    }  

                    System.out.print("Subject: ");
                    subject = scanner.nextLine().strip();

                    System.out.println("Type Message Here (Use '/end' to end the message): ");
                    message = messageScan.next().strip();                
                    System.out.print("Sending Email.....");
                    Email email = new Email(to, subject, message);
                    boolean added = email.SendEmail();//send mail
                    if (added){
                        System.out.println("Successfull");
                    }
                    break;

                //Print names of Birthday Boys and Girls
                case "3":
                    LocalDate currdate = LocalDate.now();
                    String key = currdate.toString().substring(5);
                    List<Receipient> tempL = bFactory.getReceipients(key);
                    
                    if (tempL == null){
                        System.out.println("No Birthdays to show Today");
                    }
                    else{
                        for (Receipient r: tempL){
                            System.out.println(r.getName());
                        }
                    }
                    break;      
                
                //Print all the mails sent on a given date
                case "4":
                    System.out.print("Give the Date as yyyy-mm-dd: ");
                    scanner.nextLine();
                    String temp = scanner.nextLine().strip();
                    
                    try {
                        LocalDate date = LocalDate.parse(temp);
                        Email.printMail(date);
                    } catch (DateTimeParseException e) {
                        System.out.println("-Date Format Invalid-\n");
                    }
                    break;
                
                //Print number of Receipients
                case "5":
                    System.out.println("No of Receipients in the Application: " + totReceipients + "\n"); 
                    break;
                
                //Close the Program
                case "-1":
                    running = !running;
                    messageScan.close();
                    scanner.close();
                    Email.serialize();
                    break;
                
                //Invalid Inputs
                default:
                    System.out.println(invalidInput + "\n");
                    break;
                }
            }
        }catch (Exception e){
            System.out.println("System Closed Due to an Unexpected Error");
            
        }finally{
            try{
                Email.serialize();
                messageScan.close();
                scanner.close();
            } finally{
                try{
                    Email.serialize();
                } catch(Exception e3){
                    System.out.println("File Could Not be Saved Correctly, Something is very Wrong");
                }
            }
        }
    }
    
    //Method to read receipients to the client.txt
    public static void read() {
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(receipientFile));
            String curLine = bufferedReader.readLine();

            //Read line by line
            while (curLine != null){
                rFactory.sendReceipient(curLine);
                totReceipients++;
                curLine = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        catch (Exception y){
            y.printStackTrace();
            System.out.println("ClientList.txt file Error");
        }  
    }
    
    //Method to write receipients to the client.txt
    private static void write(String input) throws IOException{
        FileWriter fileObj = new FileWriter(receipientFile, true);
        BufferedWriter writer = new BufferedWriter(fileObj);
        writer.append(input);
        writer.newLine();
        
        writer.close();
        fileObj.close(); 
        try{
            if (writer != null) {
                writer.close();
            }
            if (fileObj != null) {
                fileObj.close();
            }
        } catch (Exception e){
            System.out.println("-ClientList.txt file Error-\n");
        }
    }        
}   
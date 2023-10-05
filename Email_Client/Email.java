import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class Email implements Serializable {
    private String receipient;
    
    private String msg;
    private String subject;
    private LocalDate date;
    final static ArrayList<Email> emailList = new ArrayList<Email>(); //Array of Emails sent

    public Email(String receipient, String subject, String msg) {
        this.receipient = receipient;
        this.subject = subject;
        this.msg = msg;
        this.date = LocalDate.now(); //Current Date
    }

    //Seding a mail
    public boolean SendEmail(){
        final String from = "akeshuom@gmail.com";
        final String password = "epnnvpqhqhmhkkyz";
        final String host = "smtp.gmail.com";
        final String port = "587";
        
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true"); //Authentication
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.trust", host);

        Session session = Session.getInstance(properties,
        new javax.mail.Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(from, password);
      }
        });

        try {
             MimeMessage message = new MimeMessage(session);    
             message.setFrom(new InternetAddress());
             message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.receipient));
             message.setSubject(this.subject);
             message.setText(this.msg);             //Actual Message
             Transport.send(message);           // Send message
             emailList.add(this);
         } catch (MessagingException mex) {
            mex.printStackTrace();
            System.out.println("-Messaging Error-");
            }
        return true;    
    }
    
    //Method to Print All sent mails on a given date
    public static void printMail(LocalDate dateGiven){
        ArrayList<Email> temp = new ArrayList<Email>();;
        
        //Iterate through th emailList
        for (Email i: emailList){
            if (i.date.equals(dateGiven)){
                temp.add(i);
                //If Email is sent on a given date Email Object is added to a temporary Array
            }
        }

        //If no mails were sent
        if(temp.isEmpty()){
            System.out.println("No Emails has been sent on " + dateGiven.toString() + "\n");
            return;
        }

        //Iterate through the temp array
        int j = 1;
        System.out.println(temp.size() + " Emails Have been sent on " + dateGiven.toString());
        for (Email i: temp){
            System.out.println();
            System.out.println("Email 0" + j);
            System.out.println("Email Address: " + i.receipient + "\n" 
                             + "Subject      : " + i.subject + "\n"
                             + "-End-");
            j++;
            System.out.println();
        }
    }

    //Serializing the Array of Emails Object 
    public static void serialize(){
        FileOutputStream fileStreamout = null;
        ObjectOutputStream os = null;
        try {
            fileStreamout = new FileOutputStream("Emails.ser");
            os = new ObjectOutputStream(fileStreamout);
            os.writeObject(emailList);
            os.close();
            fileStreamout.close();

        } catch (IOException e) {
            System.out.println("-File Error-");
        } finally{
            try {
                if (os != null) {
                    os.close();
                } 
                if(fileStreamout != null) {
                    fileStreamout.close();
                }
            } catch (Exception ignore) {
                System.out.println("-File Error in Emails.ser-/n");
            }
        }     
    }
    
    //Deserializing the Array of Emails Object 
    public static void deserialize(File emailFile) {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;

        try {
            fileIn = new FileInputStream(emailFile);
            in = new ObjectInputStream(fileIn);
            
            @SuppressWarnings("unchecked")
            ArrayList<Email> temp = (ArrayList<Email>)in.readObject();
            
            //Iterate
            for (Email i: temp){
                emailList.add(i);
            }            
        } catch (EOFException a){
            System.out.println("Hello Welcome to the Email Client");
        } catch (IOException | ClassNotFoundException b) {
            b.printStackTrace();
            System.out.println("-File Error in deserializing-\n");
        } finally{
            try{
                if (in != null){
                    in.close();
                }
                if (fileIn != null){                
                    fileIn.close();;
                }
                //System.out.println("Program Loaded Succesfully");
            } catch (Exception ignore){
                System.out.println("-File Error in Emails.ser-/n");
            }   
        }    
    }
}
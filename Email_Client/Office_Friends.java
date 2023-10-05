//Class for Office Friend Receipient
//Hava a name, email address, designation and a birthday
public class Office_Friends extends Receipient {
    String wish = "Wish you a Happy Birthday. \n";

    public Office_Friends(String name, String email, String designation, String birthday){
        super(name, email);
        this.designation = designation;
        this.birthday = birthday;
    }
    
    public String wish(){
        return wish + myName;
    }
}

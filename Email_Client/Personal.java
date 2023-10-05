//Class for Personal Receipient
//Have a name, nickname, emai and a birthday

public class Personal extends Receipient {
    String wish = "Hugs and love on your birthday. \n";

    public Personal(String name, String nickName, String email, String birthday) {
        super(name, email);
        this.nickName = nickName;
        this.birthday = birthday;    
    }
    public String wish(){
        return wish + myName;
    }
}

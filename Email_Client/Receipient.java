//Parent Class of Official, Office_Friends, Personal Classes
abstract class Receipient  {
    protected String myName = "Akesh"; //Senders Name
    protected String name;
    protected String nickName;
    protected String email;
    protected String birthday;
    protected String designation;
    protected String wish;
    
    public Receipient(String name, String email){
        this.name = name;
        this.email = email;
    }

    public abstract String wish();

    public String getName() {
        return this.name;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getMail() {
        return this.email;
    }
    
    public void setMyName(String name) {
        this.myName = name;
    }
}
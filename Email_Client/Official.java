//Class for Offcial Receipient
//Have a name, emai and a designation
public class Official extends Receipient {
    
    public Official(String name, String email, String designation){
        super(name, email);
        this.designation = designation;
    }

    public String wish() {
        return null;
    }
    
}

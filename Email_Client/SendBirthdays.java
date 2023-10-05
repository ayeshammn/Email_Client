public class SendBirthdays extends Thread {
        
    //Extra thread to avoid startup delay
        public void run()
        {
            try {
                BirthdayFactory.getFactory().sendBirthdays();
            }catch (Exception x) {
                System.out.println("-Error When sending Birthday-");
            }
        }
    }

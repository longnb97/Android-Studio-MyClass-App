package longnb.xda.edu.btlandroid;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

public  class UserDetails extends Application {
    private String ava;
    private static String email;
    private String displayName;
    private String classname;
    private static String mainSubject;
    private static String dob;
    private static UserDetails singleInstance = null;



    public  void empty(){
        this.ava = this.email = this.displayName = this.classname = this.mainSubject = this.dob ="";
    }
    public void set(String ava, String email, String displayName, String className,String mainSubject, String dob){
        this.ava = ava;
        this.email = email;
        this.displayName = displayName;
        this.classname = className;
        this.mainSubject = mainSubject;
        this.dob = dob;
    }

    public String getAva() {
        return ava;
    }

    public void setAva(String ava) {
        this.ava = ava;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMainSubject() {
        return mainSubject;
    }

    public void setMainSubject(String mainSubject) {
        this.mainSubject = mainSubject;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }
    public static UserDetails getInstance(){
        return singleInstance;
    }
}

package longnb.xda.edu.btlandroid;

import com.android.volley.toolbox.StringRequest;

public class StudentJSON {

    private String conduct;
    private String fullname;
    private String id;
    private String dateOfBirth;
    private String classname;
    private String address;
    private String phoneNumber;
    private String email;
    private String parentName;
    private String photo;
    private String gender;
    private String averageScore;


//    public StudentJSON(String gender, String classname, String fullname, String photo, String averageScore){
    public StudentJSON( String conduct, String fullname, String id, String dateOfBirth, String classname, String address, String phoneNumber, String email, String parentName, String photo, String gender, String averageScore) {

        this.conduct = conduct;
        this.fullname = fullname;
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.classname = classname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.parentName = parentName;
        this.photo = photo;
        this.averageScore = averageScore;
        this.gender = gender;
        this.address = address;
    }

    public void onStudentSelected(){}
    public String getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(String averageScore) {
        this.averageScore = averageScore;
    }

    public String getConduct() {
        return conduct;
    }

    public void setConduct(String conduct) {
        this.conduct = conduct;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

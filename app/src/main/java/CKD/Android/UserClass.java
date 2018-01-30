package CKD.Android;



public class UserClass
{
    private String name;
    private String email;
    private String phone;
    private String age;
    private String activityLevel;
    private String UID;

    public UserClass(String name, String email, String phone, String age, String activityLevel, String UID )
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.activityLevel =  activityLevel;
        this.UID = UID;
    }


    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone(){
        return phone;
    }

    public String getAge(){return age;}

    public String getActivityLevel(){return activityLevel;}

    public String getUID(){return UID;}

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public void setActivityLevel(String activityLevel)
    {
        this.activityLevel = activityLevel;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }

}

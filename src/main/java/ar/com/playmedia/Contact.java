package ar.com.playmedia;

public class Contact{
    private Integer intId;
    public Integer getIntId(){return this.intId;}
    public void setIntId(Integer intId){this.intId = intId;}

    private String strName;
    public String getStrName(){return this.strName;}
    public void setStrName(String strName){this.strName = strName;}

    private String strPhone;
    public String getStrPhone(){return this.strPhone;}
    public void setStrPhone(String strPhone){this.strPhone = strPhone;}

    private String strMail;
    public String getStrMail(){return this.strMail;}
    public void setStrMail(String strMail){this.strMail = strMail;}


    public Contact(){
        this.intId = 0;
        this.strName = "";
        this.strPhone = "";
        this.strMail = "";
    }
    public Contact (Integer intId, String strName, String strPhone, String strMail){
        this.intId = intId;
        this.strName = strName;
        this.strPhone = strPhone;
        this.strMail = strMail;
    }
    public String toString(){
        String strString = this.intId.toString() + "\t\t\t" + this.strName + "\t\t\t" + this.strPhone + "\t\t\t" + this.strMail;
        return strString;
    }
}
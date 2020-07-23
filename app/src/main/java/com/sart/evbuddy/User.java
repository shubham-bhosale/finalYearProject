package com.sart.evbuddy;

public class User
{
    String mail;
    String phone;
    String type;

    User(String mail, String phone, String type)
    {
        this.mail = mail;
        this.phone = phone;
        this.type = type;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

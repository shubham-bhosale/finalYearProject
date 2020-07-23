package com.sart.evbuddy;

import java.io.Serializable;

public class Owner implements Serializable
{
    String mail;
    String phone;
    String password,name,totalAvl,totalBsy,totalSlots,longitude,latitude,CSName;
    String Avl_AC_Type1,Avl_AC_Type2,Avl_AC_Type3,Avl_DC_Type1,Avl_DC_Type2,Avl_DC_Type3;
    String Bsy_AC_Type1,Bsy_AC_Type2,Bsy_AC_Type3,Bsy_DC_Type1,Bsy_DC_Type2,Bsy_DC_Type3;
    String Total_AC_Type1,Total_AC_Type2,Total_AC_Type3,Total_DC_Type1,Total_DC_Type2,Total_DC_Type3;

    /*newly added*/
    public Owner()
    {

    }

    public Owner(String mail, String phone, String password, String name, String totalAvl, String totalBsy, String totalSlots, String longitude, String latitude, String CSName, String avl_AC_Type1, String avl_AC_Type2, String avl_AC_Type3, String avl_DC_Type1, String avl_DC_Type2, String avl_DC_Type3, String bsy_AC_Type1, String bsy_AC_Type2, String bsy_AC_Type3, String bsy_DC_Type1, String bsy_DC_Type2, String bsy_DC_Type3, String total_AC_Type1, String total_AC_Type2, String total_AC_Type3, String total_DC_Type1, String total_DC_Type2, String total_DC_Type3)
    {
        this.mail = mail;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.totalAvl = totalAvl;
        this.totalBsy = totalBsy;
        this.totalSlots = totalSlots;
        this.longitude = longitude;
        this.latitude = latitude;
        this.CSName = CSName;
        Avl_AC_Type1 = avl_AC_Type1;
        Avl_AC_Type2 = avl_AC_Type2;
        Avl_AC_Type3 = avl_AC_Type3;
        Avl_DC_Type1 = avl_DC_Type1;
        Avl_DC_Type2 = avl_DC_Type2;
        Avl_DC_Type3 = avl_DC_Type3;
        Bsy_AC_Type1 = bsy_AC_Type1;
        Bsy_AC_Type2 = bsy_AC_Type2;
        Bsy_AC_Type3 = bsy_AC_Type3;
        Bsy_DC_Type1 = bsy_DC_Type1;
        Bsy_DC_Type2 = bsy_DC_Type2;
        Bsy_DC_Type3 = bsy_DC_Type3;
        Total_AC_Type1 = total_AC_Type1;
        Total_AC_Type2 = total_AC_Type2;
        Total_AC_Type3 = total_AC_Type3;
        Total_DC_Type1 = total_DC_Type1;
        Total_DC_Type2 = total_DC_Type2;
        Total_DC_Type3 = total_DC_Type3;
    }

    /**************/

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalAvl() {
        return totalAvl;
    }

    public void setTotalAvl(String totalAvl) {
        this.totalAvl = totalAvl;
    }

    public String getTotalBsy() {
        return totalBsy;
    }

    public void setTotalBsy(String totalBsy) {
        this.totalBsy = totalBsy;
    }

    public String getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(String totalSlots) {
        this.totalSlots = totalSlots;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCSName() {
        return CSName;
    }

    public void setCSName(String CSName) {
        this.CSName = CSName;
    }

    public String getAvl_AC_Type1() {
        return Avl_AC_Type1;
    }

    public void setAvl_AC_Type1(String avl_AC_Type1) {
        Avl_AC_Type1 = avl_AC_Type1;
    }

    public String getAvl_AC_Type2() {
        return Avl_AC_Type2;
    }

    public void setAvl_AC_Type2(String avl_AC_Type2) {
        Avl_AC_Type2 = avl_AC_Type2;
    }

    public String getAvl_AC_Type3() {
        return Avl_AC_Type3;
    }

    public void setAvl_AC_Type3(String avl_AC_Type3) {
        Avl_AC_Type3 = avl_AC_Type3;
    }

    public String getAvl_DC_Type1() {
        return Avl_DC_Type1;
    }

    public void setAvl_DC_Type1(String avl_DC_Type1) {
        Avl_DC_Type1 = avl_DC_Type1;
    }

    public String getAvl_DC_Type2() {
        return Avl_DC_Type2;
    }

    public void setAvl_DC_Type2(String avl_DC_Type2) {
        Avl_DC_Type2 = avl_DC_Type2;
    }

    public String getAvl_DC_Type3() {
        return Avl_DC_Type3;
    }

    public void setAvl_DC_Type3(String avl_DC_Type3) {
        Avl_DC_Type3 = avl_DC_Type3;
    }

    public String getBsy_AC_Type1() {
        return Bsy_AC_Type1;
    }

    public void setBsy_AC_Type1(String bsy_AC_Type1) {
        Bsy_AC_Type1 = bsy_AC_Type1;
    }

    public String getBsy_AC_Type2() {
        return Bsy_AC_Type2;
    }

    public void setBsy_AC_Type2(String bsy_AC_Type2) {
        Bsy_AC_Type2 = bsy_AC_Type2;
    }

    public String getBsy_AC_Type3() {
        return Bsy_AC_Type3;
    }

    public void setBsy_AC_Type3(String bsy_AC_Type3) {
        Bsy_AC_Type3 = bsy_AC_Type3;
    }

    public String getBsy_DC_Type1() {
        return Bsy_DC_Type1;
    }

    public void setBsy_DC_Type1(String bsy_DC_Type1) {
        Bsy_DC_Type1 = bsy_DC_Type1;
    }

    public String getBsy_DC_Type2() {
        return Bsy_DC_Type2;
    }

    public void setBsy_DC_Type2(String bsy_DC_Type2) {
        Bsy_DC_Type2 = bsy_DC_Type2;
    }

    public String getBsy_DC_Type3() {
        return Bsy_DC_Type3;
    }

    public void setBsy_DC_Type3(String bsy_DC_Type3) {
        Bsy_DC_Type3 = bsy_DC_Type3;
    }

    public String getTotal_AC_Type1() {
        return Total_AC_Type1;
    }

    public void setTotal_AC_Type1(String total_AC_Type1) {
        Total_AC_Type1 = total_AC_Type1;
    }

    public String getTotal_AC_Type2() {
        return Total_AC_Type2;
    }

    public void setTotal_AC_Type2(String total_AC_Type2) {
        Total_AC_Type2 = total_AC_Type2;
    }

    public String getTotal_AC_Type3() {
        return Total_AC_Type3;
    }

    public void setTotal_AC_Type3(String total_AC_Type3) {
        Total_AC_Type3 = total_AC_Type3;
    }

    public String getTotal_DC_Type1() {
        return Total_DC_Type1;
    }

    public void setTotal_DC_Type1(String total_DC_Type1) {
        Total_DC_Type1 = total_DC_Type1;
    }

    public String getTotal_DC_Type2() {
        return Total_DC_Type2;
    }

    public void setTotal_DC_Type2(String total_DC_Type2) {
        Total_DC_Type2 = total_DC_Type2;
    }

    public String getTotal_DC_Type3() {
        return Total_DC_Type3;
    }

    public void setTotal_DC_Type3(String total_DC_Type3) {
        Total_DC_Type3 = total_DC_Type3;
    }
}

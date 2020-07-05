package com.campus.management;

import java.io.Serializable;

public class Student implements Serializable {

    private String SClass, regNo, Name, FName, Section, Dob, Gender, PlaceDob, Religion,
    Address, Phone, MName, FCnin, Designation, fOccupation,  MCnic, AddOW, Nationality,Flang;

    public Student(String SClass, String regNo, String name, String FName, String section, String dob, String gender, String placeDob, String religion, String address, String phone, String MName, String FCnin, String designation, String fOccupation, String MCnic, String addOW, String nationality, String flang) {
        this.SClass = SClass;
        this.regNo=regNo;
        Name = name;
        this.FName = FName;
        Section = section;
        Dob = dob;
        Gender = gender;
        PlaceDob = placeDob;
        Religion = religion;
        Address = address;
        Phone = phone;
        this.MName = MName;
        this.FCnin = FCnin;
        Designation = designation;
        this.fOccupation = fOccupation;
        this.MCnic = MCnic;
        AddOW = addOW;
        Nationality = nationality;
        Flang = flang;
    }

    public Student() {
    }

    public String getSClass() {
        return SClass;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public void setSClass(String SClass) {
        this.SClass = SClass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getSection() {
        return Section;
    }

    public void setSection(String section) {
        Section = section;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPlaceDob() {
        return PlaceDob;
    }

    public void setPlaceDob(String placeDob) {
        PlaceDob = placeDob;
    }

    public String getReligion() {
        return Religion;
    }

    public void setReligion(String religion) {
        Religion = religion;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMName() {
        return MName;
    }

    public void setMName(String MName) {
        this.MName = MName;
    }

    public String getFCnin() {
        return FCnin;
    }

    public void setFCnin(String FCnin) {
        this.FCnin = FCnin;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getfOccupation() {
        return fOccupation;
    }

    public void setfOccupation(String fOccupation) {
        this.fOccupation = fOccupation;
    }

    public String getMCnic() {
        return MCnic;
    }

    public void setMCnic(String MCnic) {
        this.MCnic = MCnic;
    }

    public String getAddOW() {
        return AddOW;
    }

    public void setAddOW(String addOW) {
        AddOW = addOW;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getFlang() {
        return Flang;
    }

    public void setFlang(String flang) {
        Flang = flang;
    }
}

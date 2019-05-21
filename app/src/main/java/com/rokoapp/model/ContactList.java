package com.rokoapp.model;

public class ContactList {
    private String contactName;
    private String contactNumber;

    public ContactList(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactName() { return contactName; }

    public String getContactNumber() { return contactNumber; }
}

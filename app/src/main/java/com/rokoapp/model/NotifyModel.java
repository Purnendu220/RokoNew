package com.rokoapp.model;

public class NotifyModel {
    private int id;
    private String notifyTitle;
    private String notifyDesc;
    private String notifyImg;

    public NotifyModel(int id, String notifyTitle, String notifyDesc, String notifyImg) {
        this.id = id;
        this.notifyTitle = notifyTitle;
        this.notifyDesc = notifyDesc;
        this.notifyImg = notifyImg;
    }

    public int getId() { return id; }

    public String getNotifyTitle() { return notifyTitle; }

    public String getNotifyDesc() { return notifyDesc; }

    public String getNotifyImg() { return notifyImg; }
}

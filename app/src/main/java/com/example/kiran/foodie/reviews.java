package com.example.kiran.foodie;

public class reviews {
    public reviews() {
    }
    private String feedback,type;
    private long time;
    private String from;
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }



    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    public reviews(String feedback, String type, long time, String from,String to) {
        this.feedback = feedback;
        this.type = type;
        this.time = time;
        this.from = from;
        this.to=to;
    }
}

package sample;

import java.util.GregorianCalendar;

public class Request {
    private String FIO;
    private String address;
    private String contacts;
    private String executor;
    private String condition;
    private String description;
    private String date;

    public Request(String FIO, String address, String contacts, String executor, String condition, String description, String date) {
        this.FIO = FIO;
        this.address = address;
        this.contacts = contacts;
        this.executor = executor;
        this.condition = condition;
        this.description = description;
        this.date = date;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

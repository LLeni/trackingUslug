package sample;

import javafx.scene.control.CheckBox;

import java.util.GregorianCalendar;

public class Usluga {
    private String FIO;
    private String address;
    private String contacts;
    private String executor;
    private String name;
    private String description;
    private String dateBegin;
    private String dateEnd;
    private CheckBox checkBox;

    public Usluga(String FIO, String address, String contacts, String executor, String name, String description, String dateBegin, String dateEnd, String condition) {
        this.FIO = FIO;
        this.address = address;
        this.contacts = contacts;
        this.executor = executor;
        this.name = name;
        this.description = description;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;

        CheckBox checkBox = new CheckBox();
        if(condition.equals("Выполнено")){
            checkBox.setSelected(true);
        } else {
            checkBox.setSelected(false);
        }

        this.checkBox = checkBox;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}

package sample;

public class CountPerDate {
    //Необходимо для отображения данных в таблице
    private int count;
    private String date;

    public CountPerDate(String date, int count) {
        this.count = count;
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

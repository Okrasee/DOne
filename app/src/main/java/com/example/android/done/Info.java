package com.example.android.done;

public class Info {
    private String title;
    private String category;
    private String description;
    private int bgcolor;
    private int fabcolor;
    private int catColor;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean mode;
    private int groupPos;
    private int childPos;

    public Info(String newTitle, int newBgColor, int fabColor, boolean newMode) {
        this.title = newTitle;
        this.bgcolor = newBgColor;
        this.fabcolor = fabColor;
        this.mode = newMode;
    }

    //mixedTaskInfoList
    public Info(String newTitle, int newBgColor, String newDescription, int newMonth, int newDay, int newHour, int newMinute, boolean newMode) {
        this.title = newTitle;
        this.bgcolor = newBgColor;
        this.month = newMonth;
        this.day = newDay;
        this.hour = newHour;
        this.minute = newMinute;
        this.description = newDescription;
        this.mode = newMode;
    }

    @Override
    public boolean equals(Object obj) {
        Info other = (Info) obj;
        return (this.title.equals(other.getTitle()) &
                this.bgcolor == other.getBgColor() &
                this.month == other.getMonth() & this.day == other.getDay() &
                this.hour == other.getHour() & this.minute == other.getMinute() &
                this.description == other.getDescription() & this.mode == other.getMode());
    }

    public Info(String newTitle, String itsCategory, int newBgColor, int newCatColor, String newDescription, int newMonth, int newDay, int newHour, int newMinute, boolean newMode) {
        this.title = newTitle;
        this.category = itsCategory;
        this.bgcolor = newBgColor;
        this.month = newMonth;
        this.day = newDay;
        this.hour = newHour;
        this.minute = newMinute;
        this.description = newDescription;
        this.mode = newMode;
        this.catColor = newCatColor;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() { return category; }

    public String getDescription() { return description; }

    public int getBgColor() { return bgcolor; }

    public int getFabcolor() { return fabcolor; }

    public int getCatColor() { return catColor; }

    public int getMonth() { return month; }

    public int getDay() {return day; }

    public int getHour() {return hour; }

    public int getMinute() {return minute; }

    public boolean getMode() {return mode; }

    public int getGroupPos() {return groupPos;}

    public  int getChildPos() {return childPos;}
}

package by.bsu.dekrat.enrolleeservice.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by USER on 01.12.2016.
 */

public class Test {

    private int id;
    private Date date;
    private String type;
    private Room room;
    private Subject subject;

    public Test() {
    }

    public Test(int id, String date, String type) {
        this.id = id;
        DateFormatter formatter = new DateFormatter();
        formatter.setIso(DateTimeFormat.ISO.DATE_TIME);
        try {
            this.date = formatter.parse(date, Locale.ROOT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.type = type;
    }

    public Test(int id, String date, String type, Room room, Subject subject) {
        this.id = id;
        DateFormatter formatter = new DateFormatter();
        formatter.setIso(DateTimeFormat.ISO.DATE_TIME);
        try {
            this.date = formatter.parse(date, Locale.ROOT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.type = type;
        this.room = room;
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return id == test.id &&
                Objects.equals(date, test.date) &&
                Objects.equals(type, test.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, type);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}

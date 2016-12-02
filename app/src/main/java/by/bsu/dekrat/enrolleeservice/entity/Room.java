package by.bsu.dekrat.enrolleeservice.entity;

/**
 * Created by USER on 02.12.2016.
 */

public class Room {

    private int id;
    private int capacity;
    private String number;
    private University university;

    public Room() {
    }

    public Room(int id, int capacity, String number, University university) {
        this.id = id;
        this.capacity = capacity;
        this.number = number;
        this.university = university;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}

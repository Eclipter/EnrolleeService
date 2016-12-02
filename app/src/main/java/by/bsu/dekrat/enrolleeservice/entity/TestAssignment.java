package by.bsu.dekrat.enrolleeservice.entity;

/**
 * Created by USER on 21.11.2016.
 */

public class TestAssignment {

    private int id;
    private int points;
    private Test test;

    public TestAssignment() {
    }

    public TestAssignment(int id, int points, Test test) {
        this.id = id;
        this.points = points;
        this.test = test;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}

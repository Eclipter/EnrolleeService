package by.bsu.dekrat.enrolleeservice.entity;

import java.util.Date;
import java.util.Objects;

/**
 * Created by USER on 21.11.2016.
 */

public class TestAssignment {

    private TestType type;
    private Date testDate;
    private String subject;
    private Integer points;
    private String university;

    public TestAssignment(TestType type, Date testDate, String subject, Integer points, String university) {
        this.type = type;
        this.testDate = testDate;
        this.subject = subject;
        this.points = points;
        this.university = university;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestAssignment that = (TestAssignment) o;
        return type == that.type &&
                Objects.equals(testDate, that.testDate) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(points, that.points) &&
                Objects.equals(university, that.university);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, testDate, subject, points, university);
    }

    @Override
    public String toString() {
        return "TestAssignment{" +
                "type=" + type +
                ", testDate=" + testDate +
                ", subject='" + subject + '\'' +
                ", points=" + points +
                ", university='" + university + '\'' +
                '}';
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}

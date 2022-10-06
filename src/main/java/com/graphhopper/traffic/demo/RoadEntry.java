package com.graphhopper.traffic.demo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

/**
 *
 * @author Peter Karich
 */
public class RoadEntry {
    public enum DAYOFWEEK {
        ITEM,
        ITEM2,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY,
    }
    private List<RoadPoint> points;
    private double value;
    private String valueType;
    private String mode;
    private String id;
    private String time;
    private List<DAYOFWEEK> dayOfWeeks;
    public RoadEntry() {
    }

    public RoadEntry(String id, List<RoadPoint> points, double value, String valueType, String mode, String time, List<DAYOFWEEK> dayOfWeeks) {
        this.points = points;
        this.value = value;
        this.valueType = valueType;
        this.mode = mode;
        this.id = id;
        this.time = time;
        this.dayOfWeeks = dayOfWeeks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RoadPoint> getPoints() {
        return points;
    }

    public void setPoints(List<RoadPoint> points) {
        this.points = points;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setValueType(String type) {
        this.valueType = type;
    }

    /**
     * E.g. speed or any
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * Currently 'replace', 'multiply' and 'add' are supported
     */
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<DAYOFWEEK> getDayOfWeeks() {
        return dayOfWeeks;
    }

    public void setDayOfWeeks(List<DAYOFWEEK> dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }

    @Override
    public String toString() {
        return "points:" + points + ", value:" + value + ", type:" + valueType + ", mode:" + mode + ", time: " + time;
    }
}

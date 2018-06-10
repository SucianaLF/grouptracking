package com.example.sucianalf.grouptracking.Model;

public class DijkstraObject {
    private double startLat;
    private double startLng;
    private int start;
    private int end;
    private double endLat;
    private double endLng;
    private double distance;
    private int route;
    private int step;
    private int startStep;
    private int endStep;
    private int index;
    private int endIndex;

    public DijkstraObject(int index, double startLat, double startLng, double endLat, double endLng, int endIndex, double distance)
    {
        this.startLat = startLat;
        this.startLng = startLng;
        this.index = index;
        this.endLat = endLat;
        this.endLng = endLng;
        this.endIndex = endIndex;
        this.distance = distance;
    }

    public DijkstraObject(int route, int step, int startStep, int endStep)
    {
        this.startLat = startLat;
        this.startLng = startLng;
        this.start = start;
        this.endLat = endLat;
        this.endLng = endLng;
        this.end = end;
        this.distance = distance;
    }

    public DijkstraObject()
    {

    }

    public double getstartLat() {
        return startLat;
    }

    public void setstartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getendLat() {
        return endLat;
    }

    public void setendLat(double endLat) {
        this.endLat = endLat;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getStartStep() {
        return startStep;
    }

    public void setStartStep(int startStep) {
        this.startStep = startStep;
    }

    public int getEndStep() {
        return endStep;
    }

    public void setEndStep(int endStep) {
        this.endStep = endStep;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}

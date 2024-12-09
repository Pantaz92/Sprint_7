package ru.qascooter.dto.order;

public class CancelOrder {
    private String track;

    public CancelOrder (String track) {
        this.track = track;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}

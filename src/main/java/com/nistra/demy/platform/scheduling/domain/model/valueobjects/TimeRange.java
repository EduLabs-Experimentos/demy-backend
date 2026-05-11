package com.nistra.demy.platform.scheduling.domain.model.valueobjects;

import java.time.LocalTime;

public record TimeRange(LocalTime startTime, LocalTime endTime) {

    /**
     public TimeRange() {
     this(null, null);
     }
     */
    public TimeRange {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time must not be null");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time must not be null");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }


    public TimeRange(String startTime, String endTime) {
        this(LocalTime.parse(startTime), LocalTime.parse(endTime)); // parse the string times to LocalTime
    }

    public boolean overlapsWith(TimeRange other) {
        if (other == null) return false;
        return startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime);
    }
}

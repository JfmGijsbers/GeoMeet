package com.group02tue.geomeet.backend;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import java.text.ParseException;


public class Location2D {
    public final float longitude;
    public final float latitude;
    public final String detailedLocation;       // May NOT contain @ !!

    public Location2D(final float longitude, final float latitude, final String detailedLocation) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.detailedLocation = detailedLocation;
    }

    /**
     * Parses a string which represents a location.
     * @param input String representing a location (in long@lat format)
     * @return Location represented by the string
     * @throws ParseException Failed to parse
     */
    public static Location2D parse(String input) throws ParseException {
        String[] split = input.split("@");
        if (split.length == 3) {
            float lon = Float.parseFloat(split[0]);
            float lat = Float.parseFloat(split[1]);
            String detailedLocation = split[2];
            return new Location2D(lon, lat, detailedLocation);
        } else if (split.length == 2) {     // For compatibility
            float lon = Float.parseFloat(split[0]);
            float lat = Float.parseFloat(split[1]);
            return new Location2D(lon, lat, "Not available");
        }
        throw new ParseException("Invalid location string", 0);
    }

    /**
     * Gets the string representation of the location in long@lat format.
     * @return String representation
     */
    @Override
    @NonNull
    public String toString() {
        return Float.toString(this.longitude) + "@" + Float.toString(this.latitude) + "@" + detailedLocation;
    }
}

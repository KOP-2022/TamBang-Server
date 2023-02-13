package com.example.tambang.service;

import com.example.tambang.domain.Facility;

public interface FacilityService {
    void enroll(Facility facility);
    double getDistance(double aLat, double aLon, double bLat, double bLon);

}

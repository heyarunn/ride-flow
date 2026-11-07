package com.rideflow.ride_service.matching;

import com.rideflow.ride_service.domain.dto.NearbyDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class DriverScoringService {
    private static final double MAX_IDLE_TIME = 30.0 // 30 mins idle time

    private final double distanceWeightage;
    private final double ratingWeightage;
    private final double waitingWeightage;


    public DriverScoringService(@Value("${rideflow.matching.weights.distance:0.5}") double distanceWeightage,
                                @Value("${rideflow.matching.weights.rating:0.3}") double ratingWeightage,
                                @Value("${rideflow.matching.weights.rating:0.2}") double waitingWeightage) {
        this.distanceWeightage = distanceWeightage;
        this.ratingWeightage = ratingWeightage;
        this.waitingWeightage = waitingWeightage;
    }

    public double scoreDriver(NearbyDriver currDriver, double radiusKm, LocalDateTime now) {

        double distanceScore = radiusKm <= 0 ? 1.0 : 1.0 - normaliseScore(currDriver.distKm()/radiusKm);
        double ratingScore = currDriver.rating() == null ? 0.0 : normaliseScore((currDriver.rating()-1.0)/4.0);
        double waitingScore = 0.0;
        if (currDriver.availableSince()!=null) {
            double idleTime = Duration.between(currDriver.availableSince(), now).toSeconds()/60.0;
            waitingScore = normaliseScore(idleTime/MAX_IDLE_TIME);
        }

        return (distanceScore * distanceWeightage + ratingScore * ratingWeightage + waitingScore * waitingWeightage);
    }

    public static double normaliseScore(double score) {
        return Math.max(0.0, Math.min(1.0, score));
    }
}

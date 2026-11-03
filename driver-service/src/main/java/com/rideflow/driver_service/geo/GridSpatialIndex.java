package com.rideflow.driver_service.geo;

import java.util.*;

public class GridSpatialIndex {

    private record Entry(UUID driverId, double latitude, double longitude) {}

    private record CellKey(int gridX, int gridY) {}

    private final double cellSizeDegrees;
    private final Map<CellKey, List<Entry>> cells = new HashMap<>();

    public GridSpatialIndex(double cellSizeDegrees) {
        if (cellSizeDegrees <= 0) {
            throw new IllegalArgumentException("Cell size must be positive");
        }
        this.cellSizeDegrees = cellSizeDegrees;
    }

    private CellKey cellOf(double latitude, double longitude) {
        int gx = (int) Math.floor(longitude / cellSizeDegrees);
        int gy = (int) Math.floor(latitude / cellSizeDegrees);
        return new CellKey(gx, gy);
    }

    // Add entries to the list
    public void add(UUID driverId, double latitude, double longitude) {
        CellKey cellKey = cellOf(latitude, longitude);

        cells.computeIfAbsent(cellKey, k -> new ArrayList<>())
                .add(new Entry(driverId, latitude, longitude));
    }

    // Clear all entries
    public void clear() {
        cells.clear();
    }

    public int size() {
        return cells.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    public List<Result> queryNearBy(
            double longitude,
            double latitude,
            double radiusKm,
            int limit) {

        if (radiusKm < 0 || limit <= 0) {
            return Collections.emptyList();
        }

        CellKey center = cellOf(latitude, longitude);

        int cellSpan = (int) Math.ceil(
                (radiusKm / 111.0) / cellSizeDegrees
        );

        List<Result> results = new ArrayList<>();

        for (int dx = -cellSpan; dx <= cellSpan; dx++) {
            for (int dy = -cellSpan; dy <= cellSpan; dy++) {

                CellKey cellKey = new CellKey(
                        center.gridX() + dx,
                        center.gridY() + dy
                );

                List<Entry> bucket = cells.get(cellKey);

                if (bucket == null) {
                    continue;
                }

                for (Entry entry : bucket) {
                    double distKm = GeoDistance.haversineKm(
                            latitude,
                            longitude,
                            entry.latitude(),
                            entry.longitude()
                    );

                    if (distKm <= radiusKm) {
                        results.add(new Result(
                                entry.driverId(),
                                entry.latitude(),
                                entry.longitude(),
                                distKm
                        ));
                    }
                }
            }
        }

        return results.stream()
                .sorted(Comparator.comparingDouble(Result::distanceKm))
                .limit(limit)
                .toList();
    }

    public record Result(
            UUID driverId,
            double latitude,
            double longitude,
            double distanceKm
    ) {}
}
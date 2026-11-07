package com.rideflow.driver_service.geo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GridSpatialIndex {

    private record Entry(UUID driverId, double latitude, double longitude) {}

    private record CellKey(int gridX, int gridY) {}

    private final double cellSizeDegrees;

    // current cell and the drivers in that cell
    private final Map<CellKey, List<Entry>> cells = new ConcurrentHashMap<>();

    // driver id and the current cell key
    private final Map<UUID, CellKey> driverCells = new ConcurrentHashMap<>();

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

    // insert or move a driver to a new location
    public void upsert(UUID driverId, double latitude, double longitude) {
        CellKey newKey = cellOf(latitude, longitude);
        CellKey oldKey = driverCells.get(driverId);

        if (oldKey!=null && !oldKey.equals(newKey)) {
            removeFromCell(oldKey, driverId);
        }

        List<Entry> bucket = cells.computeIfAbsent(newKey, k -> new CopyOnWriteArrayList<>());
        bucket.removeIf(entry -> entry.driverId().equals(driverId));
        bucket.add(new Entry(driverId, latitude, longitude));

        driverCells.put(driverId, newKey);
    }

    // remove a driver from an index
    public void remove(UUID driverId) {
        CellKey cellKey = driverCells.remove(driverId);
        if (cellKey!=null) {
            removeFromCell(cellKey, driverId);
        }
    }

    // remove a driver id from a cell
    public void removeFromCell(CellKey cellKey, UUID driverId) {
        List<Entry> bucket = cells.get(cellKey);
        if (bucket!=null) {
            bucket.removeIf(entry -> entry.driverId().equals(driverId));
        }
    }

    // get the size of cells
    public int size() {
        return cells.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    // find the drivers within radiusKm of longitude, latitude, as nearest will be the first one
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
            double distanceKm,
            double rating,
            LocalDateTime availableSince
    ) {}
}
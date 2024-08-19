package com.example.satisfactorymanager.utils;

/**
 * Copper
 */
public class Copper extends Resource {
    public Copper(float productionRate, float utilizationRate) {
        super(ResourceType.Copper, productionRate, utilizationRate);
    }

    public Copper(float productionRate) {
        super(ResourceType.Copper, productionRate);
    }
}

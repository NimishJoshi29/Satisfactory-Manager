package com.example.satisfactorymanager.utils;

/**
 * Limestone
 */
public class Limestone extends Resource {
    Limestone(float productionRate, float utilizationRate) {
        super(ResourceType.Limestone, productionRate, utilizationRate);
    }

    public Limestone(float productionRate) {
        super(ResourceType.Limestone, productionRate);
    }
}

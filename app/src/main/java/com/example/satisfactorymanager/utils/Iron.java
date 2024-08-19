package com.example.satisfactorymanager.utils;

/**
 * Iron
 */
public class Iron extends Resource {
    public Iron(float productionRate, float utilizationRate) {
        super(ResourceType.Iron, productionRate, utilizationRate);
    }

    public Iron(float productionRate) {
        super(ResourceType.Iron, productionRate);
    }
}

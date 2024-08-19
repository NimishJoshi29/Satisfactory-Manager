package com.example.satisfactorymanager.utils;

/**
 * Water
 */
public class Water extends Resource {
    Water(float productionRate, float utilizationRate) {
        super(ResourceType.Water, productionRate, utilizationRate);
    }

    public Water(float productionRate) {
        super(ResourceType.Water, productionRate);
    }
}

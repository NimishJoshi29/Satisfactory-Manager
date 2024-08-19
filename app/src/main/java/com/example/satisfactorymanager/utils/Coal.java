package com.example.satisfactorymanager.utils;

/**
 * Coal
 */
public class Coal extends Resource {
    public Coal(float productionRate, float utilizationRate) {
        super(ResourceType.Coal, productionRate, utilizationRate);
    }

    public Coal(float productionRate) {
        super(ResourceType.Coal, productionRate);
    }
}

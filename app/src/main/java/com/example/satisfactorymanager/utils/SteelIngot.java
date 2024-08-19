package com.example.satisfactorymanager.utils;

/**
 * SteelIngot
 */
public class SteelIngot extends Resource {

    public SteelIngot(float productionRate, float utilizationRate) {
        super(ResourceType.SteelIngot, productionRate, utilizationRate);
    }

    public SteelIngot(float productionRate){
        super(ResourceType.SteelIngot,productionRate,0);
    }
}

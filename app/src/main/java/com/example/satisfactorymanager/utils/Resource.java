package com.example.satisfactorymanager.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Resource implements Serializable {
    private ResourceType resourceType;
    private float productionRate;
    private float utilizationRate;
    private float utilizationRatio;
    private List<Part> usages;

    public Resource(ResourceType resourceType, float productionRate, float utilizationRate) {
        this.resourceType = resourceType;
        this.productionRate = productionRate;
        this.utilizationRate = utilizationRate;
        usages = new ArrayList<>();
        calculateUtilizationRatio();
    }

    public Resource(ResourceType resourceType, float productionRate) {
        this.resourceType = resourceType;
        this.productionRate = productionRate;
        this.utilizationRate = 0;
        usages = new ArrayList<>();
        calculateUtilizationRatio();
    }

    public float getUtilizationRatio() {
        return utilizationRatio;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public float getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(float productionRate) {
        this.productionRate = productionRate;
        calculateUtilizationRatio();
    }

    public void increaseProductionRate(float by) {
        productionRate += by;
    }

    public float getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(float utilizationRate) {
        this.utilizationRate = utilizationRate;
        calculateUtilizationRatio();
    }

    private void calculateUtilizationRatio() {
        if (productionRate == 0)
            utilizationRatio = 0;
        else
            utilizationRatio = utilizationRate / productionRate;
    }

    @Override
    public String toString() {
        return "Resource [resourceType=" + resourceType + ", productionRate=" + productionRate + ", utilizationRate="
                + utilizationRate + ", utilizationRatio=" + utilizationRatio + "]";
    }

    public boolean addUsage(Part usage) {
        if (usage.getInputResourceType() == resourceType) {
            usages.add(usage);
            calculateUtilizationRate();
            calculateUtilizationRatio();
            return true;
        } else {
            System.out.println("Raw Material mismatch.");
            return false;
        }
    }

    private void calculateUtilizationRate() {
        utilizationRate = 0;
        for (Part usage : usages) {
            utilizationRate += usage.getInputRate();
        }
    }

    public List<Part> getUsages() {
        return usages;
    }

}

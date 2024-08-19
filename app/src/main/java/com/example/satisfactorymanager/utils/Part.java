package com.example.satisfactorymanager.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Usage
 */
public class Part implements Serializable {
    private float inputRate;
    private ResourceType inputResourceType;
    private float productionRate;
    private PartType partType;
    private float utilizationRate;
    private List<Part> usages;

    public Part(float inputRate, ResourceType inputResourceType, float productionRate, PartType partType) {
        this.inputRate = inputRate;
        this.inputResourceType = inputResourceType;
        this.productionRate = productionRate;
        this.partType = partType;
        this.utilizationRate = 0;
        usages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Usage [inputRate=" + inputRate + ", productionRate=" + productionRate + ", inputRawMaterial="
                + inputResourceType + ", partType=" + partType + ", utilizationRate=" + utilizationRate + "]";
    }

    public float getInputRate() {
        return inputRate;
    }

    public void setInputRate(float inputRate) {
        this.inputRate = inputRate;
    }

    public ResourceType getInputResourceType() {
        return inputResourceType;
    }

    public void setInputResourceType(ResourceType inputResourceType) {
        this.inputResourceType = inputResourceType;
    }

    public float getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(float productionRate) {
        this.productionRate = productionRate;
    }

    public PartType getPartType() {
        return partType;
    }

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    public float getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(float utilizationRate) {
        this.utilizationRate = utilizationRate;
    }
}

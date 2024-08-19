package com.example.satisfactorymanager.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory
 */
public class Inventory implements Serializable {

    private final List<Resource> rawMaterials;
    private final List<Part> parts;

    public Inventory() {
        rawMaterials = new ArrayList<>();
        parts = new ArrayList<>();
    }

    public String getRawMaterialString() {
        StringBuilder returnString = new StringBuilder();
        returnString.append("Inventory { \n\tRaw Materials\n");
        for (int i = 0; i < rawMaterials.size(); i++)
            returnString.append(String.format("\t\t%d. %s\n", (i + 1), rawMaterials.get(i).toString()));
        returnString.append("}");
        return returnString.toString();
    }

    public List<Resource> getRawMaterials() {
        return rawMaterials;
    }

    public void addPart(Part usage) {
        for (Part part : parts) {
            if (part.getPartType() == usage.getPartType()) {
                part.setProductionRate(part.getProductionRate() + usage.getProductionRate());
                part.setInputRate(part.getInputRate() + usage.getInputRate());
                return;
            }
        }
        parts.add(usage);
    }

    public String getPartString() {
        StringBuilder returnString = new StringBuilder();
        returnString.append("Inventory { \n\tParts\n");
        for (int i = 0; i < parts.size(); i++)
            returnString.append(String.format("\t\t%d. %s\n", (i + 1), parts.get(i).toString()));
        returnString.append("}");
        return returnString.toString();
    }

    public void addRawMaterial(Resource resource) {
        for (Resource rawMaterial : rawMaterials) {
            if (rawMaterial.getResourceType() == resource.getResourceType()) {
                rawMaterial.increaseProductionRate(resource.getProductionRate());
                return;
            }
        }
        rawMaterials.add(resource);
    }

    @Override
    public String toString() {
        return getRawMaterialString() + getPartString();
    }
}

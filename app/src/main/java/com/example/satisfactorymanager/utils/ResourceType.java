package com.example.satisfactorymanager.utils;

import androidx.annotation.NonNull;

public enum ResourceType {
    Iron,
    Copper,
    Limestone,
    Coal,
    Water,
    SteelIngot{
        @NonNull
        @Override
        public String toString() {
            return "Steel Ingot";
        }
    },
    Leaves,
    Wood
}

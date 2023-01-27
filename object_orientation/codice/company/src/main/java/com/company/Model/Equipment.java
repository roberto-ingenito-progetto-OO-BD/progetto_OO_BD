package com.company.Model;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public class Equipment {
    private String name;
    private String type;
    private @Nullable String techSpecs;


    public Equipment(
            String name,
            String type,
            @Nullable String techSpecs
    ) {
        this.name = name;
        this.type = type;
        this.techSpecs = techSpecs;
    }


    /// GETTERS
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public @Nullable String getTechSpecs() {
        return techSpecs;
    }
}

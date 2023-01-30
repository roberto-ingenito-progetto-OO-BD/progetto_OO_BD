package com.company.Model;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public class Equipment {
    private final String name;
    private final String type;
    private final @Nullable String techSpecs;

    private final float price;
    private final LocalDate purchaseDate;



    public Equipment(
            String name,
            String type,
            @Nullable String techSpecs,
            Float price,
            LocalDate purchaseDate
    ) {
        this.name = name;
        this.type = type;
        this.techSpecs = techSpecs;
        this.price = price;
        this.purchaseDate = purchaseDate;
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
    public float getPrice() {
        return price;
    }
}

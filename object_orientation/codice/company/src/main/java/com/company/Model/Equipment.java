package com.company.Model;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

@SuppressWarnings("ClassCanBeRecord")
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
            float price,
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

    @SuppressWarnings("unused")
    public float getPrice() {
        return price;
    }

    @SuppressWarnings("unused")
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
}

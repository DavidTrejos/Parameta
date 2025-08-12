package com.parameta.dto;

/**
 * Diseño inmutable. El compilador generado constructores y get and setter.
 * @param years
 * @param months
 * @param days
 */
public record AgeDetail (
    int years,
    int months,
    int days
){}

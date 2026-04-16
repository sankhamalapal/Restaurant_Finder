package dev.sankha.restaurant_finder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Restaurant(String name,
                         Rating rating,
                         List<Cuisine> cuisines,
                         Address address) {
}

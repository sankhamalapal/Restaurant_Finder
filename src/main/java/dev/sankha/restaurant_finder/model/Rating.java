package dev.sankha.restaurant_finder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Rating(Integer count,
                     Double starRating) {
}

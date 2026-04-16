package dev.sankha.restaurant_finder.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Address(String city,
                      String firstLine,
                      String postalCode) {
}

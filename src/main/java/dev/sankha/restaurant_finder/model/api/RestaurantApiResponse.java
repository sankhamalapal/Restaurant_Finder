package dev.sankha.restaurant_finder.model.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RestaurantApiResponse(
        @JsonAlias("restaurants") List<Restaurant> restaurantList) {
}

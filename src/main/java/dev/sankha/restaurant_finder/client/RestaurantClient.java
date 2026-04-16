package dev.sankha.restaurant_finder.client;

import dev.sankha.restaurant_finder.model.Restaurant;

import java.util.List;

public interface RestaurantClient {
    List<Restaurant> fetchRestaurants(String postcode);
}

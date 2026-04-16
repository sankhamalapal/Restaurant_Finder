package dev.sankha.restaurant_finder.service;

import dev.sankha.restaurant_finder.client.RestaurantClient;
import dev.sankha.restaurant_finder.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantClient restaurantClient;

    public RestaurantService(RestaurantClient restaurantClient) {
        this.restaurantClient = restaurantClient;
    }

    public List<Restaurant> getRestaurants(String postcode) {
        return restaurantClient.fetchRestaurants(postcode);
    }
}

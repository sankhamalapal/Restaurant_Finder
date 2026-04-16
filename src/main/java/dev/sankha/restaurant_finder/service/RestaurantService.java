package dev.sankha.restaurant_finder.service;

import dev.sankha.restaurant_finder.client.JETRestaurantApiClient;
import dev.sankha.restaurant_finder.client.RestaurantClient;
import dev.sankha.restaurant_finder.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantClient restaurantClient;

    public RestaurantService(RestaurantClient restaurantClient) {
        log.info("Initializing RestaurantService with RestaurantClient: {}", restaurantClient.getClass().getSimpleName());
        this.restaurantClient = restaurantClient;
    }

    public List<Restaurant> getRestaurants(String postcode) {
        log.info("RestaurantService - Fetching restaurants for postcode: {}", postcode);
        return restaurantClient.fetchRestaurants(postcode);
    }
}

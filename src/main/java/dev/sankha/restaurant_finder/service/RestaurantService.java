package dev.sankha.restaurant_finder.service;

import dev.sankha.restaurant_finder.client.RestaurantClient;
import dev.sankha.restaurant_finder.model.api.Cuisine;
import dev.sankha.restaurant_finder.model.api.Restaurant;
import dev.sankha.restaurant_finder.model.dto.RestaurantDTO;
import dev.sankha.restaurant_finder.model.dto.RestaurantResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantClient restaurantClient;

    public RestaurantService(RestaurantClient restaurantClient) {
        this.restaurantClient = restaurantClient;
    }

    public RestaurantResponse getRestaurants(String postcode) {
        log.info("RestaurantService - Fetching restaurants for postcode: {}", postcode);
        List<Restaurant> all = restaurantClient.fetchRestaurants(postcode);
        List<RestaurantDTO> limited = all.stream()
                .limit(10)
                .map(this::toDTO)
                .toList();
        return new RestaurantResponse(limited, all.size());
    }

    private RestaurantDTO toDTO(Restaurant restaurant) {

        List<String> cuisines = restaurant.cuisines() != null ?
                restaurant.cuisines().stream()
                        .map(Cuisine::name)
                        .toList() :
                List.of();

        double rating = (restaurant.rating() != null && restaurant.rating().starRating() != null) ?
                restaurant.rating().starRating() : 0.0;

        String address = restaurant.address() != null ?
                restaurant.address().firstLine().strip().replace("\n", " ") + ", "
                        + restaurant.address().city().strip() + ", "
                        + restaurant.address().postalCode().strip() :
                "";

        return new RestaurantDTO(restaurant.name(), cuisines, rating, address);
    }
}

package dev.sankha.restaurant_finder.controller;

import dev.sankha.restaurant_finder.model.Restaurant;
import dev.sankha.restaurant_finder.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
    private final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService){
        log.info("Initializing RestaurantController with RestaurantService: {}", restaurantService.getClass().getSimpleName());
        this.restaurantService = restaurantService;
    }
    @GetMapping("/restaurants/{postcode}")
    public List<Restaurant> getRestaurantsByPostcode(@PathVariable String postcode) {
        log.info("RestaurantController - Received request for restaurants in postcode: {}", postcode);
        return restaurantService.getRestaurants(postcode);
    }
}

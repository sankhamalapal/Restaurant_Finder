package dev.sankha.restaurant_finder.controller;

import dev.sankha.restaurant_finder.model.dto.RestaurantDTO;
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
        this.restaurantService = restaurantService;
    }
    @GetMapping("/restaurants/{postcode}")
    public List<RestaurantDTO> getRestaurantsByPostcode(@PathVariable String postcode) {
        log.info("RestaurantController - Received request for restaurants in postcode: {}", postcode);
        List<RestaurantDTO> restaurants = restaurantService.getRestaurants(postcode);
        displayConsole(restaurants, postcode);
        return restaurants;
    }

    public void displayConsole(List<RestaurantDTO> restaurants, String postcode) {
        log.info("RestaurantController - displayConsole called");
        log.info("========================================");
        log.info(" JET Restaurant Finder | {}", postcode);
        log.info("========================================");

        for (int i = 0; i < restaurants.size(); i++) {
            RestaurantDTO r = restaurants.get(i);
            log.info("\n{}. {}", i + 1, r.name());
            log.info("   Cuisines : {}", String.join(", ", r.cuisines()));
            log.info("   Rating   : {}", r.rating());
            log.info("   Address  : {}", r.address());
        }

        log.info("\n========================================");
    }
}

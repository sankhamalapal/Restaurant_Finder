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

    private void displayConsole(List<RestaurantDTO> restaurants, String postcode) {
        System.out.println("========================================");
        System.out.println(" JET Restaurant Finder | "+ postcode);
        System.out.println("========================================");

        for (int i = 0; i < restaurants.size(); i++) {
            RestaurantDTO r = restaurants.get(i);
            System.out.println("\n"+ (i + 1)+". "+ r.name());
            System.out.println("   Cuisines : "+ String.join(", ", r.cuisines()));
            System.out.println("   Rating   : "+ r.rating());
            System.out.println("   Address  : "+ r.address());
        }

        System.out.println("\n========================================");
    }
}

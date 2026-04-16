package dev.sankha.restaurant_finder.controller;

import dev.sankha.restaurant_finder.model.api.Address;
import dev.sankha.restaurant_finder.model.api.Cuisine;
import dev.sankha.restaurant_finder.model.api.Rating;
import dev.sankha.restaurant_finder.model.api.Restaurant;
import dev.sankha.restaurant_finder.model.dto.RestaurantDTO;
import dev.sankha.restaurant_finder.service.RestaurantService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantControllerTest {

    private static Restaurant restaurant(String name) {
        return new Restaurant(name, new Rating(100, 4.2), List.of(new Cuisine("Italian")),
                new Address("London", "1 High Street", "EC4M 7RF"));
    }

    private static RestaurantController controllerWith(List<Restaurant> restaurants) {
        RestaurantService service = new RestaurantService(postcode -> restaurants);
        return new RestaurantController(service);
    }

    @Test
    void returnsAllRestaurantsForPostcode() {
        List<RestaurantDTO> result = controllerWith(List.of(
                restaurant("Pizza Place"),
                restaurant("Burger Barn"),
                restaurant("Sushi World")
        )).getRestaurantsByPostcode("EC4M");

        assertEquals(3, result.size());
    }

    @Test
    void returnsEmptyListWhenNoRestaurantsFound() {
        assertTrue(controllerWith(List.of()).getRestaurantsByPostcode("EC4M").isEmpty());
    }

    @Test
    void returnsSingleRestaurantByName() {
        List<RestaurantDTO> result = controllerWith(List.of(
                restaurant("Noodle House")
        )).getRestaurantsByPostcode("EC4M");

        assertEquals("Noodle House", result.get(0).name());
    }

    @Test
    void passesPostcodeToService() {
        String[] captured = new String[1];
        RestaurantService service = new RestaurantService(postcode -> {
            captured[0] = postcode;
            return List.of();
        });
        new RestaurantController(service).getRestaurantsByPostcode("SW1A");

        assertEquals("SW1A", captured[0]);
    }

    @Test
    void preservesOrderOfRestaurantsFromService() {
        List<RestaurantDTO> result = controllerWith(List.of(
                restaurant("Alpha"),
                restaurant("Beta"),
                restaurant("Gamma")
        )).getRestaurantsByPostcode("EC4M");

        List<String> names = result.stream().map(RestaurantDTO::name).toList();
        assertEquals(List.of("Alpha", "Beta", "Gamma"), names);
    }
}
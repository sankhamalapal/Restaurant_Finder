package dev.sankha.restaurant_finder.service;

import dev.sankha.restaurant_finder.client.RestaurantClient;
import dev.sankha.restaurant_finder.model.Address;
import dev.sankha.restaurant_finder.model.Cuisine;
import dev.sankha.restaurant_finder.model.Rating;
import dev.sankha.restaurant_finder.model.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantServiceTest {

    private static Restaurant restaurant(String name) {
        return new Restaurant(name, new Rating(100, 4.2), List.of(new Cuisine("Italian")),
                new Address("London", "1 High Street", "EC4M 7RF"));
    }

    private static RestaurantService serviceWith(List<Restaurant> restaurants) {
        RestaurantClient stub = postcode -> restaurants;
        return new RestaurantService(stub);
    }

    @Test
    void returnsAllRestaurantsFromClient() {
        List<Restaurant> result = serviceWith(List.of(
                restaurant("Pizza Place"),
                restaurant("Burger Barn"),
                restaurant("Sushi World")
        )).getRestaurants("EC4M");

        assertEquals(3, result.size());
    }

    @Test
    void returnsEmptyWhenClientReturnsEmpty() {
        assertTrue(serviceWith(List.of()).getRestaurants("EC4M").isEmpty());
    }

    @Test
    void returnsSingleRestaurantByName() {
        List<Restaurant> result = serviceWith(List.of(
                restaurant("Noodle House")
        )).getRestaurants("EC4M");

        assertEquals("Noodle House", result.get(0).name());
    }

    @Test
    void passesPostcodeToClient() {
        String[] captured = new String[1];
        RestaurantClient stub = postcode -> {
            captured[0] = postcode;
            return List.of();
        };
        new RestaurantService(stub).getRestaurants("SW1A");

        assertEquals("SW1A", captured[0]);
    }

    @Test
    void preservesOrderOfRestaurantsFromClient() {
        List<Restaurant> result = serviceWith(List.of(
                restaurant("Alpha"),
                restaurant("Beta"),
                restaurant("Gamma")
        )).getRestaurants("EC4M");

        List<String> names = result.stream().map(Restaurant::name).toList();
        assertEquals(List.of("Alpha", "Beta", "Gamma"), names);
    }
}
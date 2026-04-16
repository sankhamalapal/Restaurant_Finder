package dev.sankha.restaurant_finder.service;

import dev.sankha.restaurant_finder.client.RestaurantClient;
import dev.sankha.restaurant_finder.model.api.Address;
import dev.sankha.restaurant_finder.model.api.Cuisine;
import dev.sankha.restaurant_finder.model.api.Rating;
import dev.sankha.restaurant_finder.model.api.Restaurant;
import dev.sankha.restaurant_finder.model.dto.RestaurantDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantServiceTest {

    // Builds a default restaurant with a given name for tests that only care about the name
    private static Restaurant restaurant(String name) {
        return new Restaurant(name, new Rating(100, 4.2), List.of(new Cuisine("Italian")),
                new Address("London", "1 High Street", "EC4M 7RF"));
    }

    // Wires a stub client that returns a fixed list, so tests don't hit the real API
    private static RestaurantService serviceWith(List<Restaurant> restaurants) {
        RestaurantClient stub = postcode -> restaurants;
        return new RestaurantService(stub);
    }

    // Verifies the service returns all restaurants the client provides
    @Test
    void returnsAllRestaurantsFromClient() {
        List<RestaurantDTO> result = serviceWith(List.of(
                restaurant("Pizza Place"),
                restaurant("Burger Shop"),
                restaurant("Sushi World")
        )).getRestaurants("EC4M 7RF");

        assertEquals(3, result.size());
    }

    // Verifies the service handles an empty response without errors
    @Test
    void returnsEmptyWhenClientReturnsEmpty() {
        assertTrue(serviceWith(List.of()).getRestaurants("CT1 2EH").isEmpty());
    }

    // Verifies the restaurant name is correctly mapped to the DTO
    @Test
    void returnsSingleRestaurantByName() {
        List<RestaurantDTO> result = serviceWith(List.of(
                restaurant("Noodle House")
        )).getRestaurants("EC4M 7RF");

        assertEquals("Noodle House", result.get(0).name());
    }

    // Verifies the service does not reorder restaurants returned by the client
    @Test
    void preservesOrderOfRestaurantsFromClient() {
        List<RestaurantDTO> result = serviceWith(List.of(
                restaurant("Alpha"),
                restaurant("Beta"),
                restaurant("Gamma")
        )).getRestaurants("EC4M 7RF");

        List<String> names = result.stream().map(RestaurantDTO::name).toList();
        assertEquals(List.of("Alpha", "Beta", "Gamma"), names);
    }

    // Verifies cuisine objects are mapped to a plain list of cuisine name strings
    @Test
    void mapsCuisinesToListOfNames() {
        Restaurant r = new Restaurant("Pasta Co", new Rating(50, 4.0),
                List.of(new Cuisine("Italian"), new Cuisine("Pizza")),
                new Address("London", "1 High Street", "EC4M 7RF"));

        RestaurantDTO dto = serviceWith(List.of(r)).getRestaurants("EC4M").get(0);

        assertEquals(List.of("Italian", "Pizza"), dto.cuisines());
    }

    // Verifies the star rating value is correctly mapped to the DTO
    @Test
    void mapsStarRatingToDTO() {
        Restaurant r = new Restaurant("Ramen Bar", new Rating(200, 4.7),
                List.of(new Cuisine("Japanese")),
                new Address("London", "2 Low Street", "EC4M 7RF"));

        RestaurantDTO dto = serviceWith(List.of(r)).getRestaurants("EC4M 7RF").get(0);

        assertEquals(4.7, dto.rating());
    }

    // Verifies the address is formatted as "firstLine, city, postalCode"
    @Test
    void formatsAddressAsFirstLineCityPostalCode() {
        Restaurant r = new Restaurant("Sushi World", new Rating(80, 4.1),
                List.of(new Cuisine("Japanese")),
                new Address("Canterbury", "53 St Peter St", "CT1 2BE"));

        RestaurantDTO dto = serviceWith(List.of(r)).getRestaurants("CT1 2BE").get(0);

        assertEquals("53 St Peter St, Canterbury, CT1 2BE", dto.address());
    }

    // Verifies the service handles a restaurant with no cuisines without errors
    @Test
    void handlesRestaurantWithNoCuisines() {
        Restaurant r = new Restaurant("Plain Eats", new Rating(50, 3.5),
                List.of(),
                new Address("London", "10 Bread St", "EC4M 7RF"));

        RestaurantDTO dto = serviceWith(List.of(r)).getRestaurants("EC4M 7RF").get(0);

        assertTrue(dto.cuisines().isEmpty());
    }

    // Verifies only the first 10 restaurants are returned when the client provides 11
    @Test
    void returnsOnlyFirstTenWhenClientProvidesEleven() {
        List<Restaurant> eleven = IntStream.rangeClosed(1, 11)
                .mapToObj(i -> restaurant("Restaurant " + i))
                .toList();

        List<RestaurantDTO> result = serviceWith(eleven).getRestaurants("EC4M 7RF");

        assertEquals(10, result.size());
    }

    // Verifies all restaurants are returned when the client provides fewer than 10
    @Test
    void returnsAllWhenLessThanTen() {
        List<Restaurant> seven = IntStream.rangeClosed(1, 7)
                .mapToObj(i -> restaurant("Restaurant " + i))
                .toList();

        List<RestaurantDTO> result = serviceWith(seven).getRestaurants("EC4M 7RF");

        assertEquals(7, result.size());
    }


}
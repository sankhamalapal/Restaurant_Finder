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
        List<RestaurantDTO> result = serviceWith(List.of(
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
        List<RestaurantDTO> result = serviceWith(List.of(
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
        List<RestaurantDTO> result = serviceWith(List.of(
                restaurant("Alpha"),
                restaurant("Beta"),
                restaurant("Gamma")
        )).getRestaurants("EC4M");

        List<String> names = result.stream().map(RestaurantDTO::name).toList();
        assertEquals(List.of("Alpha", "Beta", "Gamma"), names);
    }

    @Test
    void mapsCuisinesToListOfNames() {
        Restaurant r = new Restaurant("Pasta Co", new Rating(50, 4.0),
                List.of(new Cuisine("Italian"), new Cuisine("Pizza")),
                new Address("London", "1 High Street", "EC4M 7RF"));

        RestaurantDTO dto = serviceWith(List.of(r)).getRestaurants("EC4M").get(0);

        assertEquals(List.of("Italian", "Pizza"), dto.cuisines());
    }

    @Test
    void mapsStarRatingToDTO() {
        Restaurant r = new Restaurant("Ramen Bar", new Rating(200, 4.7),
                List.of(new Cuisine("Japanese")),
                new Address("London", "2 Low Street", "EC4M 7RF"));

        RestaurantDTO dto = serviceWith(List.of(r)).getRestaurants("EC4M").get(0);

        assertEquals(4.7, dto.rating());
    }

    @Test
    void formatsAddressAsFirstLineCityPostalCode() {
        Restaurant r = new Restaurant("Sushi World", new Rating(80, 4.1),
                List.of(new Cuisine("Japanese")),
                new Address("Canterbury", "53 St Peter St", "CT1 2BE"));

        RestaurantDTO dto = serviceWith(List.of(r)).getRestaurants("EC4M").get(0);

        assertEquals("53 St Peter St, Canterbury, CT1 2BE", dto.address());
    }

    @Test
    void limitsResultsToTen() {
        List<Restaurant> eleven = IntStream.rangeClosed(1, 11)
                .mapToObj(i -> restaurant("Restaurant " + i))
                .toList();

        List<RestaurantDTO> result = serviceWith(eleven).getRestaurants("EC4M");

        assertEquals(10, result.size());
    }
}
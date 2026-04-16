package dev.sankha.restaurant_finder.client;

import dev.sankha.restaurant_finder.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class JETRestaurantApiClient implements RestaurantClient{

    private static final Logger log = LoggerFactory.getLogger(JETRestaurantApiClient.class);
    private final String BASE_URL = "https://uk.api.just-eat.io/discovery/uk/restaurants/enriched/bypostcode/";
    private final RestClient restClient;

    public JETRestaurantApiClient(){
        log.info("Initializing RestClient for JET API with base URL: {}", BASE_URL);
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Override
    public List<Restaurant> fetchRestaurants(String postcode) {
        return List.of();
    }
}

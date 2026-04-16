package dev.sankha.restaurant_finder.client;

import dev.sankha.restaurant_finder.model.api.Restaurant;
import dev.sankha.restaurant_finder.model.api.RestaurantApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

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
        try{
            RestaurantApiResponse response = restClient.get()
                    .uri("/{postcode}",postcode.strip().replace(" ", "").toLowerCase())
                    .retrieve()
                    .body(RestaurantApiResponse.class);

            if(response==null || response.restaurantList()==null){
                log.warn("Received empty response from JET API for postcode: {}", postcode);
                return List.of();
            }
            log.info("Successfully fetched {} restaurants for postcode: {}", response.restaurantList().size(), postcode);
            return response.restaurantList();

        } catch (RestClientException e) {
            log.error("Error fetching restaurants for postcode '{}': {}", postcode, e.getMessage());
            return List.of();
        }
    }
}

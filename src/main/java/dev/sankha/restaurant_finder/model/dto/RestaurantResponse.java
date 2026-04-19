package dev.sankha.restaurant_finder.model.dto;

import java.util.List;

public record RestaurantResponse(List<RestaurantDTO> restaurants,
                                 int total) {
}

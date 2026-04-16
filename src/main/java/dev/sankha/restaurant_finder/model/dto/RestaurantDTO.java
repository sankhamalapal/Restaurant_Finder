package dev.sankha.restaurant_finder.model.dto;

import java.util.List;

public record RestaurantDTO(String name,
                            List<String> cuisines,
                            double rating,
                            String address) {
}

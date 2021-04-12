package com.swm.sprint1.repository.restaurant;

import com.swm.sprint1.domain.Restaurant;

import java.util.List;

public interface RestaurantRepositoryCustom {

    List<Restaurant> findAllByIdOrderByIdAsc(List<Long> restaurantId);

}

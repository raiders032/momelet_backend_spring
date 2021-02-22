package com.swm.sprint1.repository.restaurant;

import com.swm.sprint1.domain.Restaurant;
import com.swm.sprint1.payload.request.RestaurantSearchCondition;
import com.swm.sprint1.payload.response.RestaurantResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantRepositoryCustom {

    List<Restaurant> findAllByIdOrderByIdAsc(List<Long> restaurantId);

    Page<RestaurantResponseDto> searchRestaurantsOrderByLikeCount(Pageable pageable, RestaurantSearchCondition condition);

    Page<RestaurantResponseDto> searchRestaurantsOrderByDistance(Pageable pageable, RestaurantSearchCondition condition);

    List<RestaurantResponseDto> findDtosByUserId(Long userId, BigDecimal longitude, BigDecimal latitude, BigDecimal radius);

    List<RestaurantResponseDto> findDtosById(List<Long> restaurantIds);
}

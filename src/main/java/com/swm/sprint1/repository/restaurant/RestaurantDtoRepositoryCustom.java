package com.swm.sprint1.repository.restaurant;

import com.swm.sprint1.payload.request.RestaurantSearchCondition;
import com.swm.sprint1.payload.response.RestaurantResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantDtoRepositoryCustom {
    Page<RestaurantResponseDto> searchAllOrderByLikeCount(Pageable pageable, RestaurantSearchCondition condition);

    Page<RestaurantResponseDto> searchAllOrderByDistance(Pageable pageable, RestaurantSearchCondition condition);

    List<RestaurantResponseDto> findAllByUserId(Long userId, BigDecimal longitude, BigDecimal latitude, BigDecimal radius);

    List<RestaurantResponseDto> findAllById(List<Long> restaurantIds);
}

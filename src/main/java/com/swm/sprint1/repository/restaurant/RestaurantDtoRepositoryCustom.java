package com.swm.sprint1.repository.restaurant;

import com.swm.sprint1.dto.request.RestaurantSearchConditionRequest;
import com.swm.sprint1.dto.RestaurantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantDtoRepositoryCustom {
    Page<RestaurantDto> searchAllOrderByLikeCount(Pageable pageable, RestaurantSearchConditionRequest condition);

    Page<RestaurantDto> searchAllOrderByDistance(Pageable pageable, RestaurantSearchConditionRequest condition);

    List<RestaurantDto> findAllByUserId(Long userId, BigDecimal longitude, BigDecimal latitude, BigDecimal radius);

    List<RestaurantDto> findAllById(List<Long> restaurantIds);
}

package com.swm.sprint1.repository.photo;

import com.swm.sprint1.dto.RestaurantPhotoDto;

import java.util.List;

public interface RestaurantPhotoDtoRepositoryCustom {
    List<RestaurantPhotoDto> findAllByRestaurantId(Long restaurantId);
}

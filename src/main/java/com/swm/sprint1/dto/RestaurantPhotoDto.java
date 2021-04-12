package com.swm.sprint1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RestaurantPhotoDto {
    private Long id;

    private Long restaurantId;

    private Long userId;

    private String filename;

    private String path;
}

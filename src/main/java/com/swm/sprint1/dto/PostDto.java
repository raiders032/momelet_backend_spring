package com.swm.sprint1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    private Long restaurantId;

    private String imageUrl;

    private String claim;
}

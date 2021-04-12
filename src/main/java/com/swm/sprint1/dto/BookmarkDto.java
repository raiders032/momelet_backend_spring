package com.swm.sprint1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class BookmarkDto {

    private Long id;
    private Long restaurantId;
    private String name;
    private String thumUrl;
    private Long likecnt;
    private BigDecimal longitude;
    private BigDecimal latitude;

}

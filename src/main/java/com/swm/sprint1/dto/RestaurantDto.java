package com.swm.sprint1.dto;

import com.swm.sprint1.domain.Restaurant;
import com.swm.sprint1.dto.MenuDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode(of="id")
@Setter
@Getter
@NoArgsConstructor
public class RestaurantDto {
    private BigInteger id;
    private String name;
    private String thumUrl;
    private List<MenuDto> menu = new ArrayList<>();
    private String openingHours;
    private String address;
    private String roadAddress;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String phoneNumber;
    private Long like;
    private Double distance;

    //식당 조회 sql
    public RestaurantDto(BigInteger id, String name, String thumUrl, String menu, String openingHours, String address, String roadAddress, BigDecimal longitude, BigDecimal latitude, String phoneNumber, BigInteger likeCount) {
        this.id = id;
        this.name = name;
        this.thumUrl = thumUrl;
        this.openingHours = openingHours;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
        if(menu != null)
            this.menu = Arrays.stream(menu.split("`")).map(MenuDto::new).collect(Collectors.toList());
        this.like = likeCount.longValue();
    }

    /* 식당 검색 sql */
    public RestaurantDto(BigInteger id, String name, String thumUrl, String menu,
                         String openingHours, String address, String roadAddress,
                         BigDecimal longitude, BigDecimal latitude, String phoneNumber,
                         BigInteger likeCount, Double distance) {
        this.id = id;
        this.name = name;
        this.thumUrl = thumUrl;
        this.openingHours = openingHours;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
        if(menu != null)
            this.menu = Arrays.stream(menu.split("`")).map(MenuDto::new).collect(Collectors.toList());
        this.like = likeCount.longValue();
        this.distance = distance;
    }

    public RestaurantDto(BigInteger id, String name, String thumUrl, String menu, String categories, BigDecimal googleRating, Integer googleReviewCount, String openingHours, Integer priceLevel, String address, String roadAddress, BigDecimal longitude, BigDecimal latitude, BigInteger naverId, BigInteger googleId, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.thumUrl = thumUrl;
        this.openingHours = openingHours;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
        if(menu != null)
            this.menu = Arrays.stream(menu.split("`")).map(MenuDto::new).collect(Collectors.toList());
    }

    public RestaurantDto(Long id, String name, String thumUrl, String address, String roadAddress, BigDecimal longitude, BigDecimal latitude, String phoneNumber, Long like) {
        this.id = BigInteger.valueOf(id);
        this.name = name;
        this.thumUrl = thumUrl;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
        this.like = like;
    }

    public RestaurantDto(Long id, String name, String thumUrl, String address, String roadAddress, BigDecimal longitude, BigDecimal latitude, String phoneNumber) {
        this.id = BigInteger.valueOf(id);
        this.name = name;
        this.thumUrl = thumUrl;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
    }

    public RestaurantDto(Restaurant restaurant, Long like){
        this.id = BigInteger.valueOf(restaurant.getId());
        this.name = restaurant.getName() ;
        this.thumUrl = restaurant.getThumUrl();
        this.address = restaurant.getAddress();
        this.roadAddress = restaurant.getRoadAddress();
        this.longitude = restaurant.getLongitude();
        this.latitude = restaurant.getLatitude();
        this.phoneNumber = restaurant.getPhoneNumber();
        this.menu = restaurant.getMenuList().stream().map(MenuDto::new).collect(Collectors.toList());
        this.like = like;
    }

}

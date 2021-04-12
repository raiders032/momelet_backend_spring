package com.swm.sprint1.controller;

import com.swm.sprint1.exception.RequestParamException;
import com.swm.sprint1.dto.request.RestaurantSearchConditionRequest;
import com.swm.sprint1.dto.response.ApiResponse;
import com.swm.sprint1.dto.RestaurantDto;
import com.swm.sprint1.security.CurrentUser;
import com.swm.sprint1.security.UserPrincipal;
import com.swm.sprint1.service.RestaurantService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@PreAuthorize("hasRole('USER')")
@Validated
@RequiredArgsConstructor
@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    @ApiOperation(value = "유저 카테고리 기반 식당 조회", notes = "유저의 카테고리를 기반으로 하여 최대 100개의 주변 식당 목록을 반환합니다.")
    @GetMapping("/api/v1/restaurants/users/{userId}/categories")
    public ResponseEntity<?> getRestaurantWithUserCategory(@CurrentUser UserPrincipal currentUser,
                                                           @RequestParam @DecimalMin("122") @DecimalMax("133") BigDecimal longitude,
                                                           @RequestParam @DecimalMin("32") @DecimalMax("43") BigDecimal latitude,
                                                           @RequestParam @DecimalMin("0.001") @DecimalMax("0.02") BigDecimal radius,
                                                           @PathVariable Long userId) {
        log.debug("GetMapping /api/v1/restaurants/users/{userId}/categories");

        validateUser(currentUser, userId);

        List<RestaurantDto> restaurants = restaurantService.findDtosByUserCategory(userId, longitude, latitude, radius);

        return ResponseEntity.ok(new ApiResponse(true, "유저 카테고리 기반 식당 조회", "restaurants", restaurants));
    }

    private void validateUser(@CurrentUser UserPrincipal userPrincipal, Long userId) {
        if (!userId.equals(userPrincipal.getId())) {
            log.error("jwt token의 유저 아이디와 path param 유저 아이디가 일치하지 않습니다.");
            throw new RequestParamException("jwt token의 유저 아이디와 path param 유저 아이디가 일치하지 않습니다. :" + userId, "103");
        }
    }

    @ApiOperation(value = "유저들의 카테고리 기반 식당 카드 조회", notes = "유저들 카테고리를 기반으로 주변 식당 목록을 반환합니다.")
    @GetMapping("/api/v1/restaurants")
    public ResponseEntity<?> getRestaurant7SimpleCategoryBased(@RequestParam @NotBlank String userId,
                                                               @RequestParam(defaultValue = "") String restaurantId,
                                                               @RequestParam @NotNull @DecimalMin("123") @DecimalMax("133") BigDecimal longitude,
                                                               @RequestParam @DecimalMin("32") @DecimalMax("43") BigDecimal latitude,
                                                               @RequestParam @DecimalMin("0.001") @DecimalMax("0.02") BigDecimal radius) {
        log.debug("GetMapping /api/v1/restaurants");

        List<RestaurantDto> restaurants = restaurantService.getGameCards(userId, restaurantId, longitude, latitude, radius);

        return ResponseEntity.ok(new ApiResponse(true, "유저들의 카테고리 기반 식당 카드 조회", "restaurants", restaurants));
    }

    @ApiOperation(value = "식당 검색", notes = "식당을 검색합니다.")
    @GetMapping("/api/v1/restaurants/search")
    public ResponseEntity<?> getRestaurant(Pageable pageable, @Valid @ModelAttribute RestaurantSearchConditionRequest condition) {
        log.debug("GetMapping /api/v1/restaurants/search");

        Page<RestaurantDto> restaurants = restaurantService.searchRestaurants(pageable, condition);

        return ResponseEntity.ok(new ApiResponse(true, "식당 검색", "restaurants", restaurants));
    }

    @ApiOperation(value = "식당 조회", notes = "식당을 조회합니다.")
    @GetMapping("/api/v1/restaurants/{restaurantId}")
    public ResponseEntity<?> getRestaurant(@PathVariable Long restaurantId) {
        log.debug("GetMapping /api/v1/restaurants/{restaurantId}");

        RestaurantDto restaurant = restaurantService.findDtoById(restaurantId);

        return ResponseEntity.ok(new ApiResponse(true, "식당 조회", "restaurant", restaurant));
    }
}

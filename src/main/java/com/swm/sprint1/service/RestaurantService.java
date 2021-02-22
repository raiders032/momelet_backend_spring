package com.swm.sprint1.service;

import com.swm.sprint1.exception.ResourceNotFoundException;
import com.swm.sprint1.exception.RestaurantLessThan7Exception;
import com.swm.sprint1.payload.request.RestaurantSearchCondition;
import com.swm.sprint1.payload.response.RestaurantResponseDto;
import com.swm.sprint1.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    public List<RestaurantResponseDto> findDtosByUserCategory(Long userId, BigDecimal longitude, BigDecimal latitude, BigDecimal radius) {
        return restaurantRepository.findDtosByUserId(userId, longitude, latitude, radius);
    }

    public Page<RestaurantResponseDto> searchRestaurants(Pageable pageable, RestaurantSearchCondition condition) {
        if (condition.getFilter().equals("like")) {
            return restaurantRepository.searchRestaurantsOrderByLikeCount(pageable, condition);
        }

        return restaurantRepository.searchRestaurantsOrderByDistance(pageable, condition);
    }

    public List<RestaurantResponseDto> getGameCards(String userId, String restaurantId, BigDecimal longitude, BigDecimal latitude, BigDecimal radius) {
        logger.debug("findGameCards 호출");

        List<Long> userIds = getSplitUserIds(userId);
        Set<RestaurantResponseDto> restaurants = new HashSet<>();

        if (!restaurantId.isEmpty()) {
            Set<Long> restaurantIds = new HashSet<>(Arrays.stream(restaurantId.split(",")).map(Long::parseLong).collect(Collectors.toList()));
            restaurants.addAll(restaurantRepository.findDtosById(new ArrayList<>(restaurantIds)));
        }

        if (restaurants.size() < 7) {
            Set<RestaurantResponseDto> candidates = new HashSet<>();
            userIds.forEach(id -> candidates.addAll(restaurantRepository.findDtosByUserId(id, longitude, latitude, radius)));
            List<RestaurantResponseDto> filteredCandidate = candidates.stream()
                    .filter(candidate -> !restaurants.contains(candidate))
                    .collect(Collectors.toList());
            Collections.shuffle(filteredCandidate);
            restaurants.addAll(filteredCandidate.subList(0, 7 - restaurants.size()));
        }

        if (restaurants.size() < 7) {
            throw new RestaurantLessThan7Exception("식당 카드가 7장 미만입니다.");
        }

        return new ArrayList<>(restaurants);
    }

    private List<Long> getSplitUserIds(String userIds) {
        return Arrays.stream(userIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
    }

    public RestaurantResponseDto findDtoById(Long restaurantId) {
        List<RestaurantResponseDto> dtosById = restaurantRepository.findDtosById(Arrays.asList(restaurantId));
        if (!dtosById.isEmpty())
            return dtosById.get(0);
        else
            throw new ResourceNotFoundException("Restaurant", "id", restaurantId, "210");
    }
}

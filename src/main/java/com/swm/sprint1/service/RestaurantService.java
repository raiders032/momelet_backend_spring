package com.swm.sprint1.service;

import com.swm.sprint1.dto.RestaurantDto;
import com.swm.sprint1.dto.request.RestaurantSearchConditionRequest;
import com.swm.sprint1.exception.ResourceNotFoundException;
import com.swm.sprint1.exception.RestaurantLessThan7Exception;
import com.swm.sprint1.repository.restaurant.RestaurantDtoRepository;
import com.swm.sprint1.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDtoRepository restaurantDtoRepository;

    public List<RestaurantDto> findDtosByUserCategory(Long userId, BigDecimal longitude, BigDecimal latitude, BigDecimal radius) {
        return restaurantDtoRepository.findAllByUserId(userId, longitude, latitude, radius);
    }

    public Page<RestaurantDto> searchRestaurants(Pageable pageable, RestaurantSearchConditionRequest condition) {
        if (condition.getFilter().equals("like")) {
            return restaurantDtoRepository.searchAllOrderByLikeCount(pageable, condition);
        }

        return restaurantDtoRepository.searchAllOrderByDistance(pageable, condition);
    }

    public List<RestaurantDto> getGameCards(String userId, String restaurantId, BigDecimal longitude, BigDecimal latitude, BigDecimal radius) {
        log.debug("findGameCards 호출");

        List<Long> userIds = getSplitUserIds(userId);
        Set<RestaurantDto> restaurants = new HashSet<>();

        if (!restaurantId.isEmpty()) {
            Set<Long> restaurantIds = new HashSet<>(Arrays.stream(restaurantId.split(",")).map(Long::parseLong).collect(Collectors.toList()));
            restaurants.addAll(restaurantDtoRepository.findAllById(new ArrayList<>(restaurantIds)));
        }

        if (restaurants.size() < 7) {
            Set<RestaurantDto> candidates = new HashSet<>();
            userIds.forEach(id -> candidates.addAll(restaurantDtoRepository.findAllByUserId(id, longitude, latitude, radius)));
            List<RestaurantDto> filteredCandidate = candidates.stream()
                    .filter(candidate -> !restaurants.contains(candidate))
                    .collect(Collectors.toList());
            if (filteredCandidate.size() < 7 - restaurants.size())
                throw new RestaurantLessThan7Exception("식당 카드가 7장 미만입니다.");
            Collections.shuffle(filteredCandidate);
            restaurants.addAll(filteredCandidate.subList(0, 7 - restaurants.size()));
        }

        return new ArrayList<>(restaurants);
    }

    private List<Long> getSplitUserIds(String userIds) {
        return Arrays.stream(userIds.split(",")).map(Long::parseLong).collect(Collectors.toList());
    }

    public RestaurantDto findDtoById(Long restaurantId) {
        List<RestaurantDto> dtosById = restaurantDtoRepository.findAllById(Arrays.asList(restaurantId));
        if (!dtosById.isEmpty())
            return dtosById.get(0);
        else
            throw new ResourceNotFoundException("Restaurant", "id", restaurantId, "210");
    }
}

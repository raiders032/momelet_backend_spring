package com.swm.sprint1.repository.restaurant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swm.sprint1.domain.Restaurant;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.swm.sprint1.domain.QRestaurant.restaurant;

@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Restaurant> findAllByIdOrderByIdAsc(List<Long> restaurantId) {
        return queryFactory
                .select(restaurant)
                .from(restaurant)
                .where(restaurant.id.in(restaurantId))
                .orderBy(restaurant.id.asc())
                .fetch();
    }



}

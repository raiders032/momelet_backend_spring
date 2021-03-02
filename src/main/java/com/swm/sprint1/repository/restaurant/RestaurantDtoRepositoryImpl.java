package com.swm.sprint1.repository.restaurant;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swm.sprint1.domain.QCategory;
import com.swm.sprint1.domain.Restaurant;
import com.swm.sprint1.payload.request.RestaurantSearchCondition;
import com.swm.sprint1.payload.response.RestaurantResponseDto;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.swm.sprint1.domain.QRestaurant.restaurant;
import static com.swm.sprint1.domain.QRestaurantCategory.restaurantCategory;
import static com.swm.sprint1.domain.QUserCategory.userCategory;
import static com.swm.sprint1.domain.QUserLiking.userLiking;

@RequiredArgsConstructor
public class RestaurantDtoRepositoryImpl implements RestaurantDtoRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final JpaResultMapper jpaResultMapper;

    @Override
    public Page<RestaurantResponseDto> searchAllOrderByLikeCount(Pageable pageable, RestaurantSearchCondition condition) {
        NumberPath<Long> aliasLike = Expressions.numberPath(Long.class, "likeCount");
        List<Tuple> tuples = queryFactory
                .select(restaurant, userLiking.id.count().as(aliasLike))
                .from(restaurant)
                .leftJoin(restaurant.userLikings, userLiking)
                .where( longitudeBetween(condition.getLongitude(), condition.getRadius()),
                        latitudeBetween(condition.getLatitude(), condition.getRadius()),
                        nameLike(condition.getName()))
                .groupBy(restaurant.id)
                .orderBy(aliasLike.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<RestaurantResponseDto> content = tuples.stream()
                .map(tuple -> new RestaurantResponseDto(tuple.get(0, Restaurant.class), tuple.get(1, Long.class)))
                .collect(Collectors.toList());

        JPAQuery<Restaurant> countQuery = queryFactory
                .select(restaurant)
                .from(restaurant)
                .where( longitudeBetween(condition.getLongitude(), condition.getRadius()),
                        latitudeBetween(condition.getLatitude(), condition.getRadius()),
                        nameLike(condition.getName()))
                .groupBy(restaurant.id);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<RestaurantResponseDto> searchAllOrderByDistance(Pageable pageable, RestaurantSearchCondition condition) {

        String sql =
                "   select  " +
                "       r1.restaurant_id, r1.name, r1.thum_url,  " +
                "       group_concat(DISTINCT concat(m.name, '∬', m.price)  order by m.menu_id SEPARATOR '`') as menu,   " +
                "       r1.opening_hours, r1.address, r1.road_address, " +
                "       r1.longitude, r1.latitude, r1.phone_number," +
                "       r1.likecount, r1.distance " +
                "   from(   " +
                "           select  " +
                "               r.*,    " +
                "               count(ul.user_liking_id) as likecount,  " +
                "               (6371*acos(cos(radians(:ulat1))*cos(radians(r.latitude))*cos(radians(r.longitude)   " +
                "               -radians(:ulon1))+sin(radians(:ulat2))*sin(radians(r.latitude)))) AS distance  " +
                "           from restaurant r   " +
                "           left outer join user_liking ul on r.restaurant_id=ul.restaurant_id  " +
                "           where (r.longitude between :lon1 and :lon2)     " +
                "           and (r.latitude between :lat1 and :lat2)  " +
                "           and (r.name like :name)    " +
                "           group by r.restaurant_id    " +
                "           order by distance asc, likecount desc   " +
                "           LIMIT :limit OFFSET :offset    " +
                "       ) r1    " +
                "   left join menu m on r1.restaurant_id = m.restaurant_id  " +
                "   group by r1.restaurant_id   " +
                "   order by r1.distance    ";

        BigDecimal latitude = condition.getLatitude();
        BigDecimal longitude = condition.getLongitude();
        BigDecimal radius = condition.getRadius();

        Query query = em.createNativeQuery(sql)
                .setParameter("ulat1", latitude)
                .setParameter("ulat2", latitude)
                .setParameter("ulon1", longitude)
                .setParameter("lat1", latitude.subtract(radius))
                .setParameter("lat2", latitude.add(radius))
                .setParameter("lon1", longitude.subtract(radius))
                .setParameter("lon2", longitude.add(radius))
                .setParameter("name", "%" + condition.getName() +"%")
                .setParameter("offset", pageable.getOffset())
                .setParameter("limit", pageable.getPageSize());

        List<RestaurantResponseDto> content = jpaResultMapper.list(query, RestaurantResponseDto.class);

        JPAQuery<Restaurant> countQuery = queryFactory
                .select(restaurant)
                .from(restaurant)
                .where( longitudeBetween(longitude, radius),
                        latitudeBetween(latitude,radius),
                        nameLike(condition.getName()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public List<RestaurantResponseDto> findAllByUserId(Long userId, BigDecimal longitude, BigDecimal latitude, BigDecimal radius) {
        QCategory c = new QCategory("c");
        List<Tuple> tuples = queryFactory
                .select(restaurant, userLiking.id.count().as("like"))
                .from(restaurant)
                .join(restaurant.restaurantCategories, restaurantCategory)
                .leftJoin(restaurant.userLikings, userLiking)
                .where( longitudeBetween(longitude, radius),
                        latitudeBetween(latitude, radius),
                        restaurantCategory.category.id.in(JPAExpressions
                                .select(userCategory.category.id)
                                .from(userCategory)
                                .where(userCategory.user.id.eq(userId))))
                .groupBy(restaurant.id)
                .orderBy(Expressions.numberTemplate(Double.class, "rand()").asc())
                .limit(100)
                .fetch();

        List<RestaurantResponseDto> restaurantResponseDtos = tuples.stream()
                .map(tuple -> new RestaurantResponseDto(tuple.get(0, Restaurant.class), tuple.get(1, Long.class)))
                .collect(Collectors.toList());

        return restaurantResponseDtos;
    }

    @Override
    public List<RestaurantResponseDto> findAllById(List<Long> restaurantIds) {
        List<Tuple> tuples = queryFactory
                .select(restaurant, userLiking.id.count().as("like"))
                .from(restaurant)
                .leftJoin(restaurant.userLikings, userLiking)
                .where(restaurant.id.in(restaurantIds))
                .groupBy(restaurant.id)
                .fetch();

        return tuples.stream()
                .map(tuple -> new RestaurantResponseDto(tuple.get(0, Restaurant.class), tuple.get(1, Long.class)))
                .collect(Collectors.toList());
    }

    private BooleanExpression nameLike(String name) {
        return name != null ? restaurant.name.like("%"+name+"%") : null;
    }

    private BooleanExpression latitudeBetween(BigDecimal latitude, BigDecimal radius){
        return latitude != null ? restaurant.latitude.between(latitude.subtract(radius), latitude.add(radius)) : null;
    }

    private BooleanExpression longitudeBetween(BigDecimal longitude, BigDecimal radius){
        return longitude != null ? restaurant.longitude.between(longitude.subtract(radius), longitude.add(radius)) : null;
    }

}

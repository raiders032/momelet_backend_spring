package com.swm.sprint1.repository.menu;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swm.sprint1.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.swm.sprint1.domain.QMenu.menu;

@RequiredArgsConstructor
public class MenuDtoRepositoryImpl implements MenuDtoRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserInfoDto.MenuDto> findMenuResponseDto(Long restaurantId) {
        return queryFactory
                .select(Projections.fields(UserInfoDto.MenuDto.class, menu.id, menu.name, menu.price ))
                .from(menu)
                .where(menu.restaurant.id.eq(restaurantId))
                .fetch();
    }
}

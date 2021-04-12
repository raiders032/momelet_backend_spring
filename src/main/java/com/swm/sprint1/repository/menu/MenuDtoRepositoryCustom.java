package com.swm.sprint1.repository.menu;

import com.swm.sprint1.dto.UserInfoDto;

import java.util.List;

public interface MenuDtoRepositoryCustom {
    List<UserInfoDto.MenuDto> findMenuResponseDto(Long restaurantId);
}

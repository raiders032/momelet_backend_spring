package com.swm.sprint1.service;

import com.swm.sprint1.domain.Menu;
import com.swm.sprint1.domain.Restaurant;
import com.swm.sprint1.dto.UserInfoDto;
import com.swm.sprint1.exception.ResourceNotFoundException;
import com.swm.sprint1.repository.menu.MenuDtoRepository;
import com.swm.sprint1.repository.menu.MenuRepository;
import com.swm.sprint1.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuDtoRepository menuDtoRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void createMenu(Long restaurantId, com.swm.sprint1.dto.MenuDto menuDto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", restaurantId, "210"));
        Menu menu = new Menu(restaurant, menuDto.getName(), menuDto.getPrice(), false);
        menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenu(Long restaurantId, Long menuId) {
        Menu menu = menuRepository.findByRestaurantIdAndId(restaurantId, menuId);
        menuRepository.delete(menu);
    }

    @Transactional
    public void updateMenu(Long restaurantId, Long menuId, com.swm.sprint1.dto.MenuDto menuDto) {
        Menu menu = menuRepository.findByRestaurantIdAndId(restaurantId, menuId);
        menu.update(menuDto.getName(), menuDto.getPrice());
    }

    public List<UserInfoDto.MenuDto> getMenu(Long restaurantId) {
        return menuDtoRepository.findMenuResponseDto(restaurantId);
    }
}

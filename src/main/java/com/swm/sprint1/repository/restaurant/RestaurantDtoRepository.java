package com.swm.sprint1.repository.restaurant;

import com.swm.sprint1.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RestaurantDtoRepository extends JpaRepository<Restaurant,Long>, RestaurantDtoRepositoryCustom {

}

package com.swm.sprint1.repository.menu;

import com.swm.sprint1.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDtoRepository extends JpaRepository<Menu, Long>, MenuDtoRepositoryCustom {
}

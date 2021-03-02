package com.swm.sprint1.repository.bookmark;

import com.swm.sprint1.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);

    Optional<Bookmark> findByUserIdAndRestaurantId(Long userId, Long restaurantId);
}

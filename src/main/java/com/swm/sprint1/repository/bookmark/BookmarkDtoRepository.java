package com.swm.sprint1.repository.bookmark;

import com.swm.sprint1.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkDtoRepository extends JpaRepository<Bookmark, Long>, BookmarkDtoRepositoryCustom {

}

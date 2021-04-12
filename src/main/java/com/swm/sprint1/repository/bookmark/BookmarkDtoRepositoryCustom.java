package com.swm.sprint1.repository.bookmark;

import com.swm.sprint1.dto.BookmarkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkDtoRepositoryCustom{

    Page<BookmarkDto> findAllByUserIdOrderByLike(Long userId, Pageable pageable);

    Page<BookmarkDto> findAllByUserIdOrderById(Long userId, Pageable pageable);
}

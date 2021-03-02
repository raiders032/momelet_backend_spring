package com.swm.sprint1.repository.bookmark;

import com.swm.sprint1.payload.response.BookmarkResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkDtoRepositoryCustom{

    Page<BookmarkResponseDto> findAllByUserIdOrderByLike(Long userId, Pageable pageable);

    Page<BookmarkResponseDto> findAllByUserIdOrderById(Long userId, Pageable pageable);
}

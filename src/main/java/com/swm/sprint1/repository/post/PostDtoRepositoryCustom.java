package com.swm.sprint1.repository.post;

import com.swm.sprint1.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostDtoRepositoryCustom {
    Page<PostDto> findAllPostResponseDto(Pageable pageable);
}

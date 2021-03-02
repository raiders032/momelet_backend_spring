package com.swm.sprint1.repository.post;

import com.swm.sprint1.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDtoRepository extends JpaRepository<Post, Long>, PostDtoRepositoryCustom {

}

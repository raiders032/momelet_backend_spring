package com.swm.sprint1.repository.post;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swm.sprint1.domain.Post;
import com.swm.sprint1.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.List;

import static com.swm.sprint1.domain.QPost.post;

@RequiredArgsConstructor
public class PostDtoRepositoryImpl implements PostDtoRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostDto> findAllPostResponseDto(Pageable pageable) {

        List<PostDto> content = queryFactory
                .select(Projections.fields(PostDto.class, post.id, post.restaurant.id.as("restaurantId"), post.imageUrl, post.claim))
                .from(post)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory
                .select(post)
                .from(post);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);

    }
}

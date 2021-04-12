package com.swm.sprint1.controller;

import com.swm.sprint1.dto.response.ApiResponse;
import com.swm.sprint1.dto.PostDto;
import com.swm.sprint1.security.CurrentUser;
import com.swm.sprint1.security.UserPrincipal;
import com.swm.sprint1.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.IOException;


@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "식당 정보 수정 요청", notes = "식당 정보 수정을 요청합니다.")
    @PostMapping("/api/v1/posts/restaurants/{restaurantId}")
    public ResponseEntity<?> createPost(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long restaurantId,
                                        @RequestParam(required = false) MultipartFile imageFile,
                                        @RequestParam @NotBlank String claim) throws IOException {
        log.debug("PostMapping /api/v1/posts/restaurants/{restaurantId}");

        postService.createPost(currentUser.getId(), restaurantId, imageFile, claim);

        return ResponseEntity.ok(new ApiResponse(true, "식당 정보 수정 요청 완료"));
    }

    @ApiOperation(value = "식당 정보 수정 요청 조회", notes = "식당 정보 수정 요청을 조회합니다.")
    @GetMapping("/api/v1/posts")
    public ResponseEntity<?> getPost(Pageable pageable) {
        log.debug("GetMapping /api/v1/posts");

        Page<PostDto> posts = postService.getPost(pageable);

        return ResponseEntity.ok(new ApiResponse(true, "식당 정보 수정 요청 조회 완료", "posts", posts));
    }

    @ApiOperation(value = "식당 정보 수정 요청 삭제", notes = "식당 정보 수정 요청을 삭제합니다.")
    @DeleteMapping("/api/v1/posts/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        log.debug("DeleteMapping /api/v1/posts/{postId}");

        postService.deletePost(postId);

        return ResponseEntity.ok(new ApiResponse(true, "식당 정보 수정 요청 삭제 완료"));
    }


}

package com.swm.sprint1.controller;

import com.swm.sprint1.payload.response.ApiResponse;
import com.swm.sprint1.payload.response.BookmarkResponseDto;
import com.swm.sprint1.security.CurrentUser;
import com.swm.sprint1.security.UserPrincipal;
import com.swm.sprint1.service.BookmarkService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final Logger logger = LoggerFactory.getLogger(BookmarkController.class);

    @ApiOperation(value = "북마크 생성", notes = "북마크를 생성합니다.")
    @PostMapping("/api/v1/bookmarks/restaurants/{restaurantId}")
    public ResponseEntity<?> createBookmark(@CurrentUser UserPrincipal currentUser,
                                            @PathVariable Long restaurantId){
        logger.debug("PostMapping /api/v1/bookmarks/restaurants/{restaurantId}");

        bookmarkService.createBookmark(currentUser.getId(), restaurantId);

        return ResponseEntity.ok(new ApiResponse(true, "북마크 생성 완료"));
    }

    @ApiOperation(value = "북마크 조회", notes = "북마크를 조회합니다.")
    @GetMapping("/api/v1/bookmarks")
    public ResponseEntity<?> createBookmark(@CurrentUser UserPrincipal currentUser,
                                            @RequestParam(required = false, defaultValue = "id") String filter,
                                            Pageable pageable){
        logger.debug("GetMapping /api/v1/bookmarks");

        Page<BookmarkResponseDto> bookmarks = bookmarkService.findBookmarkResponseDtoByUserId(currentUser.getId(), filter, pageable);

        return ResponseEntity.ok(new ApiResponse(true, "북마크 조회 완료", "bookmarks", bookmarks));
    }

    @ApiOperation(value = "북마크 삭제", notes = "북마크를 삭제합니다.")
    @DeleteMapping("/api/v1/bookmarks/restaurants/{restaurantId}")
    public ResponseEntity<?> deleteBookmark(@CurrentUser UserPrincipal currentUser,
                                            @PathVariable Long restaurantId){
        logger.debug("DeleteMapping /api/v1/bookmarks/restaurants/{restaurantId}");

        bookmarkService.deleteBookmark(currentUser.getId(), restaurantId);

        return ResponseEntity.ok(new ApiResponse(true, "북마크 삭제 완료"));
    }
}
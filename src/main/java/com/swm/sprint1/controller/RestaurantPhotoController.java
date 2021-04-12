package com.swm.sprint1.controller;

import com.swm.sprint1.payload.response.ApiResponse;
import com.swm.sprint1.payload.response.RestaurantPhotoResponseDto;
import com.swm.sprint1.security.CurrentUser;
import com.swm.sprint1.security.UserPrincipal;
import com.swm.sprint1.service.RestaurantPhotoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@RestController
public class RestaurantPhotoController {

    private final RestaurantPhotoService restaurantPhotoService;

    @ApiOperation(value = "식당 사진 저장", notes = "식당 사진을 저장합니다.")
    @PostMapping("/api/v1/photos/restaurants/{restaurantId}")
    public ResponseEntity<?> createPhoto(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable Long restaurantId,
                                         @RequestParam MultipartFile imageFile) throws IOException {
        log.debug("PostMapping /api/v1/photos/restaurants/{restaurantId}");

        restaurantPhotoService.createPhoto(currentUser.getId(), restaurantId, imageFile);

        return ResponseEntity.ok(new ApiResponse(true, "식당 사진 저장 완료"));
    }

    @ApiOperation(value = "식당 사진 조회", notes = "식당 사진을 조회합니다.")
    @GetMapping("/api/v1/photos/restaurants/{restaurantId}")
    public ResponseEntity<?> getPhoto(@PathVariable Long restaurantId) {
        log.debug("GetMapping /api/v1/photos/restaurants/{restaurantId}");

        List<RestaurantPhotoResponseDto> photos = restaurantPhotoService.findDtoByRestaurantId(restaurantId);

        return ResponseEntity.ok(new ApiResponse(true, "식당 사진 조회 완료", "photos", photos));
    }

    @ApiOperation(value = "식당 사진 삭제", notes = "식당 사진을 삭제합니다.")
    @DeleteMapping("/api/v1/photos/{photoId}")
    public ResponseEntity<?> deletePhoto(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable Long photoId) {
        log.debug("DeleteMapping /api/v1/photos/{photoId}");

        restaurantPhotoService.deletePhoto(currentUser.getId(), photoId);

        return ResponseEntity.ok(new ApiResponse(true, "식당 사진 삭제 완료"));
    }
}

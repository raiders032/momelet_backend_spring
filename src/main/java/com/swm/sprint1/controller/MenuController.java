package com.swm.sprint1.controller;

import com.swm.sprint1.payload.response.ApiResponse;
import com.swm.sprint1.payload.response.MenuDto;
import com.swm.sprint1.payload.response.MenuResponseDto;
import com.swm.sprint1.service.MenuService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@Validated
@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    @ApiOperation(value = "식당 메뉴 추가", notes = "해당 식당의 메뉴를 추가합니다.")
    @PostMapping("/api/v1/restaurants/{restaurantId}/menu")
    public ResponseEntity<?> createMenu(@PathVariable Long restaurantId,
                                        @RequestBody MenuDto newMenu) {
        log.debug("PostMapping /api/v1/restaurants/{restaurantId}/menu");

        menuService.createMenu(restaurantId, newMenu);

        return ResponseEntity.ok(new ApiResponse(true, "식당 메뉴 추가 완료"));
    }

    @ApiOperation(value = "식당 메뉴 조회", notes = "해당 식당의 메뉴를 조회합니다.")
    @GetMapping("/api/v1/restaurants/{restaurantId}/menu")
    public ResponseEntity<?> getMenu(@PathVariable Long restaurantId) {
        log.debug("GetMapping /api/v1/restaurants/{restaurantId}/menu");

        List<MenuResponseDto> menu = menuService.getMenu(restaurantId);

        return ResponseEntity.ok(new ApiResponse(true, "식당 메뉴 조회 완료", "menu", menu));
    }

    @ApiOperation(value = "식당 메뉴 수정", notes = "해당 식당의 메뉴를 수정합니다.")
    @PutMapping("/api/v1/restaurants/{restaurantId}/menu/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable Long restaurantId,
                                        @PathVariable Long menuId,
                                        @RequestBody MenuDto newMenu) {
        log.debug("PutMapping /api/v1/restaurants/{restaurantId}/menu/{menuId}");

        menuService.updateMenu(restaurantId, menuId, newMenu);

        return ResponseEntity.ok(new ApiResponse(true, "식당 메뉴 수정 완료"));
    }

    @ApiOperation(value = "식당 메뉴 삭제", notes = "해당 식당의 메뉴를 삭제합니다.")
    @DeleteMapping("/api/v1/restaurants/{restaurantId}/menu/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long restaurantId,
                                        @PathVariable Long menuId) {
        log.debug("DeleteMapping /api/v1/restaurants/{restaurantId}/menu/{menuId}");

        menuService.deleteMenu(restaurantId, menuId);

        return ResponseEntity.ok(new ApiResponse(true, "식당 메뉴 삭제 완료"));
    }

}

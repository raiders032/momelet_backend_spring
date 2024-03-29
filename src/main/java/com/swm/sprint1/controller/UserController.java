package com.swm.sprint1.controller;

import com.swm.sprint1.domain.User;
import com.swm.sprint1.exception.RequestParamException;
import com.swm.sprint1.dto.request.userLikingReqeust;
import com.swm.sprint1.dto.response.ApiResponse;
import com.swm.sprint1.dto.UserInfoDto;
import com.swm.sprint1.security.CurrentUser;
import com.swm.sprint1.security.UserPrincipal;
import com.swm.sprint1.service.UserLikingService;
import com.swm.sprint1.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@Api(value = "user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserLikingService userLikingService;

    @ApiOperation(value = "유저의 정보를 반환")
    @GetMapping("/api/v1/users/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> retrieveUserInfo(@CurrentUser UserPrincipal currentUser) {
        log.debug("GetMapping /api/v1/users/me");

        User user = currentUser.getUser();
        Map<String, Integer> categories = userService.findAllCategoryNameByUserId(user.getId());

        UserInfoDto userInfoDto = new UserInfoDto(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), categories);

        return ResponseEntity.ok(new ApiResponse(true, "유저의 정보를 반환", "userInfo", userInfoDto));
    }

    @ApiOperation(value = "유저 정보 수정")
    @PostMapping("/api/v1/users/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserInfo(@CurrentUser UserPrincipal currentUser,
                                            @RequestParam(required = false) MultipartFile imageFile,
                                            @RequestParam @NotBlank String name,
                                            @RequestParam @NotEmpty List<String> categories,
                                            @PathVariable Long id) throws IOException {
        log.debug("PostMapping /api/v1/users/{id}");

        if (!id.equals(currentUser.getId())) {
            log.error("jwt token의 유저 아이디와 path param 유저 아이디가 일치하지 않습니다.");
            throw new RequestParamException("jwt token의 유저 아이디와 path param 유저 아이디가 일치하지 않습니다. :" + id, "103");
        }

        userService.updateUser(id, imageFile, name, categories);

        return ResponseEntity.ok(new ApiResponse(true, "회원 정보 수정 완료"));
    }

    @ApiOperation(value = "유저 의사표현 저장")
    @PostMapping("/api/v1/users/{id}/liking")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createUserLiking(@CurrentUser UserPrincipal currentUser,
                                              @PathVariable Long id,
                                              @Valid @RequestBody userLikingReqeust userLikingReqeust,
                                              BindingResult result) {
        log.debug("PostMapping /api/v1/users/{id}/liking");

        if (!id.equals(currentUser.getId())) {
            log.error("jwt token의 유저 아이디와 path param 유저 아이디가 일치하지 않습니다.");
            throw new RequestParamException("jwt token의 유저 아이디와 path param 유저 아이디가 일치하지 않습니다. :" + id, "103");
        }

        if (result.hasErrors()) {
            throw new RequestParamException(result.getAllErrors().toString(), "102");
        }

        List<Long> userLikingId = userLikingService.saveUserLiking(currentUser.getId(), userLikingReqeust);

        return ResponseEntity.ok(new ApiResponse(true, "유저 의사 표현 저장 완료", "userLikingId", userLikingId));
    }

}
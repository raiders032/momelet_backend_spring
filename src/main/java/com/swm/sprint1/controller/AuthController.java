package com.swm.sprint1.controller;

import com.swm.sprint1.exception.RequestParamException;
import com.swm.sprint1.dto.request.JwtRequest;
import com.swm.sprint1.dto.request.LoginRequest;
import com.swm.sprint1.dto.response.ApiResponse;
import com.swm.sprint1.dto.response.AuthResponse;
import com.swm.sprint1.security.Token;
import com.swm.sprint1.security.TokenProvider;
import com.swm.sprint1.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @ApiOperation(value = "액세스 토큰 재발급", notes = "새로 갱신된 액세스 토큰을 발급합니다.")
    @PostMapping("/api/v1/auth/access-token")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody JwtRequest jwtRequest, BindingResult result) {
        log.debug("PostMapping /api/v1/auth/access-token");

        if (result.hasErrors()) {
            log.error("JwtDto 바인딩 에러가 발생했습니다.");
            throw new RequestParamException("JwtDto 바인딩 에러가 발생했습니다.", "102");
        }

        Token refreshedAccessToken = authService.refreshAccessToken(jwtRequest);

        return ResponseEntity.ok(new ApiResponse(true, "액세스 토큰 갱신 완료", "accessToken", refreshedAccessToken));
    }

    @ApiOperation(value = "액세스 토큰 & 리프레시 토큰 재발급", notes = "새로 갱신된 액세스 토큰과 리프레시 토큰을 발급합니다.")
    @PostMapping("/api/v1/auth/refresh-token")
    public ResponseEntity<?> refreshAccessAndRefreshTokens(@Valid @RequestBody JwtRequest jwtRequest, BindingResult result) {
        log.debug("PostMapping /api/v1/auth/refresh-token");

        if (result.hasErrors()) {
            log.error("JwtDto 바인딩 에러가 발생했습니다.");
            throw new RequestParamException("JwtDto 바인딩 에러가 발생했습니다.", "102");
        }

        AuthResponse authResponse = authService.refreshAccessAndRefreshToken(jwtRequest);

        return ResponseEntity.ok(new ApiResponse(true, "액세스 & 리프레시 토큰 갱신 완료", "tokens", authResponse));
    }

    @ApiOperation(value = "액세스 토큰 유효성 검사", notes = "토큰의 유효성을 검사하고 결과를 반환합니다.")
    @PostMapping("/api/v1/auth/validation/access")
    public ResponseEntity<?> validateAccessJwtToken(@Valid @RequestBody JwtRequest jwtRequest, BindingResult result) {
        log.debug("PostMapping /api/v1/auth/validation/access");

        if (result.hasErrors()) {
            log.error("JwtDto 바인딩 에러가 발생했습니다.");
            throw new RequestParamException("JwtDto 바인딩 에러가 발생했습니다.", "102");
        }

        tokenProvider.validateAccessToken(jwtRequest.getJwt());
        return ResponseEntity.ok(new ApiResponse(true, "유효한 액세스 토큰 입니다."));
    }

    @ApiOperation(value = "리프레시 토큰 유효성 검사", notes = "리프레시 토큰의 유효성을 검사하고 결과를 반환합니다.")
    @PostMapping("/api/v1/auth/validation/refresh")
    public ResponseEntity<?> validateRefreshJwtToken(@Valid @RequestBody JwtRequest jwtRequest, BindingResult result) {
        log.debug("PostMapping /api/v1/auth/validation/refresh");

        if (result.hasErrors()) {
            log.error("JwtDto 바인딩 에러가 발생했습니다.");
            throw new RequestParamException("JwtDto 바인딩 에러가 발생했습니다.", "102");
        }

        tokenProvider.validateRefreshToken(jwtRequest.getJwt());
        return ResponseEntity.ok(new ApiResponse(true, "유효한 리프레시 토큰 입니다."));
    }

    @ApiOperation(value = "관리자 로그인", notes = "관리자 로그인 하고 응답으로 액세스 토큰")
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("PostMapping /api/v1/auth/login");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Token accessToken = tokenProvider.createAdminAccessToken(authentication);
        return ResponseEntity.ok(new AuthResponse(accessToken, null));
    }

}

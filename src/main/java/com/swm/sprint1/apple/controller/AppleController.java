package com.swm.sprint1.apple.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.swm.sprint1.apple.model.Payload;
import com.swm.sprint1.apple.model.ServicesResponse;
import com.swm.sprint1.apple.model.TokenResponse;
import com.swm.sprint1.apple.service.AppleService;
import com.swm.sprint1.apple.utils.AppleUtils;
import com.swm.sprint1.config.AppProperties;
import com.swm.sprint1.domain.AuthProvider;
import com.swm.sprint1.domain.Category;
import com.swm.sprint1.domain.User;
import com.swm.sprint1.exception.BadRequestException;
import com.swm.sprint1.dto.response.AuthResponse;
import com.swm.sprint1.repository.category.CategoryRepository;
import com.swm.sprint1.repository.user.UserRepository;
import com.swm.sprint1.security.Token;
import com.swm.sprint1.service.AuthService;
import com.swm.sprint1.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.swm.sprint1.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@RequiredArgsConstructor
@Controller
public class AppleController {

    private Logger logger = LoggerFactory.getLogger(AppleController.class);

    private final UserRepository userRepository;
    private final AppleUtils appleUtils;
    private final CategoryRepository categoryRepository;
    private final AppProperties appProperties;
    private final AuthService authService;
    private final AppleService appleService;

    private static final int cookieExpireSeconds = 180;

    @GetMapping(value = "/apple/login")
    public String appleLogin(RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> metaInfo = appleService.getLoginMetaInfo();

        attributes.addAttribute("client_id", metaInfo.get("CLIENT_ID"));
        attributes.addAttribute("redirect_uri", metaInfo.get("REDIRECT_URI"));
        attributes.addAttribute("nonce", metaInfo.get("NONCE"));
        attributes.addAttribute("response_type", "code id_token");
        attributes.addAttribute("scope", "name email");
        attributes.addAttribute("response_mode", "form_post");

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }

        return "redirect:https://appleid.apple.com/auth/authorize";
    }

    @PostMapping(value = "/redirect")
    @ResponseBody
    public ResponseEntity<?> servicesRedirect(ServicesResponse serviceResponse, HttpServletRequest request, HttpServletResponse response) throws JOSEException, InvalidKeyException, IOException, URISyntaxException {
        logger.debug("servicesRedirect 호출");
        if (serviceResponse == null) {
            return null;
        }

        String code = serviceResponse.getCode();
        String client_secret = appleService.getAppleClientSecret(serviceResponse.getId_token());
        logger.debug("client_secret 생성 완료");

        TokenResponse tokenResponse = appleService.requestCodeValidations(client_secret, code, null);
        logger.debug("code 검증 완료");

        Payload payload = appleUtils.decodeFromIdToken(tokenResponse.getId_token());
        Optional<User> userOptional = userRepository.findByProviderAndProviderId(AuthProvider.apple, payload.getSub());
        User user;
        if(userOptional.isPresent()) {
            logger.debug("이미 존재하는 회원입니다.");
            user = userOptional.get();
        } else {
            logger.debug("신규 회원입니다.");
            user = registerNewUser(AuthProvider.apple, payload.getSub(), "이름을 변경 해주세요", payload.getEmail());
        }

        SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy/MM/dd HH:mm:ss", Locale.KOREA );

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(!redirectUri.isPresent()) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        logger.debug("redirectUri" +redirectUri);
        String targetUrl = redirectUri.orElse(("/"));
        AuthResponse accessAndRefreshToken = authService.createAccessAndRefreshToken(user.getId());
        Token accessToken = accessAndRefreshToken.getAccessToken();
        Token refreshToken = accessAndRefreshToken.getRefreshToken();

        URI uri = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken.getJwtToken())
                .queryParam("accessTokenExpiryDate", formatter.format(accessToken.getExpiryDate()))
                .queryParam("refreshToken", refreshToken.getJwtToken())
                .queryParam("refreshTokenExpiryDate", formatter.format(refreshToken.getExpiryDate()))
                .build().toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    private User registerNewUser(AuthProvider authProvider, String providerId, String name, String email) {
        AuthProvider provider = authProvider;
        String imageUrl = appProperties.getS3().getDefaultImageUri() + appProperties.getS3().getDefaultNumber() +appProperties.getS3().getDefaultExtension();
        List<Category> categories = categoryRepository.findAll();
        User user = new User(name, email, imageUrl, provider, providerId, categories);
        return userRepository.save(user);
    }
}
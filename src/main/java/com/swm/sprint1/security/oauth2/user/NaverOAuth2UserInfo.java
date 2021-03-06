package com.swm.sprint1.security.oauth2.user;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response"));
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        if(attributes.containsKey("email"))
            return (String) attributes.get("email");
        else
            return null;
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("profile_image");
    }
}

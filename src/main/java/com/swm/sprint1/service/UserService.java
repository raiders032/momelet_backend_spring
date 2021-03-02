package com.swm.sprint1.service;

import com.swm.sprint1.domain.*;
import com.swm.sprint1.exception.NotSupportedExtension;
import com.swm.sprint1.exception.ResourceNotFoundException;
import com.swm.sprint1.payload.request.SignUpRequest;
import com.swm.sprint1.payload.request.UpdateUserRequest;
import com.swm.sprint1.repository.category.CategoryRepository;
import com.swm.sprint1.repository.user.UserCategoryRepository;
import com.swm.sprint1.repository.user.UserRepository;
import com.swm.sprint1.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final S3Uploader s3Uploader;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void updateUser(Long id, MultipartFile imageFile, String name, List<String> categoryNames) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id, "200"));
        List<Category> categories = categoryRepository.findCategoryByCategoryName(categoryNames);
        String imageUrl;

        if(imageFile != null)
            imageUrl = s3Uploader.uploadImageFile(imageFile);
        else
            imageUrl = user.getImageUrl();

        user.updateUserInfo(name, imageUrl, categories);
    }

    public Map<String, Integer> findAllCategoryNameByUserId(Long id) {
        logger.debug("findAllCategoryNameByUserId 호출됨");
        return userCategoryRepository.findAllCategoryNameByUserId(id);
    }

}

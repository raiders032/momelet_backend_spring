package com.swm.sprint1.repository.user;

import com.swm.sprint1.domain.Category;
import com.swm.sprint1.domain.CategoryNumber;

import java.util.List;
import java.util.Map;

public interface UserCategoryRepositoryCustom {

    Map<String,Integer> findAllCategoryNameByUserId(Long userId);

}

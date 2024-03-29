package com.swm.sprint1.service;

import com.swm.sprint1.repository.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    @Test
    public void updateUser() {

    }

    @Test
    public void getUserCategoryName() {
    }
}
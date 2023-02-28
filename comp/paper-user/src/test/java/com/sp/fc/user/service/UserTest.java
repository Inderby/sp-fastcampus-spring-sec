package com.sp.fc.user.service;

import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.helper.UserTestHelper;
import com.sp.fc.user.service.helper.WithUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserTest extends WithUserTest {

    @BeforeEach
    protected void before(){
        prepareUserService();
    }

    @DisplayName("1. 사용자 생성")
    @Test
    void 사용자_생성(){
        userTestHelper.createUser(school, "user1");
        List<User> list = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(1, list.size());
        UserTestHelper.assertUser(school, list.get(0), "user1");
    }

    @DisplayName("2. 이름 수정")
    @Test
    void 이름_수정(){
        User user = userTestHelper.createUser(school, "user1");
        userService.updateUsername(user.getUserId(), "user2");
        List<User> list = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertEquals("user2", list.get(0).getName());
    }

    @DisplayName("3. 권한 부여")
    @Test
    void 권한_부여하기(){
        User user = userTestHelper.createUser(school, "user1", Authority.ROLE_STUDENT);
//        System.out.println("테스트2"+user);
        userService.addAuthority(user.getUserId(), Authority.ROLE_TEACHER);
        User savedUser = userService.findUser(user.getUserId()).get();
        UserTestHelper.assertUser(school, savedUser, "user1", Authority.ROLE_STUDENT, Authority.ROLE_TEACHER);
    }

    @DisplayName("4. 권한 취소")
    @Test
    void 권한_취소(){
        User user1 = userTestHelper.createUser(school, "admin", Authority.ROLE_TEACHER, Authority.ROLE_STUDENT);
        userService.removeAuthority(user1.getUserId(), Authority.ROLE_STUDENT);
        User savedUser = userService.findUser(user1.getUserId()).get();
        System.out.println(savedUser);
        UserTestHelper.assertUser(school, savedUser, "admin", Authority.ROLE_TEACHER);
    }

    @DisplayName("5. email검색")
    @Test
    void 이메일_검색기능(){
        User user1 = userTestHelper.createUser(school, "user1");
        User saved = (User)userSecurityService.loadUserByUsername("user1@test.com");
        UserTestHelper.assertUser(school, saved, "user1");
    }
}

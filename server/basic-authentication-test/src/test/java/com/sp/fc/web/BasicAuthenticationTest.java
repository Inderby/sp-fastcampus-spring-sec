package com.sp.fc.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BasicAuthenticationTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    private String greetingUrl(){
        return "http://localhost:" + port +"/greeting";
    }
    @DisplayName("1. 인증실패 ")
    @Test
    void test_1(){
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, ()->{
            client.getForObject(greetingUrl(), String.class);
        });
        assertEquals(401, exception.getRawStatusCode());
    }

    @DisplayName("2. 인증성공 ")

    @Test
    void test_2(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity<String> resp = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);
        System.out.println(resp.getBody());
    }
    @DisplayName("3. 인증성공2")

    @Test
    void test_3(){
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String resp = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", resp);
    }

    @DisplayName("4. POST 인증")

    @Test
    void test_4(){
        TestRestTemplate testClient = new TestRestTemplate("user1","1111");
        ResponseEntity<String> resp = testClient.getForEntity(greetingUrl() , String.class, "jongwon");
        assertEquals("hello jongwon", resp.getBody()); //csrf filter가 작동중인 상태에서는 post요청을 처리할 수 없다.

    }
}

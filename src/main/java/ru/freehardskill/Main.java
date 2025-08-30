package ru.freehardskill;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.freehardskill.model.User;


import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        String code = "";

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://94.198.50.185:7081/api/users";
        ResponseEntity<User[]> getEntity = restTemplate.getForEntity(url,User[].class);
        String[] cookies = Objects.requireNonNull(getEntity.getHeaders().get("Set-Cookie")).get(0).split(";");
        String sessionID = cookies[0];

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE,sessionID);

        User user = new User(3L,"James","Brown",(byte)25);
        HttpEntity<User> httpEntity = new HttpEntity<>(user,headers);
        ResponseEntity<String> postEntity = restTemplate.postForEntity(url,httpEntity,String.class);
        code += postEntity.getBody();

        user.setName("Thomas");
        user.setLastName("Shelby");
        ResponseEntity<String> putEntity = restTemplate.exchange(url, HttpMethod.PUT,httpEntity,String.class);
        code += putEntity.getBody();

        HttpEntity<User> httpDelete = new HttpEntity<>(headers);
        ResponseEntity<String> deleteEntity = restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, httpDelete,String.class,user.getId());
        code += deleteEntity.getBody();
        System.out.println(code);
    }
}

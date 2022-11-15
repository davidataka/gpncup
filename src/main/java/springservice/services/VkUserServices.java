package springservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import springservice.dto.VkUserRequest;
import springservice.dto.VkUserResponse;
import springservice.tools.VkException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

@Service
public class VkUserServices {

    public void checkVkExceptions(HttpResponse<String> response) throws VkException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> exceptionCheck = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        if (exceptionCheck.get("error") != null) {
            Map<String, Map<String, Object>> exception = objectMapper.readValue(response.body(), new TypeReference<>() {
            });
            throw new VkException("error_code: " + exception.get("error").get("error_code").toString() +
                    " message: " + exception.get("error").get("error_msg").toString());
        }
    }

    public VkUserResponse getUserAndGroupStatus(String vk_service_token, VkUserRequest user) throws VkException, URISyntaxException, IOException, InterruptedException {
        Map<String, String> userData = getUser(vk_service_token, user);
        boolean isMember = isMember(vk_service_token, user);
        return new VkUserResponse(userData.get("last_name"), userData.get("first_name"),
                userData.get("nickname"), isMember);
    }

    public Map<String, String> getUser(String vk_service_token, VkUserRequest user) throws URISyntaxException, IOException, InterruptedException, VkException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("https://api.vk.com/method/" +
                        "users.get?user_id=%s&fields=nickname&access_token=%s&v=5.131", user.getUser_id(), vk_service_token))).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        checkVkExceptions(response);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, ArrayList<Map<String, String>>> responseFinal = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        if (responseFinal.get("response").isEmpty()) {
            throw new VkException("There is no user with this id");
        }
        if (responseFinal.get("response").get(0).get("id") != null)
            user.setUser_id(responseFinal.get("response").get(0).get("id"));
        return responseFinal.get("response").get(0);
    }

    public boolean isMember(String vk_service_token, VkUserRequest user) throws URISyntaxException, IOException, InterruptedException, VkException {
        checkGroup(vk_service_token, user.getGroup_id());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("https://api.vk.com/method/groups.isMember" +
                                "?user_id=%s&group_id=%s&v=5.131&access_token=%s"
                        , user.getUser_id(), user.getGroup_id(), vk_service_token))).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        checkVkExceptions(response);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Boolean> responseFinal = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        return responseFinal.get("response");

    }

    public void checkGroup(String vk_service_token, String group_id) throws URISyntaxException, IOException, InterruptedException, VkException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("https://api.vk.com/method/groups.getById" +
                                "?group_id=%s&v=5.131&access_token=%s"
                        , group_id, vk_service_token))).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        checkVkExceptions(response);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, ArrayList<Map<String, String>>> responseFinal = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        if (responseFinal.get("response").isEmpty()) {
            throw new VkException("There is no group with this id");
        }
    }

}

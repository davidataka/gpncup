package springservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springservice.dto.VkUserRequest;
import springservice.dto.VkUserResponse;
import springservice.services.VkUserServices;
import springservice.tools.VkException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URISyntaxException;

@Validated
@RestController
@RequestMapping("/user")
public class VkUserController {

    @Autowired
    private VkUserServices userService;

    @PostMapping("/getUserAndGroupStatus")
    @ResponseBody
    public VkUserResponse getUserAndGroupStatus(@RequestHeader("vk_service_token") @NotBlank String vk_service_token, @Valid @RequestBody VkUserRequest user) throws VkException, URISyntaxException, IOException, InterruptedException {
        return userService.getUserAndGroupStatus(vk_service_token, user);
    }

    @ExceptionHandler({VkException.class, ConstraintViolationException.class})
    public String handleException(Exception e) {
        return e.getMessage();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleException(MethodArgumentNotValidException e) {
        FieldError validationError = e.getBindingResult().getFieldErrors().stream().findFirst().get();
        return validationError.getField() + " " + validationError.getDefaultMessage();
    }
}

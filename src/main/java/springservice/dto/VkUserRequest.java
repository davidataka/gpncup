package springservice.dto;
import javax.validation.constraints.NotBlank;

public class VkUserRequest {

    @NotBlank
    private String user_id;
    @NotBlank
    private String group_id;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getGroup_id() {
        return group_id;
    }
}

package springservice.dto;

public class VkUserResponse {
    private String last_name;
    private String first_name;
    private String middle_name;
    private boolean member;

    public VkUserResponse(String last_name, String first_name, String middle_name, boolean member) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.member = member;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public boolean isMember() {
        return member;
    }
}

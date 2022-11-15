package springservice.tools;

public class VkException extends Exception {

    private String message;

    public VkException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

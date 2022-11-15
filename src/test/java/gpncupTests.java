import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import springservice.dto.VkUserRequest;
import springservice.dto.VkUserResponse;
import springservice.services.VkUserServices;
import springservice.tools.VkException;


public class gpncupTests {
    private final VkUserServices userServices = new VkUserServices();

    @Test
    public void GetUserCorrectly() throws Exception {
        VkUserRequest PavelDurov = new VkUserRequest();
        PavelDurov.setUser_id("1");
        PavelDurov.setGroup_id("1");
        VkUserResponse response = userServices.getUserAndGroupStatus(
                "4471baf24471baf24471baf2af4760cf62444714471baf227002129c9085aee5383317b",
                PavelDurov);
        Assert.assertEquals(response.getFirst_name(), "Pavel");
        Assert.assertEquals(response.getLast_name(), "Durov");
        Assert.assertEquals(response.getMiddle_name(), "");
        Assert.assertFalse(response.isMember());
    }

    @Test()
    public void IncorrectServiceToken() throws Exception {
        try {
            VkUserRequest RandonUser = new VkUserRequest();
            RandonUser.setUser_id("399");
            RandonUser.setGroup_id("111");
            VkUserResponse response = userServices.getUserAndGroupStatus(
                    "4471baf24471baf24471",
                    RandonUser);
            Assert.fail("No VkException");
        } catch (VkException e) {
            Assert.assertEquals(e.getMessage(), "error_code: 5 message: User authorization failed: invalid access_token (4).");
        }
    }

    @Test()
    public void IncorrectUserId() throws Exception {
        try {
            VkUserRequest RandonUser = new VkUserRequest();
            RandonUser.setUser_id("299999999999");
            RandonUser.setGroup_id("111");
            VkUserResponse response = userServices.getUserAndGroupStatus(
                    "4471baf24471baf24471baf2af4760cf62444714471baf227002129c9085aee5383317b",
                    RandonUser);
            Assert.fail("No VkException");
        } catch (VkException e) {
            Assert.assertEquals(e.getMessage(), "There is no user with this id");
        }
    }
}
package se.flyingpenguins.greeting;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import se.flyingpenguins.greeting.controller.GreetingController;
import se.flyingpenguins.greeting.service.GreetingControllerService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({GreetingController.class, GreetingControllerService.class})
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /* null or invalid userAccount */
    @Test
    public void testUserAccountNullShouldReturn400() throws Exception {
        String reason = "Required String parameter 'account' is not present";
        testGreetingBadRequestError(null, null, null, reason);
        testGreetingBadRequestError(null, "big", "123", reason);
    }

    @Test
    public void testNotValidUserAccountShouldReturn400() throws Exception {
        String reason = "\'account\' can be \'personal\' or \'business\'!";
        testGreetingBadRequestError("wrong", null, null, reason);
        testGreetingBadRequestError("wrong", "big", "123", reason);
    }

    /* PERSONAL userAccount */
    @Test
    public void testPersonalUserAccountWithValidIdShouldReturn200() throws Exception {
        String msg = "Hi, userId 123";
        getPerform("personal", null, "123").andExpect(status().isOk())
                .andExpect(content().string(msg));
        getPerform("personal", "ignore", "123").andExpect(status().isOk())
                .andExpect(content().string(msg));
    }

    @Test
    public void testPersonalUserAccountWithInValidIdShouldReturn400() throws Exception {
        String reason = "Personal account must add the \'id\' to the request, which is a positive integer.";
        // id = null
        testGreetingBadRequestError("personal", null, null, reason);
        testGreetingBadRequestError("personal", "ignore", null, reason);
        // id = "wrong"
        testGreetingBadRequestError("personal", null, "wrong", reason);
        testGreetingBadRequestError("personal", "ignore", "wrong", reason);
        // id = "0"
        testGreetingBadRequestError("personal", null, "0", reason);
        testGreetingBadRequestError("personal", "ignore", "0", reason);
        // id = "-1"
        testGreetingBadRequestError("personal", null, "-1", reason);
        testGreetingBadRequestError("personal", "ignore", "-1", reason);
    }

    /* BUSINESS userAccount */
    @Test
    public void testBigBusinessUserAccountShouldReturn200() throws Exception {
        String msg = "Welcome, business user!";
        getPerform("business", "big", "-123").andExpect(status().isOk())
                .andExpect(content().string(msg));
        getPerform("business", "big", null).andExpect(status().isOk())
                .andExpect(content().string(msg));
    }

    @Test
    public void testSmallBusinessUserAccountShouldReturn200() throws Exception {
        String reason = "The path is not yet implemented.";
        getPerform("business", "small", "-123").andExpect(status().isNotImplemented())
                .andExpect(status().reason(reason));
        getPerform("business", "small", null).andExpect(status().isNotImplemented())
                .andExpect(status().reason(reason));
    }

    @Test
    public void testBusinessUserAccountWithInValidTypeShouldReturn400() throws Exception {
        String reason = "Business account must add the \'type\' to the request," +
                                " which can be either \'small\' or \'big\'.";
        // type = null
        testGreetingBadRequestError("business", null, null, reason);
        testGreetingBadRequestError("business", null, "123", reason);
        // type = "wrong"
        testGreetingBadRequestError("business", "wrong", null, reason);
        testGreetingBadRequestError("business", "wrong", "123", reason);
    }

    private void testGreetingBadRequestError(String userAccount, String type, String id, String reason) throws Exception {
        getPerform(userAccount, type, id).andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString(reason)));
    }

    private ResultActions getPerform(String userAccount, String type, String id) throws Exception {
        return mockMvc.perform(get("/greeting")
                                       .param("account", userAccount)
                                       .param("type", type)
                                       .param("id", id));
    }
}
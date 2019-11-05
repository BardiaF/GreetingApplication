package se.flyingpenguins.greeting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import se.flyingpenguins.greeting.service.BusinessAccountType;
import se.flyingpenguins.greeting.service.GreetingControllerService;

@RestController
@Validated
public class GreetingController {

    @Autowired
    private GreetingControllerService controllerService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "account") String userAccount,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) String id) {
        //used switch here to make code more readable and make it easy to adjust the code in case of adding other accounts.

        switch (controllerService.getUserAccountOrThrow(userAccount)) {
            case PERSONAL: //businessAccountType will be ignored.
                return "Hi, userId " + controllerService.getPositiveIntegerOrThrow(id);

            case BUSINESS://id will be ignored.
                BusinessAccountType businessAccountType = controllerService.getBusinessAccountTypeOrThrow(type);
                switch (businessAccountType) {
                    case BIG:
                        return "Welcome, business user!";
                    case SMALL:
                        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                                "The path is not yet implemented.");
                }
        }
        throw controllerService.generateBadRequestException("\'account\' can be \'personal\' or \'business\'!");
    }
}

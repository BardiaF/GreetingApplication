package se.flyingpenguins.greeting.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GreetingControllerService {

    public UserAccount getUserAccountOrThrow(String userAccount) {
        if (StringUtils.isEmpty(userAccount)) {
            throw generateBadRequestException("\'account\' cannot be empty!");
        }
        try {
            return UserAccount.valueOf(userAccount.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw generateBadRequestException("\'account\' can be \'personal\' or \'business\'!");
        }
    }

    public int getPositiveIntegerOrThrow(String sid) {
        try {
            int id = Integer.parseInt(sid);
            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException ignore) {
        }
        throw generateBadRequestException("Personal account must add the \'id\' to the request," +
                                                  " which is a positive integer.");
    }

    public BusinessAccountType getBusinessAccountTypeOrThrow(String type) {
        try {
            return BusinessAccountType.valueOf(type.toUpperCase());
        } catch (Exception ignore) {
        }
        throw generateBadRequestException("Business account must add the \'type\' to the request," +
                                                  " which can be either \'small\' or \'big\'.");
    }

    public ResponseStatusException generateBadRequestException(String reason) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
    }
}

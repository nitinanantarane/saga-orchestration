package com.example.UserService.query.api.controller;

import com.example.CommonService.model.User;
import com.example.CommonService.queries.GetUserPaymentDetailsQuery;
import com.example.UserService.query.api.model.GetSampleQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserQueryController {

    private QueryGateway queryGateway;

    public UserQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/sample")
    public String getSample() {
        GetSampleQuery getSampleQuery = new GetSampleQuery();
        String message = queryGateway.query(getSampleQuery, ResponseTypes.instanceOf(String.class)).join();
        return message;
    }

    @GetMapping("/hello")
    public User getUser() {
        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery =
                new GetUserPaymentDetailsQuery();
        User user = queryGateway.query(getUserPaymentDetailsQuery,
                        ResponseTypes.instanceOf(User.class))
                .join();

        return user;
    }
}

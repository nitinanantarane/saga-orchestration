package com.example.UserService.query.api.projection;

import com.example.CommonService.model.CardDetails;
import com.example.CommonService.model.User;
import com.example.CommonService.queries.GetUserPaymentDetailsQuery;
import com.example.UserService.query.api.model.GetSampleQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {

    public UserProjection() {
    }

    @QueryHandler
    public String getSample(GetSampleQuery getSampleQuery) {
        return "Hello world";
    }

    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery getUserPaymentDetailsQuery) {
        System.out.println("############# called getUserPaymentDetailsQuery #############");
        CardDetails cardDetails = CardDetails.builder()
                .name("Nitin Rane")
                .number("123456789")
                .validTillMonth(01)
                .validTillYear(2022)
                .cvv(111)
                .build();

        return User.builder()
                .firstName("Nitin")
                .lastName("Rane")
                .cardDetails(cardDetails)
                .build();
    }
}

package com.example.CommonService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String firstName;
    private String lastName;
    private CardDetails cardDetails;
}

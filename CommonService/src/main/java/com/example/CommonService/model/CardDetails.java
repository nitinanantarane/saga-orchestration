package com.example.CommonService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetails {
    private String name;
    private String number;
    private Integer validTillMonth;
    private Integer validTillYear;
    private Integer cvv;
}

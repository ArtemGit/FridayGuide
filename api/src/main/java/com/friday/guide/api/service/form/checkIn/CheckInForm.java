package com.friday.guide.api.service.form.checkIn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInForm {
    private String ip;
    private String hostname;
    private String city;
    private String region;
    private String country;
    private String longitude;
    private String latitude;
}

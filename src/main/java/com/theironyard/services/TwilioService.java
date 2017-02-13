package com.theironyard.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;


/**
 * Created by sparatan117 on 2/9/17.
 */
@Service
public class TwilioService {
    @Value("${twilio.sid}")
    private String accountSid;
    @Value("${twilio.token}")
    private String authToken;

    @Value("${twilio.phone}")
    private String formPhone;

    @PostConstruct
    public void init(){
        Twilio.init(accountSid, authToken);
    }

    public String sendSMS(String phone){
        Message message = Message.creator(
                new PhoneNumber(phone),  // To number
                new PhoneNumber(formPhone),  // From number
                "Hello form WePlay, we have found a match for you please come back to our site to see your match"                    // SMS body
        ).create();

       return message.getSid();
    }



}

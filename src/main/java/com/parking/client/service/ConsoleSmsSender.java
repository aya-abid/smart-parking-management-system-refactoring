package com.parking.client.service;

/** No SMS gateway is configured, so the message is printed in the console of the desktop app. */
public class ConsoleSmsSender implements SmsSender {

    @Override
    public void send(String mobile, String message) {
        System.out.println("SMS to " + mobile + ":\n" + message);
    }
}

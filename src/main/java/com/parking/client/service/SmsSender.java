package com.parking.client.service;

/**
 * Sends the login details to a new employee by SMS. No SMS gateway is configured yet, so the app uses
 * {@link ConsoleSmsSender}; a real gateway can be added as another implementation.
 */
public interface SmsSender {

    void send(String mobile, String message);
}

package com.parking.server.handler;

import java.util.Properties;

/**
 * Strategy: one way of answering a request. {@link com.parking.server.ParkingServer} keeps one
 * handler per URL path and picks it from a map, instead of a long if / else if on the URL.
 */
@FunctionalInterface
public interface RequestHandler {

    String handle(Properties params, Properties files) throws Exception;
}

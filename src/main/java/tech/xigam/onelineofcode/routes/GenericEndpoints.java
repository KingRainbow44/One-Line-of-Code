package tech.xigam.onelineofcode.routes;

import tech.xigam.express.Request;

public final class GenericEndpoints {
    public static void indexEndpoint(Request request) {
        request.code(200).respond("Welcome to the One Line Of Code API.");
    }
    
    public static void notFoundEndpoint(Request request) {
        request.code(404).respond("Unable to find requested resource on our servers.");
    }
}

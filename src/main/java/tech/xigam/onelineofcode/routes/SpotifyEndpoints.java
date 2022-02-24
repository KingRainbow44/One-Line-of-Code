package tech.xigam.onelineofcode.routes;

import tech.xigam.express.Request;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.spotify.SpotifyInstance;

public final class SpotifyEndpoints {
    /**
     * /spotify/callback/
     */
    public static void callbackEndpoint(Request request) {
        if (OneLineOfCode.spotifyInstance != null) {
            request.code(302).respond("Authorization has already been completed.");
            return;
        }
        var code = request.requestArguments.getOrDefault("code", "");
        if (code.isEmpty()) {
            request.respond("No code provided.");
            return;
        }

        OneLineOfCode.spotifyInstance = new SpotifyInstance(code);
        request.respond("Authentication successful!");
    }
}

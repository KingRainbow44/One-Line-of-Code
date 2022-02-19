package tech.xigam.onelineofcode.utils.spotify;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import javax.annotation.Nullable;

public final class SpotifyInstance {
    private final SpotifyApi spotifyApi;
    private final String authorizationCode;

    public SpotifyInstance(String authorizationCode) {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(Constants.SPOTIFY_CLIENT_ID)
                .setClientSecret(Constants.SPOTIFY_SECRET)
                .setRedirectUri(SpotifyHttpManager.makeUri(Constants.SPOTIFY_CALLBACK_URI))
                .build();
        this.authorizationCode = authorizationCode;
        this.startRefresh();
    }

    private void startRefresh() {
        try {
            var auth = this.spotifyApi.authorizationCode(this.authorizationCode).build().execute();
            this.spotifyApi.setAccessToken(auth.getAccessToken());
            this.spotifyApi.setRefreshToken(auth.getRefreshToken());
            new SpotifyRefreshTask(this.spotifyApi).start();
        } catch (Exception exception) {
            OneLineOfCode.logger.warn("Failed to start refresh", exception);
        }
    }

    @Nullable
    public CurrentlyPlaying getPlayingTrack() {
        try {
            return spotifyApi.getUsersCurrentlyPlayingTrack().build().execute();
        } catch (Exception exception) {
            OneLineOfCode.logger.warn("Failed to get currently playing track", exception);
            return null;
        }
    }
}

package tech.xigam.onelineofcode.utils.spotify;

import se.michaelthelin.spotify.SpotifyApi;
import tech.xigam.onelineofcode.OneLineOfCode;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public final class SpotifyRefreshTask extends Thread {
    private final SpotifyApi spotifyApi;

    public SpotifyRefreshTask(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    @Override
    public void run() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    var refresh = spotifyApi.authorizationCodeRefresh().build().execute();
                    spotifyApi.setAccessToken(refresh.getAccessToken());
                } catch (Exception exception) {
                    OneLineOfCode.logger.error("Error refreshing Spotify access token", exception);
                }
            }
        }, 0L, TimeUnit.MINUTES.toMillis(30));
    }
}

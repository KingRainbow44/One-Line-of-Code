/*
 * Copyright © 2022 Ben Petrillo. All rights reserved.
 *
 * Project licensed under the MIT License: https://www.mit.edu/~amini/LICENSE.md
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * All portions of this software are available for public use, provided that
 * credit is given to the original author(s).
 */

package tech.xigam.onelineofcode.utils;

import com.google.gson.JsonArray;
import okhttp3.*;
import com.google.gson.Gson;
import tech.xigam.onelineofcode.OneLineOfCode;
import org.apache.hc.core5.http.ParseException;
import tech.xigam.elixirapi.objects.TrackObject;
import tech.xigam.onelineofcode.objects.Asset;
import tech.xigam.onelineofcode.objects.YTVideoData;
import tech.xigam.onelineofcode.utils.absolute.Constants;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class TrackUtil {
    private static final List<String> existingCache = new ArrayList<>();
    
    /**
     * Returns a URL of a track/video's cover art/thumbnail.
     * @param track The AudioTrack to fetch.
     * @return String
     */
    
    public static String getCoverArt(TrackObject track) {
        var trackUri = track.uri;
        if (trackUri.contains("spotify") && trackUri.contains("/track/")) {
            try {
                String[] firstSplit = trackUri.split("/");
                String[] secondSplit; String id;
                if (firstSplit.length > 5) {
                    secondSplit = firstSplit[6].split("\\?");
                } else {
                    secondSplit = firstSplit[4].split("\\?");
                }
                id = secondSplit[0];
                GetTrackRequest trackRequest = OneLineOfCode.spotifyInstance.getHandle().getTrack(id).build();
                Track spotifyTrack = trackRequest.execute();
                Image thumbnail = spotifyTrack.getAlbum().getImages()[0];
                return thumbnail.getUrl();
            } catch (SpotifyWebApiException | IOException | ParseException | NullPointerException exception) {
                exception.printStackTrace();
                return null;
            }
        } else if (trackUri.contains("www.youtube.com") || trackUri.contains("youtu.be")) {
            final YTVideoData data = getVideoData(extractVideoId(track.uri));
            return data != null ? data.items.get(0).snippet.thumbnails.get("maxres").get("url") : null;
        }
        return null;
    }
    
    public static String extractTrackId(String url) {
        return switch(TrackUtil.determineTrackType(url)) {
            default -> url;
            case YOUTUBE -> TrackUtil.extractVideoId(url);
            case SPOTIFY -> TrackUtil.extractSongId(url);
        };
    }

    public static String formatDuration(long ms) {
        final Duration duration = Duration.ofMillis(ms);
        final int hours = duration.toHoursPart();
        final int minutes = duration.toMinutesPart();
        final int seconds = duration.toSecondsPart();
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String extractVideoId(String url) {
        String[] segments = url.split("/");
        return url.contains("youtu.be") ? segments[3] : segments[3].split("v=")[1];
    }

    /**
     * Extracts the song ID from a given URL.
     * @param url The Spotify URL to extract the song ID from.
     * @return A song ID.
     */

    public static String extractSongId(String url) {
        String[] segments = url.split("/");
        return segments[4].split("\\?")[0];
    }

    @Nullable
    public static YTVideoData getVideoData(String videoId) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://www.googleapis.com/youtube/v3/videos?key=" +
                Constants.YOUTUBE_API_KEY +
                "&part=snippet%2CcontentDetails&id=" +
                videoId;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            var data = new Gson().fromJson(response.body().string(), YTVideoData.class);
            response.close(); return data;
        } catch (IOException ex) {
            ex.printStackTrace(); return null;
        }
    }
    
    public static boolean isCached(TrackObject track) {
        if(existingCache.isEmpty()) {
            var cache = JsonUtil.jsonFileDeserialize(Constants.CACHE_FILE, JsonArray.class);
            cache.forEach(element -> existingCache.add(element.getAsString()));
        }
        
        return existingCache.contains(TrackUtil.extractTrackId(track.uri));
    }
    
    public static void pushTrackToDiscord(TrackObject track) {
        var coverArt = TrackUtil.getCoverArt(track);
        var asset = new Asset(); asset.type = "1";
        asset.image = EncodingUtil.base64EncodeImage(coverArt);
        asset.name = TrackUtil.extractTrackId(track.uri);
        
        var body = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(asset));
        
        var httpClient = new OkHttpClient();
        var url = "https://discord.com/api/v9/oauth2/applications/" +
                Constants.PRESENCE_CLIENT_ID + "/assets";
        var request = new Request.Builder()
                .url(url).method("POST", body)
                .addHeader("authorization", Constants.USER_AUTHORIZATION)
                .build();
        try {
            var response = httpClient.newCall(request).execute();
            if(response.code() != 201) {
                assert response.body() != null;
                OneLineOfCode.logger.warn("Failed to push track to discord: " + response.code());
                OneLineOfCode.logger.warn("Response body: " + response.body().string());
            } else OneLineOfCode.logger.info("Successfully pushed " + track.title + " to Discord.");
            response.close();
            
            // Cache asset in database.
            var cache = JsonUtil.jsonFileDeserialize(Constants.CACHE_FILE, JsonArray.class);
            cache.add(asset.name); FileUtil.writeToFile(Constants.CACHE_FILE, JsonUtil.jsonFileSerialize(cache));
            existingCache.add(asset.name);
        } catch (Exception exception) {
            OneLineOfCode.logger.warn("Unable to push asset to Discord's API.", exception);
        }
    }

    /**
     * Determines the source for a given URL.
     * @param url The URL to find the source of.
     * @return A {@link TrackType} representing the source of the URL.
     */

    public static TrackType determineTrackType(String url) {
        if (url.contains("youtu")) return TrackType.YOUTUBE;
        if (url.contains("spotify")) return TrackType.SPOTIFY;
        if (url.contains("file")) return TrackType.CUSTOM;
        return TrackType.UNKNOWN;
    }

    public enum TrackType {
        YOUTUBE,
        SPOTIFY,
        CUSTOM,
        UNKNOWN
    }
}

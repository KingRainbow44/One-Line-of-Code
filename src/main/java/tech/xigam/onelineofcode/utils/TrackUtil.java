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

import okhttp3.*;
import com.google.gson.Gson;
import tech.xigam.onelineofcode.OneLineOfCode;
import org.apache.hc.core5.http.ParseException;
import tech.xigam.elixirapi.objects.TrackObject;
import tech.xigam.onelineofcode.objects.YTVideoData;
import tech.xigam.onelineofcode.utils.absolute.Constants;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Duration;

public final class TrackUtil {

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
            return data != null ? data.items.get(0).snippet.thumbnails.get("default").get("url") : null;
        }
        return null;
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
            return new Gson().fromJson(response.body().string(), YTVideoData.class);
        } catch (IOException ex) {
            ex.printStackTrace(); return null;
        }
    }
}

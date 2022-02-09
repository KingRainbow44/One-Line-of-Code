package tech.xigam.onelineofcode.utils;

import net.dv8tion.jda.api.entities.Guild;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import java.io.IOException;

public final class HttpUtil {
    public static void playTrackOnElixir(Guild guild, String track) {
        var httpClient = new OkHttpClient();
        var encodedTrack = EncodingUtil.base64Encode(track);
        
        var url = "https://app.ponjo.club/v1/elixir/play?guild=" + guild.getId() + "&track=" + encodedTrack;
        var request = new Request.Builder().url(url)
                .addHeader("Authorization", "Bearer " + Constants.PONJO_API_KEY)
                .build();
        
        try {
            httpClient.newCall(request).execute();
        } catch (IOException ignored) { }
    }
}

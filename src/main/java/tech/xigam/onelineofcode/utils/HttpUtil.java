package tech.xigam.onelineofcode.utils;

import net.dv8tion.jda.api.entities.Guild;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class HttpUtil {
    public static int playTrackOnElixir(Guild guild, String track) {
        var httpClient = new OkHttpClient();
        var encodedTrack = URLEncoder.encode(track, StandardCharsets.UTF_8);
        
        var url = "https://app.ponjo.club/v1/elixir/play?guild=" + guild.getId() + "&query=" + encodedTrack;
        var request = new Request.Builder().url(url)
                .method("POST", RequestBody.create(null, new byte[0]))
                .addHeader("Authorization", Constants.PONJO_API_KEY)
                .build();
        
        try {
            var response = httpClient.newCall(request).execute();
            assert response.body() != null;
            
            if(response.code() != 200) {
                OneLineOfCode.logger.warn("Failed to play track on Elixir: " + response.body().string());
            } return response.code();
        } catch (IOException exception) {
            OneLineOfCode.logger.warn("Failed to play track on Elixir", exception);
        } return -1;
    }
}

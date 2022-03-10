package tech.xigam.onelineofcode.utils;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tech.xigam.elixirapi.Bot;
import tech.xigam.elixirapi.exceptions.RequestBuildException;
import tech.xigam.elixirapi.requests.player.PauseRequest;
import tech.xigam.elixirapi.requests.player.PlayRequest;
import tech.xigam.elixirapi.requests.player.ResumeRequest;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class MusicUtil {
    @Deprecated
    public static int playTrackOnElixir(Guild guild, AudioChannel channel, String track) {
        var httpClient = new OkHttpClient();
        var encodedTrack = URLEncoder.encode(track, StandardCharsets.UTF_8);

        var url = "https://app.ponjo.club/v1/elixir/play?guild=" + guild.getId() + "&query=" + encodedTrack + "&channel=" + channel.getId() + "&bot=" + Bot.ELIXIR_MUSIC.getBotId();
        var request = new Request.Builder().url(url)
                .method("POST", RequestBody.create(null, new byte[0]))
                .addHeader("Authorization", Constants.PONJO_API_KEY)
                .build();

        try {
            var response = httpClient.newCall(request).execute();
            assert response.body() != null;

            int code;
            if ((code = response.code()) != 200) {
                OneLineOfCode.logger.warn("Failed to play track on Elixir: " + response.body().string());
            } response.close();
            return code;
        } catch (IOException exception) {
            OneLineOfCode.logger.warn("Failed to play track on Elixir", exception);
        }
        
        return -1;
    }
    
    public static void joinChannelFromBot(Guild guild, AudioChannel channel, Bot bot) {
        var httpClient = new OkHttpClient();

        var url = "https://app.ponjo.club/v1/elixir/join?guild=" + guild.getId() + "&channel=" + channel.getId() + "&bot=" + bot.getBotId();
        var request = new Request.Builder().url(url)
                .method("POST", RequestBody.create(null, new byte[0]))
                .addHeader("Authorization", Constants.PONJO_API_KEY)
                .build();

        try {
            var response = httpClient.newCall(request).execute();
            response.close();
        } catch (IOException ignored) { }
    }
    
    public static void pausePlayerOnBot(Guild guild, Bot bot) {
        try {
            var request = new PauseRequest.Builder(OneLineOfCode.elixirApi)
                    .guild(guild.getId()).bot(bot).build();
            request.execute(response -> {});
        } catch (RequestBuildException ignored) { }
    }

    public static void resumePlayerOnBot(Guild guild, Bot bot) {
        try {
            var request = new ResumeRequest.Builder(OneLineOfCode.elixirApi)
                    .guild(guild.getId()).bot(bot).build();
            request.execute(response -> {});
        } catch (RequestBuildException ignored) { }
    }
    
    public static void playTrackOnBot(Guild guild, AudioChannel channel, String track, Bot bot) {
        try {
            var request = new PlayRequest.Builder(OneLineOfCode.elixirApi)
                    .track(track).channel(channel.getId())
                    .guild(guild.getId()).bot(bot)
                    .build();
            request.execute(response -> {});
        } catch (RequestBuildException ignored) { }
    }
}

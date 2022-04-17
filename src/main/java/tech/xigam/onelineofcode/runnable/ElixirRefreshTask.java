package tech.xigam.onelineofcode.runnable;

import club.bottomservices.discordrpc.lib.RichPresence;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import tech.xigam.elixirapi.Bot;
import tech.xigam.onelineofcode.utils.HttpUtil;
import tech.xigam.onelineofcode.utils.MusicUtil;
import tech.xigam.onelineofcode.utils.TrackUtil;
import tech.xigam.onelineofcode.utils.absolute.RPCClient;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.TimerTask;

public final class ElixirRefreshTask extends TimerTask {
    public Guild guild;
    public AudioChannel channel;
    public Bot bot;
    
    @Override 
    public void run() {
        MusicUtil.getPlayingFromBot(track -> {
            if(track == null) {
                RPCClient.client.sendPresence(RPCClient.presence.build());
                return;
            }

            if(!TrackUtil.isCoverArtCached(track)) {
                try {
                    TrackUtil.pushTrackToDiscord(track);
                } catch (Exception ignored) { }
            }
            
            var joinUrl = "https://discord.com/channels/" + guild.getId() + "/" + channel.getId();

            var endsIn = Duration.ofMillis(track.length - track.position).toSeconds();
            var presence = new RichPresence.Builder()
                    .setTimestamps(null, OffsetDateTime.now().plusSeconds(endsIn).toEpochSecond())
                    .setText("Listening to " + track.title, "by " + track.author)
                    .setAssets(TrackUtil.extractTrackId(track.uri).toLowerCase(), track.title,
                            "kazuha", "Playing in: " + guild.getName())
                    .addButton("Play Song", HttpUtil.shortenUrl(track.uri))
                    .addButton("Listen Along", HttpUtil.shortenUrl(joinUrl));
            RPCClient.client.sendPresence(presence.build());
        }, this.guild, this.bot);
    }
}

package tech.xigam.onelineofcode.runnable;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import tech.xigam.elixirapi.Bot;
import tech.xigam.onelineofcode.objects.PresenceDetails;
import tech.xigam.onelineofcode.utils.MusicUtil;
import tech.xigam.onelineofcode.utils.TrackUtil;
import tech.xigam.onelineofcode.utils.absolute.RPCClient;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.TimerTask;

public final class ElixirRefreshTask extends TimerTask {
    public Guild guild; 
    public Bot bot;
    
    @Override 
    public void run() {
        MusicUtil.getPlayingFromBot(track -> {
            if(track == null) {
                RPCClient.client.sendRichPresence(RPCClient.presence.build());
                return;
            }

            if(!TrackUtil.isCached(track)) {
                try {
                    TrackUtil.pushTrackToDiscord(track);
                } catch (Exception ignored) { }
            }

            var endsIn = Duration.ofMillis(track.length - track.position).toSeconds();
            var presence = new PresenceDetails()
                    .setEndTimestamp(OffsetDateTime.now().plusSeconds(endsIn))
                    .setDetails("Listening to " + track.title)
                    .setState("by " + track.author)
                    .setLargeImage(TrackUtil.extractTrackId(track.uri).toLowerCase(), track.title)
                    .setSmallImage("kazuha", "From Guild: " + guild.getName());
            RPCClient.client.sendRichPresence(presence.build());
        }, this.guild, this.bot);
    }
}

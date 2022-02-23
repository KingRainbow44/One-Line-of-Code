package tech.xigam.onelineofcode.utils.absolute;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.objects.PresenceDetails;

import java.time.OffsetDateTime;

public final class RPCClient {
    public static final IPCClient client = new IPCClient(Long.parseLong(Constants.PRESENCE_CLIENT_ID));
    public static PresenceDetails presence;
    
    public static void initialize() {
        client.setListener(new Listener());
        try {
            client.connect();
        } catch (Exception ignored) {
            OneLineOfCode.logger.info("Unable to detect a Discord client.");
        }
    }
    
    public static void updateConfig() {
        var config = OneLineOfCode.activities;
        config.richPresence.details = presence.details;
        config.richPresence.state = presence.state;
        config.richPresence.smallImage = presence.smallImageKey;
        config.richPresence.largeImage = presence.largeImageKey;
    }
    
    static class Listener implements IPCListener {
        @Override public void onReady(IPCClient client) {
            OneLineOfCode.logger.info("RPC Client logged into Discord.");

            var presenceConfig = OneLineOfCode.activities.richPresence;
            presence = new PresenceDetails().setStartTimestamp(OffsetDateTime.now())
                            .setDetails(presenceConfig.details).setState(presenceConfig.state);
            if(!presenceConfig.largeImage.isEmpty())
                presence.setLargeImage(presenceConfig.largeImage);
            if(!presenceConfig.smallImage.isEmpty())
                presence.setSmallImage(presenceConfig.smallImage);
            client.sendRichPresence(presence.build());
        }
    }
}

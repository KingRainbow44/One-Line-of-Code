package tech.xigam.onelineofcode.utils.absolute;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.objects.PresenceDetails;

public final class RPCClient {
    public static final IPCClient client = new IPCClient(Long.parseLong(Constants.PRESENCE_CLIENT_ID));
    public static boolean useElixir = false;
    public static PresenceDetails presence;

    public static void initialize() {
        client.setListener(new Listener());
        try {
            client.connect();
        } catch (Exception exception) {
            OneLineOfCode.logger.info("Unable to detect a Discord client.", exception);
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
        @Override
        public void onReady(IPCClient client) {
            OneLineOfCode.logger.info("RPC Client logged into Discord.");

            var presenceConfig = OneLineOfCode.activities.richPresence;
            presence = new PresenceDetails()
                    .setDetails(presenceConfig.details).setState(presenceConfig.state);
            if (presenceConfig.largeImage != null)
                presence.setLargeImage(presenceConfig.largeImage);
            if (presenceConfig.smallImage != null)
                presence.setSmallImage(presenceConfig.smallImage);
            client.sendRichPresence(presence.build());
        }
    }
}

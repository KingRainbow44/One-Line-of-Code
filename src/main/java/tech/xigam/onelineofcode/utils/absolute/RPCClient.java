package tech.xigam.onelineofcode.utils.absolute;

import club.bottomservices.discordrpc.lib.DiscordRPCClient;
import club.bottomservices.discordrpc.lib.EventListener;
import club.bottomservices.discordrpc.lib.RichPresence;
import club.bottomservices.discordrpc.lib.User;
import org.jetbrains.annotations.NotNull;
import tech.xigam.onelineofcode.OneLineOfCode;

import java.util.Arrays;

public final class RPCClient {
    public static final DiscordRPCClient client = new DiscordRPCClient(Constants.PRESENCE_CLIENT_ID);
    public static boolean useElixir = false;
    public static RichPresence.Builder presence;

    public static void initialize() {
        client.listeners = new Listener();
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
        config.richPresence.smallImage = presence.smallImage;
        config.richPresence.largeImage = presence.largeImage;
    }

    static class Listener implements EventListener {
        @Override
        public void onReady(@NotNull DiscordRPCClient client, @NotNull User user) {
            OneLineOfCode.logger.info("RPC Client logged into Discord.");

            var presenceConfig = OneLineOfCode.activities.richPresence;
            presence = new RichPresence.Builder()
                    .setText(presenceConfig.details, presenceConfig.state)
                    .setAssets(presenceConfig.largeImage, presenceConfig.smallImage, null, null);
            if(presenceConfig.buttons.length > 0)
                Arrays.stream(presenceConfig.buttons).forEach(button -> presence.addButton(button.label, button.url));
            client.sendPresence(presence.build());
        }
    }
}

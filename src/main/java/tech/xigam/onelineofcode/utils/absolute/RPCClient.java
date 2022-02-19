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
    
    static class Listener implements IPCListener {
        @Override public void onReady(IPCClient client) {
            OneLineOfCode.logger.info("RPC Client logged into Discord.");

            presence = new PresenceDetails().setStartTimestamp(OffsetDateTime.now())
                    .setDetails("Competing in a tennis match.")
                    .setState("It's not going well right now...");
            client.sendRichPresence(presence.build());
        }
    }
}

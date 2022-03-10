package tech.xigam.onelineofcode;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.xigam.basicqueuer.BasicQueue;
import tech.xigam.cch.ComplexCommandHandler;
import tech.xigam.elixirapi.ElixirAPI;
import tech.xigam.express.Express;
import tech.xigam.express.Router;
import tech.xigam.onelineofcode.commands.*;
import tech.xigam.onelineofcode.listeners.ActivityListener;
import tech.xigam.onelineofcode.listeners.MessageListener;
import tech.xigam.onelineofcode.objects.RemoteAction;
import tech.xigam.onelineofcode.routes.GenericEndpoints;
import tech.xigam.onelineofcode.routes.MagixEndpoints;
import tech.xigam.onelineofcode.routes.SpotifyEndpoints;
import tech.xigam.onelineofcode.utils.FileUtil;
import tech.xigam.onelineofcode.utils.JsonUtil;
import tech.xigam.onelineofcode.utils.RemoteUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;
import tech.xigam.onelineofcode.utils.absolute.RPCClient;
import tech.xigam.onelineofcode.utils.spotify.SpotifyInstance;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class OneLineOfCode extends WebSocketServer {
    public static final ComplexCommandHandler commandHandler = new ComplexCommandHandler(true);
    public static final BasicQueue bluejayQueue = new BasicQueue(TimeUnit.MINUTES.toMillis(1));
    public static final tech.xigam.onelineofcode.objects.Activity activities;
    public static JDA jda;
    public static User magix;
    public static WebSocket client;
    public static ElixirAPI elixirApi;
    public static SpotifyInstance spotifyInstance;
    public static Logger logger = LoggerFactory.getLogger(OneLineOfCode.class);
    
    public static final Map<String, List<String>> deletedMessages = new HashMap<>();

    static {
        if (!Constants.check()) {
            logger.error("One or more critical constants are missing.");
            System.exit(0);
        }

        if (!FileUtil.checkFiles()) {
            logger.error("One or more critical files are missing.");
            System.exit(0);
        }

        // Set variables.
        activities = JsonUtil.jsonFileDeserialize(
                new File(System.getProperty("user.dir"), "activity.json"),
                tech.xigam.onelineofcode.objects.Activity.class
        );
    }

    public OneLineOfCode(int port) {
        super(new InetSocketAddress(port));
    }

    public static void main(String[] args) {
        try {
            var jda = JDABuilder.create(Constants.BOT_AUTHORIZATION, EnumSet.allOf(GatewayIntent.class))
                    .setActivity(parseBotActivity()).setStatus(parseBotStatus())
                    .addEventListeners(commandHandler, new ActivityListener(), new MessageListener())
                    .enableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.ONLINE_STATUS);

            OneLineOfCode.jda = jda.build();
            continueSetup(); // Continue setting extra variables.
        } catch (Exception exception) {
            logger.error("An error occurred while initializing JDA.", exception);
            System.exit(0);
        }
    }

    private static Activity parseBotActivity() {
        var text = activities.bot.status.text;
        return switch (activities.bot.status.action) {
            default -> Activity.playing(text);
            case "listening" -> Activity.listening(text);
            case "watching" -> Activity.watching(text);
            case "streaming" -> Activity.streaming(text, "https://twitch.tv/lolMagixD");
            case "competing" -> Activity.competing(text);
        };
    }

    private static OnlineStatus parseBotStatus() {
        return switch (activities.bot.presence) {
            default -> OnlineStatus.ONLINE;
            case "dnd" -> OnlineStatus.DO_NOT_DISTURB;
            case "idle" -> OnlineStatus.IDLE;
            case "invisible" -> OnlineStatus.INVISIBLE;
        };
    }

    /*
     * WebSocket Code
     */

    private static void continueSetup() {
        // Cache the Magix user.
        jda.retrieveUserById(Constants.MAGIX_USER_ID).queue(user -> magix = user);
        // Create the web socket server.
        new OneLineOfCode(8080).start();
        // Start the Discord Rich Presence.
        RPCClient.initialize();
        // Start the queue.
        bluejayQueue.start();
        // Setup the Elixir API.
        elixirApi = ElixirAPI.create(Constants.PONJO_API_KEY);

        // Setup command handler.
        commandHandler.setJda(OneLineOfCode.jda);
        commandHandler.setPrefix("m!");
        commandHandler.registerCommand(new DeployCommand());
        commandHandler.registerCommand(new ShutdownCommand());
        commandHandler.registerCommand(new ActivityCommand());
        commandHandler.registerCommand(new MusicCommand());
        commandHandler.registerCommand(new HowLongCommand());
        commandHandler.registerCommand(new SnipeCommand());

        try { // Setup Express.
            var router = new Router()
                    .get("/", GenericEndpoints::indexEndpoint)
                    .get("/magix", MagixEndpoints::indexEndpoint)
                    .get("/magix/message", MagixEndpoints::messageEndpoint);

            if (Constants.SPOTIFY_AUTH_CODE.isEmpty())
                router.get("/spotify/callback", SpotifyEndpoints::callbackEndpoint);
            else OneLineOfCode.spotifyInstance = new SpotifyInstance(Constants.SPOTIFY_AUTH_CODE);
            Express.create(42069)
                    .notFound(GenericEndpoints::notFoundEndpoint)
                    .router(router).listen();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void onStart() {
        logger.info("WebSocket server started on " + this.getPort() + ".");
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        if (client == null) {
            client = webSocket;
            logger.info("WebSocket client connected.");
            
            // Update client.
            @SuppressWarnings("ConstantConditions") 
            var status = jda.getGuildById(Constants.XIGAM_SERVER_ID)
                    .getMemberById(Constants.BLUEJAY_USER_ID)
                    .getOnlineStatus();
            switch (status) {
                case ONLINE -> RemoteUtil.updateClient(RemoteAction.customStatus("yay! bluejay's here!", "Childelol_Soreko", "755380388934975488"));
                case OFFLINE -> RemoteUtil.updateClient(RemoteAction.customStatus("cri, bluejay isn't here T-T", "ChildeCri_wroughten", "896479017312616519"));
                case IDLE, DO_NOT_DISTURB -> RemoteUtil.updateClient(RemoteAction.customStatus("hmm, bluejay's here?", "ChildeThinking", "897807626295988294"));
                default -> RemoteUtil.updateClient(RemoteAction.blankStatus());
            }
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        if (client == webSocket) {
            client = null;
            logger.info("WebSocket client disconnected.");
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
    }
}

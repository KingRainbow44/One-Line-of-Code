package tech.xigam.onelineofcode;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.xigam.cch.ComplexCommandHandler;
import tech.xigam.express.Express;
import tech.xigam.express.Router;
import tech.xigam.onelineofcode.commands.ActivityCommand;
import tech.xigam.onelineofcode.commands.DeployCommand;
import tech.xigam.onelineofcode.commands.ShutdownCommand;
import tech.xigam.onelineofcode.commands.SpotifyCommand;
import tech.xigam.onelineofcode.listeners.ActivityListener;
import tech.xigam.onelineofcode.routes.GenericEndpoints;
import tech.xigam.onelineofcode.routes.SpotifyEndpoints;
import tech.xigam.onelineofcode.utils.absolute.Constants;
import tech.xigam.onelineofcode.utils.spotify.SpotifyInstance;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.EnumSet;

public final class OneLineOfCode extends WebSocketServer {
    public static JDA jda;
    public static User magix;
    public static WebSocket client;
    public static SpotifyInstance spotifyInstance;
    public static Logger logger = LoggerFactory.getLogger(OneLineOfCode.class);
    
    public static final ComplexCommandHandler commandHandler = new ComplexCommandHandler(true);
    
    static {
        if(!Constants.check()) {
            logger.error("One or more critical constants are missing.");
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        try {
            var jda = JDABuilder.create(Constants.BOT_AUTHORIZATION, EnumSet.allOf(GatewayIntent.class))
                    .setActivity(Activity.competing("a tennis match"))
                    .addEventListeners(commandHandler, new ActivityListener())
                    .enableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.ONLINE_STATUS);
            
            OneLineOfCode.jda = jda.build();
            continueSetup(); // Continue setting extra variables.
        } catch (Exception exception) {
            logger.error("An error occurred while initializing JDA.", exception);
            System.exit(0);
        }
    }
    
    private static void continueSetup() {
        // Cache the Magix user.
        jda.retrieveUserById(Constants.MAGIX_USER_ID).queue(user -> magix = user);
        
        // Create the web socket server.
        new OneLineOfCode(8080).start();
        
        // Setup command handler.
        commandHandler.setJda(OneLineOfCode.jda);
        commandHandler.setPrefix("m!");
        commandHandler.registerCommand(new DeployCommand());
        commandHandler.registerCommand(new ShutdownCommand());
        commandHandler.registerCommand(new ActivityCommand());
        commandHandler.registerCommand(new SpotifyCommand());
        
        // Setup Express.
        try {
            var router = new Router()
                    .get("/", GenericEndpoints::indexEndpoint);
            
            if(Constants.SPOTIFY_AUTH_CODE == null)
                router.get("/spotify/callback", SpotifyEndpoints::callbackEndpoint);
            else OneLineOfCode.spotifyInstance = new SpotifyInstance(Constants.SPOTIFY_AUTH_CODE);
            Express.create(42069)
                    .notFound(GenericEndpoints::notFoundEndpoint)
                    .router(router).listen();
        } catch (IOException ignored) { }
    }

    /*
     * WebSocket Code
     */
    
    public OneLineOfCode(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
        logger.info("WebSocket server started on " + this.getPort() + ".");
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        if(client == null) {
            client = webSocket;
            logger.info("WebSocket client connected.");
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        if(client == webSocket) {
            client = null;
            logger.info("WebSocket client disconnected.");
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) { }

    @Override
    public void onError(WebSocket webSocket, Exception e) { }
}

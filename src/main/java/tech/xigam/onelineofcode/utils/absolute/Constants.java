package tech.xigam.onelineofcode.utils.absolute;

import tech.xigam.onelineofcode.Config;
import tech.xigam.onelineofcode.OneLineOfCode;

import java.awt.*;

public final class Constants {
    public static final String BLUEJAY_USER_ID = "721523435343183896";
    public static final String MAGIX_USER_ID = "252090676068614145";
    
    public static final String XIGAM_SERVER_ID = "884951643395862548";
    
    public static final String SPOTIFY_CLIENT_ID = Config.get("SPOTIFY-CLIENT-ID");
    
    public static final String BOT_AUTHORIZATION = Config.get("TOKEN");
    public static final String PONJO_API_KEY = Config.get("PONJO-API-KEY");
    public static final String SPOTIFY_AUTH_CODE = Config.get("SPOTIFY-AUTH-CODE");
    public static final String SPOTIFY_SECRET = Config.get("SPOTIFY-CLIENT-SECRET");
    
    public static final String SPOTIFY_CALLBACK_URI = Config.get("SPOTIFY-CALLBACK-URI");
    
    public static final Color EMBED_COLOR = new Color(3, 252, 236);
    
    public static boolean check() {
        var logger = OneLineOfCode.logger;
        if(BOT_AUTHORIZATION == null) {
            logger.warn("Bot token is not set.");
            return false;
        } else if (PONJO_API_KEY == null) {
            logger.warn("Ponjo API key is not set.");
            return false;
        }
        
        return true;
    }
}

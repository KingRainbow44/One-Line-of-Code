package tech.xigam.onelineofcode;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import tech.xigam.onelineofcode.listeners.ActivityListener;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import java.util.EnumSet;

public final class OneLineOfCode {
    private static JDA jda;
    
    public static User magix;
    
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar OneLineOfCode.jar <token>");
            System.exit(1); return;
        }
        
        try {
            var jda = JDABuilder.create(args[0], EnumSet.allOf(GatewayIntent.class))
                    .setActivity(Activity.competing("a tennis game"))
                    .addEventListeners(new ActivityListener())
                    .enableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.ONLINE_STATUS);
            
            OneLineOfCode.jda = jda.build();
            continueSetup(); // Continue setting extra variables.
        } catch (Exception ignored) { }
    }
    
    private static void continueSetup() {
        jda.retrieveUserById(Constants.MAGIX_USER_ID).queue(user -> magix = user);
    }
}

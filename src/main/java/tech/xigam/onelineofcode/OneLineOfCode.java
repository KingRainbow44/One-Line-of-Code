package tech.xigam.onelineofcode;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import tech.xigam.onelineofcode.utils.absolute.Constants;

public final class OneLineOfCode {
    private static JDA jda;
    
    public static OnlineStatus lastStatus;
    public static User magix;
    
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar OneLineOfCode.jar <token>");
            System.exit(1); return;
        }
        
        try {
            var jda = JDABuilder.createDefault(args[0]);
            jda.setActivity(Activity.competing("a tennis game"));
            
            OneLineOfCode.jda = jda.build();
            continueSetup(); // Continue setting extra variables.
        } catch (Exception ignored) { }
    }
    
    private static void continueSetup() {
        OneLineOfCode.magix = // Set Magix's user.
                OneLineOfCode.jda.getUserById(Constants.MAGIX_USER_ID);
    }
}

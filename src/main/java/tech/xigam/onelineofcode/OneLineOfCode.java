package tech.xigam.onelineofcode;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public final class OneLineOfCode {
    private static JDA jda;
    
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar OneLineOfCode.jar <token>");
            System.exit(1); return;
        }
        
        try {
            var jda = JDABuilder.createDefault(args[0]);
            jda.setActivity(Activity.competing("a tennis game"));
            
            OneLineOfCode.jda = jda.build();
        } catch (Exception ignored) { }
    }
}

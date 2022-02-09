package tech.xigam.onelineofcode.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import tech.xigam.onelineofcode.utils.absolute.Constants;

public final class MessageUtil {
    public static void sendMessageTo(User user, String message) {
        user.openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
    }
    
    public static void sendMessageTo(User user, MessageEmbed embed) {
        user.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(embed).queue());
    }
    
    public static void sendMessageTo(User user, EmbedBuilder embed) {
        MessageUtil.sendMessageTo(user, embed.build());
    }
    
    public static EmbedBuilder generateEmbed(String description) {
        return new EmbedBuilder()
                .setDescription(description)
                .setColor(Constants.EMBED_COLOR);
    }
    
    public static MessageEmbed genericEmbed(String description) {
        return generateEmbed(description).build();
    }
}

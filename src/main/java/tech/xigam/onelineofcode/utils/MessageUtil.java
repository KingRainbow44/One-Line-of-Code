package tech.xigam.onelineofcode.utils;

import net.dv8tion.jda.api.entities.User;

public final class MessageUtil {
    public static void sendMessageTo(User user, String message) {
        user.openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
    }
}

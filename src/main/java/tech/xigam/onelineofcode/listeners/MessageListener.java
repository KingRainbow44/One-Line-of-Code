package tech.xigam.onelineofcode.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.xigam.onelineofcode.utils.absolute.Constants;

public final class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        var user = event.getAuthor();
        var message = event.getMessage().getContentRaw();
        
        if(
                !user.getId().equals(Constants.MAGIX_USER_ID) ||
                !message.matches("<@" + Constants.MAGIX_USER_ID + "> shutdown")
        ) return;
        
        System.exit(1000);
    }
}

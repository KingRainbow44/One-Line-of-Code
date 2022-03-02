package tech.xigam.onelineofcode.listeners;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.objects.MessageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class MessageListener extends ListenerAdapter {
    private static final Map<String, MessageData> messages = new HashMap<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        var message = event.getMessage();
        messages.put(message.getId(), MessageData.from(
                message.getContentRaw(), message.getAuthor().getId()
        ));
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if(messages.containsKey(event.getMessageId())) {
            var messageData = messages.get(event.getMessageId());
            var messages = OneLineOfCode.deletedMessages;
            
            if(!messages.containsKey(messageData.messageAuthor)) {
                messages.put(messageData.messageAuthor, new ArrayList<>());
            } messages.get(messageData.messageAuthor).add(messageData.messageContent);
            MessageListener.messages.remove(event.getMessageId());
        }
    }
}

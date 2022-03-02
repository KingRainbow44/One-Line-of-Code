package tech.xigam.onelineofcode.objects;

public final class MessageData {
    public String messageContent, messageAuthor;
    
    public static MessageData from(String messageContent, String messageAuthor) {
        var messageData = new MessageData();
        messageData.messageContent = messageContent;
        messageData.messageAuthor = messageAuthor;
        
        return messageData;
    }
}

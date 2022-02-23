package tech.xigam.onelineofcode.routes;

import tech.xigam.express.Request;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.EncodingUtil;
import tech.xigam.onelineofcode.utils.MailUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import javax.mail.MessagingException;
import javax.mail.Transport;

@SuppressWarnings("JavadocReference")
public final class MagixEndpoints {
    /**
     * /magix/
     */
    
    public static void indexEndpoint(Request request) {
        request.respond("This is the Magix endpoint.");
    }

    /**
     * /magix/message
     * @param message The message to send.
     */
    
    public static void messageEndpoint(Request request) {
        var message = request.requestArguments.getOrDefault("message", "");
        if(message.isEmpty()) {
            request.code(400).respond("Missing message argument."); return;
        } message = EncodingUtil.base64Decode(message);
        
        var session = MailUtil.createSession(
                MailUtil.defaultProperties,
                Constants.GMAIL_USERNAME,
                EncodingUtil.base64Decode(Constants.GMAIL_PASSWORD)
        );
        
        request.respond("Sending message...");
        
        var messageConfig = new MailUtil.MessageData()
                .setTo(Constants.ATT_PHONE_NUMBER + "@txt.att.net")
                .setFrom(Constants.GMAIL_USERNAME)
                .setSubject(request.httpExchange.getRemoteAddress().getAddress().getHostAddress())
                .setRawContent(message);
        var emailMessage = MailUtil.craftMessage(session, messageConfig);
        
        try {
            if(emailMessage == null)
                throw new MessagingException("Failed to craft email message.");
            Transport.send(emailMessage); request.respond("Message sent.");
        } catch (MessagingException exception) {
            request.code(500).respond("Unable to send message.");
            OneLineOfCode.logger.error("Unable to send message.", exception);
        }
    }
}

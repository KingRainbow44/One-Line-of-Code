package tech.xigam.onelineofcode.listeners;

import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;

public final class ActivityListener extends ListenerAdapter {
    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        var member = event.getMember(); var guild = event.getGuild();
        if(
                !member.getId().matches(Constants.BLUEJAY_USER_ID) ||
                !guild.getId().matches(Constants.XIGAM_SERVER_ID)
        )
            return;
        
        MessageUtil.sendMessageTo(OneLineOfCode.magix, switch(event.getNewOnlineStatus()) {
            case ONLINE -> MessageUtil.genericEmbed("Bluejay is now online!");
            case OFFLINE -> MessageUtil.genericEmbed("Bluejay is now offline.");
            case IDLE -> MessageUtil.genericEmbed("Bluejay is now idle.");
            default -> MessageUtil.genericEmbed("Bluejay is now " + event.getNewOnlineStatus().name().toLowerCase() + ".");
        });
    }
}

package tech.xigam.onelineofcode.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;

public final class ActivityListener extends ListenerAdapter {
    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        var member = event.getMember();
        if(!member.getId().matches(Constants.BLUEJAY_USER_ID))
            return;
        
        OneLineOfCode.lastStatus = event.getOldOnlineStatus();
        switch(event.getNewOnlineStatus()) {
            case ONLINE:
                if(OneLineOfCode.lastStatus == OnlineStatus.OFFLINE)
                    MessageUtil.sendMessageTo(OneLineOfCode.magix, "Bluejay is now online!");
                break;
            case OFFLINE:
                break;
            case IDLE:
                break;
            case UNKNOWN:
            case DO_NOT_DISTURB:
            case INVISIBLE:
                break;
        }
    }
}

package tech.xigam.onelineofcode.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.objects.RemoteAction;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.RemoteUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;
import tech.xigam.onelineofcode.utils.absolute.Storage;

public final class ActivityListener extends ListenerAdapter {
    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        var member = event.getMember();
        var guild = event.getGuild();
        if (
                !member.getId().matches(Constants.BLUEJAY_USER_ID) ||
                        !guild.getId().matches(Constants.XIGAM_SERVER_ID)
        ) return;

        var newStatus = event.getNewOnlineStatus();
        MessageUtil.sendMessageTo(OneLineOfCode.magix, switch (newStatus) {
            case ONLINE -> MessageUtil.genericEmbed("Bluejay is now online!");
            case OFFLINE -> MessageUtil.genericEmbed("Bluejay is now offline.");
            case IDLE -> MessageUtil.genericEmbed("Bluejay is now idle.");
            case DO_NOT_DISTURB -> MessageUtil.genericEmbed("Bluejay is now busy.");
            default -> MessageUtil.genericEmbed("Bluejay is now " + event.getNewOnlineStatus().name().toLowerCase() + ".");
        });

        if (newStatus == OnlineStatus.OFFLINE)
            Storage.store("bluejayOfflineAt", System.currentTimeMillis());
        
        OneLineOfCode.bluejayQueue.queueAction(time -> {
            switch (newStatus) {
                case ONLINE -> RemoteUtil.updateClient(RemoteAction.customStatus("yay! bluejay's here!", "Childelol_Soreko", "755380388934975488"));
                case OFFLINE -> RemoteUtil.updateClient(RemoteAction.customStatus("cri, bluejay isn't here T-T", "ChildeCri_wroughten", "896479017312616519"));
                case IDLE, DO_NOT_DISTURB -> RemoteUtil.updateClient(RemoteAction.customStatus("hmm, bluejay's here?", "ChildeThinking", "897807626295988294"));
                default -> RemoteUtil.updateClient(RemoteAction.blankStatus());
            }
        });
    }
}

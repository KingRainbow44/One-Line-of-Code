package tech.xigam.onelineofcode.commands.activity;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import java.util.Collection;
import java.util.List;

public final class StatusSubCommand extends SubCommand implements Arguments {
    public StatusSubCommand() {
        super("status", "Set the online status of the bot.");
    }

    @Override
    public void execute(Interaction interaction) {
        if(!interaction.getMember().getId().matches(Constants.MAGIX_USER_ID)) {
            interaction.reply(MessageUtil.genericEmbed("You do not have permission to use this command."));
            return;
        }
        
        var status = interaction.getArgument("status", String.class);
        var jda = OneLineOfCode.jda; OnlineStatus onlineStatus;
        switch(status) {
            default -> {
                interaction.reply(MessageUtil.genericEmbed("Invalid status."));
                return;
            }
            
            case "online" -> onlineStatus = OnlineStatus.ONLINE;
            case "idle" -> onlineStatus = OnlineStatus.IDLE;
            case "dnd" -> onlineStatus = OnlineStatus.DO_NOT_DISTURB;
            case "invisible" -> onlineStatus = OnlineStatus.INVISIBLE;
        }

        jda.getPresence().setPresence(onlineStatus, true);
        interaction.reply(MessageUtil.genericEmbed("Status changed to `" + status + "`."));
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.createWithChoices("status", "The status to change the bot to.", "status", OptionType.STRING, true, 0, "online", "idle", "dnd", "invisible")
        );
    }
}

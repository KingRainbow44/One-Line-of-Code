package tech.xigam.onelineofcode.commands;

import tech.xigam.cch.command.Baseless;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.commands.activity.PresenceSubCommand;
import tech.xigam.onelineofcode.commands.activity.StatusSubCommand;
import tech.xigam.onelineofcode.utils.MessageUtil;

public final class ActivityCommand extends Command implements Baseless {
    public ActivityCommand() {
        super("activity", "Set the bot's current activity.");
        
        registerSubCommand(new StatusSubCommand());
        registerSubCommand(new PresenceSubCommand());
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.reply(MessageUtil.genericEmbed("Use `/activity <status|presence> <type>` to set the bot's activity."));
    }
}

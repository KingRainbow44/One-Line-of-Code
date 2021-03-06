package tech.xigam.onelineofcode.commands;

import tech.xigam.cch.command.Baseless;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.commands.activity.*;
import tech.xigam.onelineofcode.utils.MessageUtil;

public final class ActivityCommand extends Command implements Baseless {
    public ActivityCommand() {
        super("activity", "Set the bot or the user's current activity.");

        registerSubCommand(new ConnectSubCommand());
        registerSubCommand(new StatusSubCommand());
        registerSubCommand(new PresenceSubCommand());
        registerSubCommand(new RPCSubCommand());
        registerSubCommand(new ElixirSubCommand());
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.reply(MessageUtil.genericEmbed("Use `/activity <connect|status|presence|rpc> [other arguments]` to set the bot's activity."));
    }
}

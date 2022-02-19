package tech.xigam.onelineofcode.commands;

import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;

public final class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("shutdown", "Stop the bot process.");
    }

    @Override
    public void execute(Interaction interaction) {
        if (!interaction.getMember().getId().matches(Constants.MAGIX_USER_ID)) {
            interaction.reply(MessageUtil.genericEmbed("You do not have permission to use this command."));
            return;
        }

        interaction.reply(MessageUtil.genericEmbed("Shutting down..."));
        System.exit(1000);
    }
}

package tech.xigam.onelineofcode.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import java.util.Collection;
import java.util.List;

public final class DeployCommand extends Command implements Arguments {
    public DeployCommand() {
        super("deploy", "Deploy slash-commands here or globally.");
    }

    @Override
    public void execute(Interaction interaction) {
        if(!interaction.getMember().getId().matches(Constants.MAGIX_USER_ID)) {
            interaction.reply(MessageUtil.genericEmbed("You do not have permission to use this command."));
            return;
        }
        
        boolean isGlobal = interaction.getArgument("global", false, Boolean.class);
        interaction.reply(MessageUtil.genericEmbed("Deploying slash-commands " + (isGlobal ? "globally" : "here") + "..."));
        OneLineOfCode.commandHandler.deployAll(isGlobal ? null : interaction.getGuild());
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.create("global", "Should this deploy slash-command globally?", "global", OptionType.BOOLEAN, false, 0)
        );
    }
}

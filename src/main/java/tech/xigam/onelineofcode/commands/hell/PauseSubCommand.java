package tech.xigam.onelineofcode.commands.hell;

import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.elixirapi.Bot;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.MusicUtil;

import java.util.EnumSet;

public final class PauseSubCommand extends SubCommand {
    public PauseSubCommand() {
        super("pause", "Pauses all the bots at once.");
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.deferReply();
        
        var guild = interaction.getGuild();
        for(var bot : EnumSet.allOf(Bot.class)) {
            MusicUtil.pausePlayerOnBot(guild, bot);
        }
        
        interaction.reply(MessageUtil.genericEmbed("Paused all the bots."));
    }
}

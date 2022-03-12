package tech.xigam.onelineofcode.commands.hell;

import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.elixirapi.Bot;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.MusicUtil;

import java.util.EnumSet;

public final class ResumeSubCommand extends SubCommand {
    public ResumeSubCommand() {
        super("resume", "Resumes all the bots at once.");
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.deferReply();
        
        var guild = interaction.getGuild();
        for(var bot : EnumSet.allOf(Bot.class)) {
            MusicUtil.resumePlayerOnBot(guild, bot);
        }
        
        interaction.reply(MessageUtil.genericEmbed("Resumed all the bots."));
    }
}

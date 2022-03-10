package tech.xigam.onelineofcode.commands;

import tech.xigam.cch.command.Baseless;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.commands.music.HellCommand;
import tech.xigam.onelineofcode.commands.music.PlaySubCommand;
import tech.xigam.onelineofcode.utils.MessageUtil;

public final class MusicCommand extends Command implements Baseless {
    public MusicCommand() {
        super("music", "Music-related commands in one package.");

        registerSubCommand(new PlaySubCommand());
        registerSubCommand(new HellCommand());
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.reply(MessageUtil.genericEmbed("Use `/music <play|hell>` to do music-related things!"));
    }
}

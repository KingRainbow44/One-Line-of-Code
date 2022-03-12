package tech.xigam.onelineofcode.commands;

import tech.xigam.cch.command.Baseless;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.commands.hell.*;
import tech.xigam.onelineofcode.utils.MessageUtil;

public final class HellCommand extends Command implements Baseless {
    public HellCommand() {
        super("hell", "Do actions across all four music bots.");
        
        registerSubCommand(new JoinSubCommand());
        registerSubCommand(new PauseSubCommand());
        registerSubCommand(new ResumeSubCommand());
        registerSubCommand(new PlaySubCommand());
        registerSubCommand(new PlaylistSubCommand());
        registerSubCommand(new NowPlayingSubCommand());
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.reply(MessageUtil.genericEmbed("Run `/hell <join|pause|resume|play|playlist> [songs|playlists]`"));
    }
}
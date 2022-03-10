package tech.xigam.onelineofcode.commands;

import tech.xigam.cch.command.Baseless;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.commands.hell.JoinSubCommand;
import tech.xigam.onelineofcode.commands.hell.PauseSubCommand;
import tech.xigam.onelineofcode.commands.hell.PlaySubCommand;
import tech.xigam.onelineofcode.commands.hell.ResumeSubCommand;
import tech.xigam.onelineofcode.utils.MessageUtil;

public final class HellCommand extends Command implements Baseless {
    public HellCommand() {
        super("hell", "Do actions across all four music bots.");
        
        registerSubCommand(new JoinSubCommand());
        registerSubCommand(new PauseSubCommand());
        registerSubCommand(new ResumeSubCommand());
        registerSubCommand(new PlaySubCommand());
//        registerSubCommand(new PlaylistSubCommand());
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.reply(MessageUtil.genericEmbed("Run `/hell <join|pause|resume|play|playlist> [songs|playlists]`"));
    }
}
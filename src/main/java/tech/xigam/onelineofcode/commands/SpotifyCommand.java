package tech.xigam.onelineofcode.commands;

import tech.xigam.cch.command.Baseless;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.commands.spotify.PlaySubCommand;
import tech.xigam.onelineofcode.utils.MessageUtil;

public final class SpotifyCommand extends Command implements Baseless {
    public SpotifyCommand() {
        super("spotify", "Spotify-related commands in one package.");

        registerSubCommand(new PlaySubCommand());
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.reply(MessageUtil.genericEmbed("Use `/spotify <play>` to do Spotify-related things!"));
    }
}

package tech.xigam.onelineofcode.commands.hell;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;

import java.util.Collection;
import java.util.List;

public final class PlaySubCommand extends SubCommand implements Arguments {
    public PlaySubCommand() {
        super("play", "Play four different songs all at once.");
    }

    @Override
    public void execute(Interaction interaction) {
        
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.create("channel", "The voice channel to unleash hell in.", "channel", OptionType.CHANNEL, true, 0),
                Argument.create("song1", "The song to play on Elixir Music.", "song1", OptionType.STRING, true, 2),
                Argument.create("song2", "The song to play on Blue Elixir.", "song2", OptionType.STRING, true, 3),
                Argument.create("song3", "The song to play on Elixir Premium.", "song3", OptionType.STRING, true, 4),
                Argument.create("song4", "The song to play on Elixir Two.", "song4", OptionType.STRING, true, 5)
        );
    }
}

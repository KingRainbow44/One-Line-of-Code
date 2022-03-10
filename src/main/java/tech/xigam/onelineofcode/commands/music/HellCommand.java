package tech.xigam.onelineofcode.commands.music;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.elixirapi.Bot;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.MusicUtil;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public final class HellCommand extends SubCommand implements Arguments {
    public HellCommand() {
        super("hell", "Unleash hell into a voice channel.");
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.deferReply();
        
        var channel = interaction.getArgument("channel", GuildChannel.class);
        if(!(channel instanceof AudioChannel)) {
            interaction.reply(MessageUtil.genericEmbed("You must specify a voice channel!"));
            return;
        }
        
        var guild = interaction.getGuild();
        var song1 = interaction.getArgument("song1", String.class);
        var song2 = interaction.getArgument("song2", String.class);
        var song3 = interaction.getArgument("song3", String.class);
        var song4 = interaction.getArgument("song4", String.class);
        
        for(var bot : EnumSet.allOf(Bot.class)) {
            MusicUtil.joinChannelFromBot(guild, (AudioChannel) channel, bot);
        }
        
        MusicUtil.playTrackOnBot(guild, (AudioChannel) channel, song1, Bot.ELIXIR_MUSIC);
        MusicUtil.playTrackOnBot(guild, (AudioChannel) channel, song2, Bot.ELIXIR_BLUE);
        MusicUtil.playTrackOnBot(guild, (AudioChannel) channel, song3, Bot.ELIXIR_PREMIUM);
        MusicUtil.playTrackOnBot(guild, (AudioChannel) channel, song4, Bot.ELIXIR_TWO);
        
        interaction.reply(MessageUtil.genericEmbed("Attempted to **unleash hell** on `" + channel.getName() + "`."));
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

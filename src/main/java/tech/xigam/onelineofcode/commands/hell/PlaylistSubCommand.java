package tech.xigam.onelineofcode.commands.hell;

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

public final class PlaylistSubCommand extends SubCommand implements Arguments {
    public PlaylistSubCommand() {
        super("playlist", "Queue four different playlists all at once.");
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
        var playlist1 = interaction.getArgument("playlist1", String.class);
        var playlist2 = interaction.getArgument("playlist2", String.class);
        var playlist3 = interaction.getArgument("playlist3", String.class);
        var playlist4 = interaction.getArgument("playlist4", String.class);

        for(var bot : EnumSet.allOf(Bot.class)) {
            MusicUtil.joinChannelFromBot(guild, (AudioChannel) channel, bot);
        }

        MusicUtil.queuePlaylistOnBot(guild, (AudioChannel) channel, playlist1, Bot.ELIXIR_MUSIC);
        MusicUtil.queuePlaylistOnBot(guild, (AudioChannel) channel, playlist2, Bot.ELIXIR_BLUE);
        MusicUtil.queuePlaylistOnBot(guild, (AudioChannel) channel, playlist3, Bot.ELIXIR_PREMIUM);
        MusicUtil.queuePlaylistOnBot(guild, (AudioChannel) channel, playlist4, Bot.ELIXIR_TWO);

        interaction.reply(MessageUtil.genericEmbed("Attempted to **unleash hell** on `" + channel.getName() + "`."));
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.create("channel", "The voice channel to unleash hell in.", "channel", OptionType.CHANNEL, true, 0),
                Argument.create("playlist1", "The playlist to queue on Elixir Music.", "playlist1", OptionType.STRING, true, 2),
                Argument.create("playlist2", "The playlist to queue on Blue Elixir.", "playlist2", OptionType.STRING, true, 3),
                Argument.create("playlist3", "The playlist to queue on Elixir Premium.", "playlist3", OptionType.STRING, true, 4),
                Argument.create("playlist4", "The playlist to queue on Elixir Two.", "playlist4", OptionType.STRING, true, 5)
        );
    }
}

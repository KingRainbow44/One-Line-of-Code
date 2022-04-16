package tech.xigam.onelineofcode.commands.hell;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.elixirapi.Bot;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.MusicUtil;
import tech.xigam.onelineofcode.utils.TrackUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import java.util.Date;
import java.util.EnumSet;

public final class NowPlayingSubCommand extends SubCommand {
    public NowPlayingSubCommand() {
        super("nowplaying", "Get the current song playing on all the bots.");
    }

    @Override
    public void execute(Interaction interaction) {
        interaction.reply(MessageUtil.genericEmbed("The playing songs will be listed below!"));
        
        var guild = interaction.getGuild();
        for(var bot : EnumSet.allOf(Bot.class)) {
            MusicUtil.getPlayingFromBot(track -> {
                final String thumbnail = TrackUtil.getCoverArt(track);
                final String title = track.title.length() > 60 ? track.title.substring(0, 60) + "..." : track.title;
                final String duration = TrackUtil.formatDuration(track.position) + "/" + TrackUtil.formatDuration(track.length);
                final String artist = track.author;
                final String url = track.uri;
                final String contents = """
                            • Artist: %s
                            • Duration: %s
                            • Bot: %s
                            """.formatted(artist, duration, fancyBotName(bot));
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Currently Playing")
                        .setDescription(String.format("[%s](%s)", title, url))
                        .setColor(Constants.EMBED_COLOR)
                        .addField("Track Data", contents, false)
                        .setTimestamp(new Date().toInstant())
                        .setThumbnail(thumbnail)
                        .build();
                interaction.sendMessage(embed);
            }, guild, bot);
        }
    }
    
    private String fancyBotName(Bot bot) {
        return switch(bot) {
            case ELIXIR_MUSIC -> "Elixir Music";
            case ELIXIR_PREMIUM -> "Elixir Premium";
            case ELIXIR_TWO -> "Elixir Two";
            case ELIXIR_BLUE -> "Blue Elixir";
        };
    }
}

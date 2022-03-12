package tech.xigam.onelineofcode.commands.hell;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.elixirapi.Bot;
import tech.xigam.elixirapi.objects.TrackObject;
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
            MusicUtil.getQueueFromBot(queue -> {
                final TrackObject info = queue.tracks.get(0);
                final String thumbnail = TrackUtil.getCoverArt(info);
                final String title = info.title.length() > 60 ? info.title.substring(0, 60) + "..." : info.title;
                final String duration = TrackUtil.formatDuration(info.position) + "/" + TrackUtil.formatDuration(info.length);
                final String artist = info.author;
                final String url = info.uri;
                final String contents = """
                            • Artist: %s
                            • Duration: %s
                            """.formatted(artist, duration);
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Currently Playing")
                        .setDescription(String.format("[%s](%s)", title, url))
                        .setColor(Constants.EMBED_COLOR)
                        .addField("Track Data", contents, false)
                        .setTimestamp(new Date().toInstant())
                        .setThumbnail(thumbnail)
                        .build();
                interaction.reply(embed, false);
            }, guild, bot);
        }
    }
}

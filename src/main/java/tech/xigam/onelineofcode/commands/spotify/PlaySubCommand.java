package tech.xigam.onelineofcode.commands.spotify;

import se.michaelthelin.spotify.model_objects.specification.Track;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.HttpUtil;
import tech.xigam.onelineofcode.utils.MessageUtil;

public final class PlaySubCommand extends SubCommand {
    public PlaySubCommand() {
        super("play", "Play the song Magix is listening to on Elixir!");
    }
    
    @Override
    public void execute(Interaction interaction) {
        assert interaction.getMember().getVoiceState() != null;
        if(!interaction.getMember().getVoiceState().inAudioChannel()) {
            interaction.reply(MessageUtil.genericEmbed("You are not in a voice channel!"));
            return;
        }
        
        interaction.deferReply();
        
        var spotifyInstance = OneLineOfCode.spotifyInstance;
        var currentlyPlaying = spotifyInstance.getPlayingTrack();
        if(currentlyPlaying == null) {
            interaction.reply(MessageUtil.genericEmbed("Magix isn't listening to anything right now!"));
            return;
        }
        
        var track = (Track) currentlyPlaying.getItem();
        var trackUrl = "https://open.spotify.com/track/" + track.getId();
        int statusCode = HttpUtil.playTrackOnElixir(interaction.getGuild(), trackUrl);
        
        if(statusCode != 200) {
            interaction.reply(MessageUtil.genericEmbed("Something went wrong while trying to play the track!"));
        } else {
            interaction.reply(MessageUtil.genericEmbed("Playing " + track.getName() + " by " + track.getArtists()[0].getName() + " on Elixir!"));
        }
    }
}

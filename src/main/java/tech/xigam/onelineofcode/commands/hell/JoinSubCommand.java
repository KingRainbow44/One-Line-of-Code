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

public final class JoinSubCommand extends SubCommand implements Arguments {
    public JoinSubCommand() {
        super("join", "Connect to the voice channel all at once.");
    }

    @Override
    public void execute(Interaction interaction) {
        var channel = interaction.getArgument("channel", GuildChannel.class);
        if(!(channel instanceof AudioChannel)) {
            interaction.reply(MessageUtil.genericEmbed("You need to specify a **voice** channel!"));
            return;
        }
        
        var guild = interaction.getGuild();
        for(var bot : EnumSet.allOf(Bot.class)) {
            MusicUtil.joinChannelFromBot(guild, (AudioChannel) channel, bot);
        }
        
        interaction.reply(MessageUtil.genericEmbed("Joined `" + channel.getName() + "`!"));
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.create("channel", "The channel to join.", "channel", OptionType.CHANNEL, true, 0)
        );
    }
}

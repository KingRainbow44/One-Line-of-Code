package tech.xigam.onelineofcode.commands.activity;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;

import java.util.Collection;
import java.util.List;

public final class PresenceSubCommand extends SubCommand implements Arguments {
    public PresenceSubCommand() {
        super("presence", "Set the presence of the bot.");
    }

    @Override
    public void execute(Interaction interaction) {
        if(!interaction.getMember().getId().matches(Constants.MAGIX_USER_ID)) {
            interaction.reply(MessageUtil.genericEmbed("You do not have permission to use this command."));
            return;
        }

        var jda = OneLineOfCode.jda; Activity activity;
        var message = interaction.getArgument("message", String.class);
        var activityType = interaction.getArgument("activity", String.class);
        
        switch (activityType) {
            case "competing" -> activity = Activity.competing(message);
            case "playing" -> activity = Activity.playing(message);
            case "streaming" -> activity = Activity.streaming(message, "https://www.twitch.tv/xigam");
            case "listening" -> activity = Activity.listening(message);
            case "watching" -> activity = Activity.watching(message);
            default -> {
                interaction.reply(MessageUtil.genericEmbed("Invalid activity type."));
                return;
            }
        }
        
        jda.getPresence().setPresence(activity, true);
        interaction.reply(MessageUtil.genericEmbed("Presence has been set to `" + activityType + "` with the message: `" + message + "`."));
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.createWithChoices("activity", "The activity type of the bot.", "activity", OptionType.STRING, true, 0, "competing", "playing", "streaming", "listening", "watching"),
                Argument.create("message", "The message of the activity.", "message", OptionType.STRING, true, 1)
        );
    }
}

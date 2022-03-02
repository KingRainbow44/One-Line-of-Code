package tech.xigam.onelineofcode.commands;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.MessageUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class SnipeCommand extends Command implements Arguments {
    public SnipeCommand() {
        super("snipe", "Find deleted messages from users!");
    }

    @Override
    public void execute(Interaction interaction) {
        if(!interaction.isSlash()) {
            interaction.reply(MessageUtil.genericEmbed("This command does not have prefix-based command support yet:tm:."));
            return;
        }
        
        interaction.setEphemeral(); // Hide the message to only the user.
        
        var user = interaction.getArgument("user", IMentionable.class);
        var deletedMessages = OneLineOfCode.deletedMessages.getOrDefault(user.getId(), Collections.emptyList());
        
        if(deletedMessages.isEmpty()) {
            interaction.reply(MessageUtil.genericEmbed("<@" + user.getId() + "> has not deleted any messages."));
        } else {
            var limit = interaction.getArgument("limit", 3L, Long.class);
            var embed = MessageUtil.generateEmbed("The following are deleted messages from: <@" + user.getId() + ">");
            for(int i = 0; i < Math.min(limit, deletedMessages.size()); i++) {
                var message = deletedMessages.get(i);
                embed.addField("Deleted Message #" + (i + 1), message, false);
            } interaction.reply(embed.build());
        }
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.create("user", "The user to search for deleted messages from.", "user", OptionType.MENTIONABLE, true, 0),
                Argument.create("limit", "The maximum amount of messages to return.", "limit", OptionType.INTEGER, false, 1).range(1, 20)
        );
    }
}

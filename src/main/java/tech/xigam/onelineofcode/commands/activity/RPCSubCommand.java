package tech.xigam.onelineofcode.commands.activity;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.FileUtil;
import tech.xigam.onelineofcode.utils.JsonUtil;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;
import tech.xigam.onelineofcode.utils.absolute.RPCClient;

import java.util.Collection;
import java.util.List;

public final class RPCSubCommand extends SubCommand implements Arguments {
    public RPCSubCommand() {
        super("rpc", "Set the rich presence of Magix (the user).");
    }

    @Override
    public void execute(Interaction interaction) {
        if (!interaction.getMember().getId().matches(Constants.MAGIX_USER_ID)) {
            interaction.reply(MessageUtil.genericEmbed("You do not have permission to use this command."));
            return;
        }

        var field = interaction.getArgument("field", String.class);
        var value = interaction.getArgument("value", String.class);
        switch (field) {
            default -> {
                interaction.reply(MessageUtil.genericEmbed("Invalid field."));
                return;
            }
            case "details" -> RPCClient.presence.setDetails(value);
            case "state" -> RPCClient.presence.setState(value);
            case "small image" -> RPCClient.presence.setSmallImage(value);
            case "big image" -> RPCClient.presence.setLargeImage(value);
        }

        RPCClient.client.sendRichPresence(RPCClient.presence.build());
        interaction.reply(MessageUtil.genericEmbed("Modified the rich presence's " + field + " to `" + value + "`."));

        // Save the file after replying.
        RPCClient.updateConfig(); // Update the config **object**.
        FileUtil.writeToFile(
                Constants.ACTIVITY_FILE,
                JsonUtil.jsonFileSerialize(OneLineOfCode.activities)
        );
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.createWithChoices("field", "The field to set.", "field", OptionType.STRING, true, 0, "details", "state", "small image", "big image"),
                Argument.createTrailingArgument("value", "The value to change it to.", "value", OptionType.STRING, true, 1)
        );
    }
}

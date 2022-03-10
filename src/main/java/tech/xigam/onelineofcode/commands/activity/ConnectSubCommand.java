package tech.xigam.onelineofcode.commands.activity;

import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;
import tech.xigam.onelineofcode.utils.absolute.RPCClient;

public final class ConnectSubCommand extends SubCommand {
    public ConnectSubCommand() {
        super("connect", "Force a reconnection to the Discord client.");
    }

    @Override
    public void execute(Interaction interaction) {
        if (!interaction.getMember().getId().matches(Constants.MAGIX_USER_ID)) {
            interaction.reply(MessageUtil.genericEmbed("You do not have permission to use this command."));
            return;
        }
        
        var client = RPCClient.client;
        if(client.getStatus() == PipeStatus.CONNECTED) {
            interaction.reply(MessageUtil.genericEmbed("The Discord client is already connected."));
            return;
        }
        
        try {
            client.connect();
            interaction.reply(MessageUtil.genericEmbed("Successfully connected to the Discord client."));
        } catch (Exception exception) {
            interaction.reply(MessageUtil.generateEmbed("An error occurred while trying to connect to the Discord client.")
                    .addField("Error", exception.getMessage(), false).build());
        }
    }
}

package tech.xigam.onelineofcode.commands.activity;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.SubCommand;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.elixirapi.Bot;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.runnable.ElixirRefreshTask;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.RPCClient;

import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public final class ElixirSubCommand extends SubCommand implements Arguments {
    public ElixirSubCommand() {
        super("elixir", "Force the RPC client to use Elixir's output.");
    }

    private static Timer timer;
    
    @Override
    public void execute(Interaction interaction) {
        if(RPCClient.useElixir) {
            interaction.reply(MessageUtil.genericEmbed("Switched to using custom activity."));
            
            RPCClient.useElixir = false; timer.cancel();
            RPCClient.client.sendRichPresence(RPCClient.presence.build());
        } else {
            interaction.deferReply();
            
            var bot = interaction.getArgument("bot", "none", String.class);
            var guild = interaction.getArgument("guild", "none", String.class);
            if(bot.equals("none") || guild.equals("none")) {
                interaction.reply(MessageUtil.genericEmbed("You must specify a bot and guild to use Elixir for your activity."));
                return;
            }
            
            var task = new ElixirRefreshTask();
            task.guild = OneLineOfCode.jda.getGuildById(guild);
            if(task.guild == null) {
                interaction.reply(MessageUtil.genericEmbed("The guild you specified does not exist."));
                return;
            }
            
            task.bot = switch(bot) {
                default -> throw new IllegalStateException("Unexpected value: " + bot);
                case "Purple" -> Bot.ELIXIR_MUSIC;
                case "Green" -> Bot.ELIXIR_PREMIUM;
                case "Orange" -> Bot.ELIXIR_TWO;
                case "Blue" -> Bot.ELIXIR_BLUE;
            };

            RPCClient.useElixir = true; timer = new Timer();
            timer.scheduleAtFixedRate(task, 0, TimeUnit.SECONDS.toMillis(15));
            interaction.reply(MessageUtil.genericEmbed("Switched to using Elixir's output."));
        }
    }

    @Override
    public Collection<Argument> getArguments() {
        return List.of(
                Argument.createWithChoices("bot", "The bot to read from.", "bot", OptionType.STRING, false, 0, "Purple", "Green", "Orange", "Blue"),
                Argument.create("guild", "The guild to read from.", "guild", OptionType.STRING, false, 1)
        );
    }
}

package tech.xigam.onelineofcode.commands;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import tech.xigam.cch.command.Arguments;
import tech.xigam.cch.command.Command;
import tech.xigam.cch.utils.Argument;
import tech.xigam.cch.utils.Interaction;
import tech.xigam.onelineofcode.OneLineOfCode;
import tech.xigam.onelineofcode.utils.EncodingUtil;
import tech.xigam.onelineofcode.utils.MessageUtil;
import tech.xigam.onelineofcode.utils.absolute.Constants;
import tech.xigam.onelineofcode.utils.absolute.Storage;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class HowLongCommand extends Command implements Arguments {
    private static final String BLUEJAY_ONLINE = "Bluejay was last online";
    private static final String MAGIX_BORN = "Magix was born";
    private static final String LEQEND_SIMP = "the day レケンド met his simp";
    private static final String START_GENSHIN = "Magix started playing Genshin Impact";
    private final String[] options = new String[]{
            "Bluejay was last online",
            "Magix was born",
            "the day レケンド met his simp",
            "Magix started playing Genshin Impact"
    };

    public HowLongCommand() {
        super("howlong", "How long has it been since...");
    }

    @Override
    public void execute(Interaction interaction) {
        var since = interaction.getArgument("since", String.class);
        switch (since) {
            default -> interaction.reply(MessageUtil.genericEmbed("I don't know how long it has been since " + since + "."));

            case BLUEJAY_ONLINE -> {
                interaction.deferReply();
                var status = Objects.requireNonNull(Objects.requireNonNull(OneLineOfCode.jda.getGuildById(Constants.XIGAM_SERVER_ID))
                        .getMember(User.fromId(Constants.BLUEJAY_USER_ID))).getOnlineStatus();
                if (status != OnlineStatus.OFFLINE) {
                    interaction.reply(MessageUtil.genericEmbed("Bluejay is currently online."));
                } else {
                    var now = System.currentTimeMillis();
                    var lastOnline = Storage.fetch("bluejayOfflineAt", Long.class);
                    if (lastOnline == null) {
                        Storage.store("bluejayOfflineAt", now); // Cache the time for now.
                        interaction.reply(MessageUtil.genericEmbed("I haven't tracked that metric yet!"));
                        return;
                    }

                    var diff = now - lastOnline;
                    var formatted = EncodingUtil.formatDuration(diff);
                    interaction.reply(MessageUtil.genericEmbed("Bluejay was last online **" + formatted + "** ago."));
                }
            }
            case MAGIX_BORN -> {
                var diff = ChronoUnit.SECONDS.between(Constants.MAGIX_BIRTHDAY, OffsetDateTime.now());
                interaction.reply(MessageUtil.genericEmbed("Magix was born:\n **" + EncodingUtil.formatPeriod(diff) + "** ago."));
            }
            case LEQEND_SIMP -> {
                var diff = ChronoUnit.SECONDS.between(Constants.LEQEND_MET_SIMP, OffsetDateTime.now());
                interaction.reply(MessageUtil.genericEmbed("レケンド met his simp:\n **" + EncodingUtil.formatPeriod(diff) + "** ago."));
            }
            case START_GENSHIN -> {
                var diff = ChronoUnit.SECONDS.between(Constants.GENSHIN_STARTED, OffsetDateTime.now());
                interaction.reply(MessageUtil.genericEmbed("Magix started playing Genshin Impact:\n **" + EncodingUtil.formatPeriod(diff) + "** ago."));
            }
        }
    }

    @Override
    public Collection<Argument> getArguments() {
        var argument = Argument.createWithChoices("since", "How long has it been since...", "since", OptionType.STRING, true, 0, options);
        argument.trailing = true; // This manually enables trailing arguments with choices.
        
        return List.of(argument);
    }
}

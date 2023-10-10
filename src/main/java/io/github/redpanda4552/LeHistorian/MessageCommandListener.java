package io.github.redpanda4552.LeHistorian;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class MessageCommandListener extends ListenerAdapter {

    private static final Pattern X_PATTERN = Pattern.compile("(https:\\/\\/x\\.com\\/.+\\/status\\/[^\\s?]+)");
    private static final Pattern TWITTER_PATTERN = Pattern.compile("(https:\\/\\/twitter\\.com\\/.+\\/status\\/[^\\s?]+)");
    
    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {
        if (!event.isFromGuild()) {
            event.reply("Message context commands are disabled in DMs.").setEphemeral(true).queue();
            return;
        }
        
        switch (event.getName()) {
        case "fix-tweet":
            Message msg = event.getTarget();
            String contentRaw = msg.getContentRaw();
            String contentFixed = null;

            Matcher xMatcher = X_PATTERN.matcher(contentRaw);
            
            if (xMatcher.find()) {
                contentFixed = xMatcher.group(1).replace("https://x.com", "https://fxtwitter.com");
            }

            Matcher twitterMatcher = TWITTER_PATTERN.matcher(contentRaw);

            if (twitterMatcher.find()) {
                contentFixed = twitterMatcher.group(1).replace("https://twitter.com", "https://fxtwitter.com");
            }

            if (contentFixed != null) {
                event.reply(contentFixed).addActionRow(Button.link(msg.getJumpUrl(), "Jump to Original Message")).queue();
            } else {
                event.reply("No tweets to fix").setEphemeral(true).queue();
            }
            
            return;
        default:
            break;
        }
        
        event.reply(":warning: ATTENTION :warning:\n\nEveryone, give " + event.getMember().getAsMention() + " a round of applause for screwing up. :clap::clap::clap:").queue();
    }

    public static CommandData getFixTweetCommandData() {
        return Commands.message("fix-tweet").setDefaultPermissions(DefaultMemberPermissions.ENABLED);
    }
}

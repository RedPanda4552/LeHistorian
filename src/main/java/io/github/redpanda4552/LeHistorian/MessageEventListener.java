package io.github.redpanda4552.LeHistorian;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class MessageEventListener extends ListenerAdapter {
    
    private static final Pattern X_PATTERN = Pattern.compile("(https:\\/\\/x\\.com\\/.+\\/status\\/[^\\s?]+)");
    private static final Pattern TWITTER_PATTERN = Pattern.compile("(https:\\/\\/twitter\\.com\\/.+\\/status\\/[^\\s?]+)");

    @Override 
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) {
            return;
        }
        
        Message msg = event.getMessage();
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
            event.getChannel().sendMessage(contentFixed).setMessageReference(msg.getId()).mentionRepliedUser(false).addActionRow(Button.link(msg.getJumpUrl(), "Jump to Original Message")).queue();
        }
    }
}
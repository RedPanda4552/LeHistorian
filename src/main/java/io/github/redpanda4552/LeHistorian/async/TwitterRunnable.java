package io.github.redpanda4552.LeHistorian.async;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.redpanda4552.LeHistorian.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TwitterRunnable implements Runnable {

    private static final Pattern X_PATTERN = Pattern.compile("(\\|\\||)(https:\\/\\/x\\.com\\/.+\\/status\\/[^\\s?]+)[^\\|]*(\\|\\||)");
    private static final Pattern TWITTER_PATTERN = Pattern.compile("(\\|\\||)(https:\\/\\/twitter\\.com\\/.+\\/status\\/[^\\s?]+)[^\\|]*(\\|\\||)");

    private String channelId;
    private String messageId;

    public TwitterRunnable(String channelId, String messageId) {
        this.channelId = channelId;
        this.messageId = messageId;
    }

    @Override 
    public void run() {
        TextChannel channel = Main.getSelf().getJDA().getTextChannelById(this.channelId);
        
        if (channel != null) {
            Message msg = channel.retrieveMessageById(messageId).complete();
            String contentRaw = msg.getContentRaw();
            String contentFixed = null;

            Matcher xMatcher = X_PATTERN.matcher(contentRaw);
            
            if (xMatcher.find()) {
                if (xMatcher.group(1).isEmpty()) {
                    contentFixed = xMatcher.group(2).replace("https://x.com", "https://fxtwitter.com");
                }
            }

            Matcher twitterMatcher = TWITTER_PATTERN.matcher(contentRaw);

            if (twitterMatcher.find()) {
                if (twitterMatcher.group(1).isEmpty()) {
                    contentFixed = twitterMatcher.group(2).replace("https://twitter.com", "https://fxtwitter.com");
                }
            }

            if (contentFixed != null) {
                channel.sendMessage(contentFixed)
                        .setMessageReference(msg.getId())
                        .mentionRepliedUser(false)
                        .addActionRow(
                            Button.link(msg.getJumpUrl(), "Jump to Original Message"),
                            Button.danger("delete:" + msg.getId(), "Delete Original Message"),
                            Button.primary("delete-self", "Delete This Message")
                        )
                        .queue();
            }
        }
    }
}

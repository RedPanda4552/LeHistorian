package io.github.redpanda4552.LeHistorian;

import io.github.redpanda4552.LeHistorian.async.TwitterRunnable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEventListener extends ListenerAdapter {
    
    @Override 
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) {
            return;
        }
        
        TwitterRunnable runnable = new TwitterRunnable(event.getChannel().getId(), event.getMessageId());
        Main.getSelf().getScheduler().runOnceDelayed(runnable, 2000);
    }
}

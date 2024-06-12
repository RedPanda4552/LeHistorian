package io.github.redpanda4552.LeHistorian;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonEventListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        
        if (buttonId == null) {
            event.reply("Bad button ID").setEphemeral(true).queue();
            return;
        }

        String[] parts = buttonId.split(":");
        TextChannel channel = Main.getSelf().getJDA().getTextChannelById(event.getChannel().getId());

        if (channel == null) {
            event.reply("Bad channel").setEphemeral(true).queue();
            return;
        }

        switch (parts[0]) {
            case "delete": {
                if (parts.length >= 2) {
                    channel.deleteMessageById(parts[1]).queue();
                    event.deferEdit().queue();
                }
                
                return;
            }
            case "delete-self": {
                channel.deleteMessageById(event.getMessageId()).queue();
                event.deferEdit().queue();
                return;
            }
            default: {
                return;
            }
        }
    }
}

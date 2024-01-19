package io.github.redpanda4552.LeHistorian;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonEventListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();

        if (buttonId == null) {
            return;
        }

        String[] parts = buttonId.split(":");

        if (parts.length != 2) {
            return;
        }

        TextChannel channel = Main.getSelf().getJDA().getTextChannelById(event.getChannel().getId());

        if (channel == null) {
            return;
        }

        switch (parts[0]) {
            case "delete":
                channel.deleteMessageById(event.getMessageId()).queue();
                break;
            default:
                break;
        }
    }
}

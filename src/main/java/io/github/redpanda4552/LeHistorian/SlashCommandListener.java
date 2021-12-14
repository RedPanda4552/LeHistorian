package io.github.redpanda4552.LeHistorian;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.isFromGuild()) {
            event.reply("Slash commands are disabled in DMs.").setEphemeral(true).queue();
            return;
        }
        
        switch (event.getName()) {
        case "archive":
            OptionMapping opt = event.getOption("link");
            
            if (opt == null) {
                event.reply("Missing required arg 'link'").setEphemeral(true).queue();
                return;
            }
            
            
            String res = ArchiveCommand.archive(event.getOption("link").getAsString());
            
            if (res != null && !res.isEmpty()) {
                event.reply("Added a new message to the archive, you can see it [here](" + res + ")").queue();
                return;
            }
            
            event.reply("Could not find a message at the given link.").setEphemeral(true).queue();
            break;
        default:
            break;
        }
        
        event.reply(":warning: ATTENTION :warning:\n\nEveryone, give " + event.getMember().getAsMention() + " a round of applause for screwing up. :clap::clap::clap:").queue();
    }
}

package io.github.redpanda4552.LeHistorian;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ArchiveCommand {

    public static String archive(String link) {
        if (!link.startsWith("http")) {
            return null;
        }
        
        String[] parts = link.split("/");
        
        if (parts.length < 3) {
            return null;
        }
        
        String messageId = parts[parts.length - 1];
        String channelId = parts[parts.length - 2];
        
        TextChannel channel = Main.getSelf().getJDA().getTextChannelById(channelId);
        Message oldMessage = channel.retrieveMessageById(messageId).complete();
        String archiveChannelId = Main.getSelf().getArchiveChannelId();
        Member member = oldMessage.getGuild().retrieveMember(oldMessage.getAuthor()).complete();
        
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(member.getEffectiveName(), link);
        eb.setColor(member.getColor());
        eb.setTimestamp(oldMessage.getTimeCreated());
        eb.setThumbnail(member.getEffectiveAvatarUrl());
        eb.setDescription(oldMessage.getContentDisplay());
        
        Message newMessage = Main.getSelf().getJDA().getTextChannelById(archiveChannelId).sendMessageEmbeds(eb.build()).complete();
        return newMessage.getJumpUrl();
    }
    
    public static CommandData getArchiveCommandDefinition() {
        return Commands.slash("archive", "Archives a message")
            .addOption(OptionType.STRING, "link", "'Copy Message Link', then paste here", true);
    }
}

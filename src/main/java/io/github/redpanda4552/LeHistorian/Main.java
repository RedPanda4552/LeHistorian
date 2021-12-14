package io.github.redpanda4552.LeHistorian;
import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main {

    private static Main self;
    private static String discordBotToken;
    private static String archiveChannelId;
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar LeHistorian-x.y.z.jar <discord-bot-token> <archive-channel-id>");
            return;
        }
        
        discordBotToken = args[0];
        archiveChannelId = args[1];
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (self != null)
                self.shutdown(false);
        }));

        self = new Main();
    }
    
    public static Main getSelf() {
        return self;
    }
    
    private JDA jda;
    
    public Main() {
        self = this;
        
        if (discordBotToken == null || discordBotToken.isEmpty()) {
            System.out.println("Attempted to start with a null or empty Discord bot token!");
            return;
        }
        
        try {
            jda = JDABuilder.createDefault(discordBotToken).enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .setMemberCachePolicy(MemberCachePolicy.ALL).setAutoReconnect(true).build().awaitReady();
        } catch (LoginException | IllegalArgumentException | InterruptedException e) {
            e.printStackTrace();
        }
        
        updateStatus("Starting...");
        jda.addEventListener(new SlashCommandListener());
        
        for (Guild guild : getJDA().getGuilds()) {
            guild.upsertCommand(ArchiveCommand.getArchiveCommandDefinition()).queue();
        }
        
        updateStatus("/archive");
    }
    
    public JDA getJDA() {
        return jda;
    }
    
    private void updateStatus(String str) {
        jda.getPresence().setActivity(Activity.watching(str));
    }
    
    public String getArchiveChannelId() {
        return archiveChannelId;
    }
    
    public void shutdown(boolean reload) {
        updateStatus("Shutting down...");
        jda.shutdown();

        if (reload)
            self = new Main();
    }
}

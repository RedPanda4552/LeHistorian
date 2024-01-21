package io.github.redpanda4552.LeHistorian;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
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
    private Scheduler scheduler;
    
    public Main() {
        self = this;
        
        if (discordBotToken == null || discordBotToken.isEmpty()) {
            System.out.println("Attempted to start with a null or empty Discord bot token!");
            return;
        }
        
        try {
            jda = JDABuilder.createDefault(discordBotToken)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setAutoReconnect(true)
                    .build()
                    .awaitReady();
        } catch (IllegalArgumentException | InterruptedException e) {
            e.printStackTrace();
        }
        
        updateStatus("Starting...");
        this.scheduler = new Scheduler();
        jda.addEventListener(new SlashCommandListener());
        jda.addEventListener(new MessageEventListener());
        jda.addEventListener(new ButtonEventListener());
        
        CommandListUpdateAction update = Main.getSelf().getJDA().updateCommands();
        update.addCommands(ArchiveCommand.getArchiveCommandDefinition());
        update.queue();
        
        updateStatus("/archive");
    }
    
    public JDA getJDA() {
        return jda;
    }

    public Scheduler getScheduler() {
        return this.scheduler;
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

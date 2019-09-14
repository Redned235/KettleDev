package mc.kettle.kettle.craftkettle;

import com.google.common.base.Preconditions;
import mc.kettle.kettle.KettleVersion;
import mc.kettle.kettle.converter.wrapper.WrapperConverter;
import mc.kettle.kettle.craftkettle.help.KettleHelpMap;
import mc.kettle.kettle.craftkettle.scheduler.KettleBukkitScheduler;
import mc.kettle.kettle.util.KettleCollections;
import mc.kettle.kettle.util.KettleText;
import mc.kettle.kettle.util.KettleWrapper;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.*;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpMap;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.CachedServerIcon;
import org.bukkit.util.StringUtil;
import org.bukkit.util.permissions.DefaultPermissions;
import org.spongepowered.api.Game;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.text.channel.MessageChannel;

import java.io.File;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KettleServer extends KettleWrapper<org.spongepowered.api.Server> implements Server {
    private final Game game;
    private final Logger logger;
    private final SimpleCommandMap commandMap;
    private final PluginManager pluginManager;
    private final ServicesManager servicesManager;
    private final Messenger messenger = new StandardMessenger();
    private final Warning.WarningState warnState = Warning.WarningState.DEFAULT;
    private final HelpMap helpMap = new KettleHelpMap();
    private final File pluginsDir = new File(".", "plugins");

    private final BukkitScheduler scheduler = new KettleBukkitScheduler();
    private final BanList profileBans = new KettleUserBanList();
    private final BanList ipBans = new KettleIpBanList();
    // private final ItemFactory itemFactory = new KettleItemFactory();
    private CachedServerIcon icon = null;

    public KettleServer(Game handle, org.slf4j.Logger logger) {
        super(handle.getServer());
        this.game = handle;
        this.logger = Logger.getLogger(logger.getName());
        // this.commandMap = new KettleCommandMap(this);
        this.pluginManager = new SimplePluginManager(this, commandMap);
        this.servicesManager = new SimpleServicesManager();
        Bukkit.setServer(this);
        getFavicon();
        // registerEnchantments()
    }

    public MinecraftServer getServer() {
        return (MinecraftServer) getHandle();
    }

    public Game getGame() {
        return game;
    }

    private void getFavicon() {
        try {
            icon = loadServerIcon(new File("server-icon.png"));
        } catch (Exception e) {
            logger.log(Level.CONFIG, "Could not find the server icon!");
        }
    }

    private static void registerEnchantments() {
//        for (Field field : EnchantmentTypes.class.getFields()) {
//            try {
//                Enchantment.registerEnchantment(new KettleEnchantment());
//            }
//        }

        Enchantment.stopAcceptingRegistrations();
    }

    public void loadPlugins() {
        if (pluginsDir.isDirectory()) {
            pluginManager.clearPlugins();
            pluginManager.registerInterface(JavaPluginLoader.class);

            for (Plugin plugin : pluginManager.loadPlugins(pluginsDir)) {
                try {
                    getLogger().info(String.format("Loading %s", plugin.getDescription().getFullName()));
                    plugin.onLoad();
                } catch (RuntimeException e) {
                    getLogger().log(Level.SEVERE, e.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", e);
                }
            }
        } else {
            if (!pluginsDir.mkdir()) {
                logger.log(Level.SEVERE, "Could not create plugins directory: " + pluginsDir);
            }
        }
    }

    private void loadPlugin(Plugin plugin) {
        for (Permission permission : plugin.getDescription().getPermissions()) {
            try {
                pluginManager.addPermission(permission);
            } catch (IllegalArgumentException e) {
                getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + permission.getName() + "' but it's already registered!", e);
            }
        }

        try {
            pluginManager.enablePlugin(plugin);
        } catch (Throwable e) {
            getLogger().log(Level.SEVERE, e.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)");
        }
    }

    public void enablePlugins(PluginLoadOrder order) {
        if (order == PluginLoadOrder.STARTUP) {
            helpMap.clear();
        }

        for (Plugin plugin : pluginManager.getPlugins()) {
            if (!plugin.isEnabled() && plugin.getDescription().getLoad() == order) {
                loadPlugin(plugin);
            }
        }

        if (order == PluginLoadOrder.POSTWORLD) {
            commandMap.setFallbackCommands();
            commandMap.registerServerAliases();
            DefaultPermissions.registerCorePermissions();
        }
    }

    public void disablePlugins() {
        pluginManager.disablePlugins();
    }

    @Override
    public String getName() {
        return KettleVersion.NAME;
    }

    @Override
    public String getVersion() {
        return KettleVersion.VERSION + "@" + game.getPlatform().getImplementation().getVersion();
    }

    @Override
    public String getBukkitVersion() {
        return KettleVersion.API_VERSION + "@" + game.getPlatform().getApi().getVersion();
    }

    @Override
    public Player[] _INVALID_getOnlinePlayers() {
        Collection<? extends Player> online = getOnlinePlayers();
        return online.toArray(new Player[online.size()]);
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return KettleCollections.transform(getHandle().getOnlinePlayers(), WrapperConverter.<org.spongepowered.api.entity.living.player.Player, KettlePlayer>getConverter());
    }

    @Override
    public int getMaxPlayers() {
        return getHandle().getMaxPlayers();
    }

    @Override
    public int getPort() {
        Optional<InetSocketAddress> address = getHandle().getBoundAddress();
        return address.map(InetSocketAddress::getPort).orElse(-1);
    }

    @Override
    public int getViewDistance() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public String getIp() {
        Optional<InetSocketAddress> address = getHandle().getBoundAddress();
        return address.map(InetSocketAddress::getHostName).orElse("Unknown");
    }

    @Override
    public String getServerName() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public String getServerId() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public String getWorldType() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public boolean getGenerateStructures() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public boolean getAllowEnd() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public boolean getAllowNether() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public boolean hasWhitelist() {
        return getHandle().hasWhitelist();
    }

    @Override
    public void setWhitelist(boolean value) {
        getHandle().setHasWhitelist(value);
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public void reloadWhitelist() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public int broadcastMessage(String message) {
        MessageChannel channel = getHandle().getBroadcastChannel();
        channel.send(KettleText.convert(message));
        return channel.getMembers().size();
    }

    @Override
    public String getUpdateFolder() {
        return "update";
    }

    @Override
    public File getUpdateFolderFile() {
        return new File(pluginsDir, getUpdateFolder());
    }

    @Override
    public long getConnectionThrottle() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Player getPlayer(String name) {
        Preconditions.checkNotNull(name, "name");

        org.spongepowered.api.entity.living.player.Player result = null;
        int delta = Integer.MAX_VALUE;
        for (org.spongepowered.api.entity.living.player.Player player : getHandle().getOnlinePlayers()) {
            if (StringUtil.startsWithIgnoreCase(player.getName(), name)) {
                int newDelta = player.getName().length() - name.length();
                if (newDelta < delta) {
                    result = player;
                    delta = newDelta;
                }

                if (newDelta == 0) {
                    break;
                }
            }
        }

        return result != null ? KettlePlayer.of(result) : null;
    }
}

package mc.kettle.kettle;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.TextTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class Kettle {
    static Kettle instance;

    private final Game game;
    private final Logger logger;
    private final PluginContainer plugin;

    @Inject
    public Kettle(Game game, Logger logger, PluginContainer plugin) {
        checkState(instance == null, "Kettle is already initialized");
        instance = this;

        this.game = checkNotNull(game, "game");
        this.logger = checkNotNull(logger, "logger");
        this.plugin = checkNotNull(plugin, "plugin");
    }

    public static Kettle getInstance() {
        return checkNotNull(instance, "instance");
    }

    public static Game getGame() {
        return getInstance().game;
    }

    public static Logger getLogger() {
        return getInstance().logger;
    }

    public static PluginContainer getPlugin() {
        return getInstance().plugin;
    }

    static Logger testLogger = NOPLogger.NOP_LOGGER;

    public static Logger getTestLogger() {
        return testLogger;
    }

    private static TextTemplate replaceString(String string, String replace, String arg) {
        List<String> formatHeader = Arrays.asList(string.split(replace));
        List<Object> args = new ArrayList<>();
        Iterator<String> iter = formatHeader.iterator();

        while (iter.hasNext()) {
            args.add(iter.next());
            if (iter.hasNext()) {
                args.add(TextTemplate.arg(arg));
            }
        }

        return TextTemplate.of(args.toArray());
    }
}

package mc.kettle.kettle.craftkettle.help;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

import java.util.Collection;
import java.util.List;

public class KettleHelpMap implements HelpMap {
    @Override
    public Collection<HelpTopic> getHelpTopics() {
        return ImmutableSet.of();
    }

    @Override
    public HelpTopic getHelpTopic(String topicName) {
        return null;
    }

    @Override
    public void addTopic(HelpTopic topic) {
    }

    @Override
    public void clear() {
    }

    @Override
    public void registerHelpTopicFactory(Class<?> commandClass, HelpTopicFactory<?> factory) {
    }

    @Override
    public List<String> getIgnoredPlugins() {
        return ImmutableList.of();
    }
}
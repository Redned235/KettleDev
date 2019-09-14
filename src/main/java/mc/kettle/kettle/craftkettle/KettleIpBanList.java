package mc.kettle.kettle.craftkettle;

import org.spongepowered.api.util.ban.Ban;
import org.spongepowered.api.util.ban.BanTypes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Optional;

public final class KettleIpBanList extends KettleBanList<Ban.Ip> {
    @Override
    protected Optional<Ban.Ip> getBanFor(String target) {
        try {
            return getBanService().getBanFor(InetAddress.getByName(target));
        } catch (UnknownHostException e) {
            return Optional.empty();
        }
    }

    @Override
    protected boolean setTarget(Ban.Builder builder, String target) {
        builder.type(BanTypes.IP);
        try {
            builder.address(InetAddress.getByName(target));
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    @Override
    protected Collection<Ban.Ip> getBans() {
        return getBanService().getIpBans();
    }

    @Override
    protected String getTarget(Ban.Ip ban) {
        return ban.getAddress().toString();
    }

    @Override
    public void pardon(String target) {
        try {
            getBanService().pardon(InetAddress.getByName(target));
        } catch (UnknownHostException ignored) {
        }
    }
}

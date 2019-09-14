package mc.kettle.kettle.craftkettle;

import mc.kettle.kettle.Kettle;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.util.ban.Ban;
import org.spongepowered.api.util.ban.BanTypes;

import java.util.Collection;
import java.util.Optional;

public final class KettleUserBanList extends KettleBanList<Ban.Profile> {
    @Override
    protected Optional<Ban.Profile> getBanFor(String target) {
        return getGameProfile(target).flatMap(getBanService()::getBanFor);
    }

    @Override
    protected boolean setTarget(Ban.Builder builder, String target) {
        builder.type(BanTypes.PROFILE);
        Optional<GameProfile> profile = getGameProfile(target);
        if (!profile.isPresent()) {
            return false;
        }
        builder.profile(profile.get());
        return true;
    }

    @Override
    protected Collection<Ban.Profile> getBans() {
        return getBanService().getProfileBans();
    }

    @Override
    protected String getTarget(Ban.Profile ban) {
        return ban.getProfile().getName().get();
    }

    @Override
    public void pardon(String target) {
        getGameProfile(target).ifPresent(getBanService()::pardon);
    }

    private Optional<GameProfile> getGameProfile(String target) {
        return Kettle.getGame().getServiceManager().provideUnchecked(UserStorageService.class).get(target).map(User::getProfile);
    }
}

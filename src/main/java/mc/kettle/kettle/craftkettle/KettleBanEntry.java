package mc.kettle.kettle.craftkettle;

import mc.kettle.kettle.util.KettleText;
import org.bukkit.BanEntry;
import org.spongepowered.api.util.ban.Ban;

import java.util.Date;

final class KettleBanEntry implements BanEntry {
    private final String target;
    private final KettleBanList<?> banList;
    private Date created;
    private String source;
    private Date expiration;
    private String reason;

    KettleBanEntry(String target, KettleBanList<?> banList) {
        this.target = target;
        this.banList = banList;
    }

    <T extends Ban> KettleBanEntry(T ban, KettleBanList<T> banList) {
        this.banList = banList;
        this.target = banList.getTarget(ban);
        this.created = Date.from(ban.getCreationDate());
        this.source = ban.getBanSource().map(KettleText::convert).orElse(null);
        this.expiration = ban.getExpirationDate().map(Date::from).orElse(null);
        this.reason = KettleText.convert(ban.getReason().orElse(null));
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public Date getExpiration() {
        return expiration;
    }

    @Override
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void save() {
        Ban.Builder builder = Ban.builder();
        if (!banList.setTarget(builder, target)) {
            return;
        }
        if (reason != null) {
            builder.reason(KettleText.convert(reason));
        }
        if (expiration != null) {
            builder.expirationDate(expiration.toInstant());
        }
        if (source != null) {
            builder.source(KettleText.convert(source));
        }

        KettleBanList.getBanService().addBan(builder.build());
    }
}

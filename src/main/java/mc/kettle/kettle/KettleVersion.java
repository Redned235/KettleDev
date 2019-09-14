package mc.kettle.kettle;

import com.google.common.base.MoreObjects;

public final class KettleVersion {
    private KettleVersion() {
    }

    public static final String API_NAME = nullToUnknown(getPackage().getSpecificationTitle());
    public static final String API_VERSION = nullToUnknown(getPackage().getSpecificationVersion());
    public static final String NAME = nullToUnknown(getPackage().getImplementationTitle());
    public static final String VERSION = nullToUnknown(getPackage().getImplementationVersion());

    private static Package getPackage() {
        return KettleVersion.class.getPackage();
    }

    private static String nullToUnknown(String s) {
        return MoreObjects.firstNonNull(s, "UNKNOWN");
    }
}

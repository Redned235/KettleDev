package org.spigotmc;

public class SneakyThrow {
    public static void sneaky(Throwable throwable) {
        throw SneakyThrow.<RuntimeException>superSneaky(throwable);
    }

    private static <T extends Throwable> T superSneaky(Throwable t) throws T {
        throw (T) t;
    }
}
package tech.xigam.onelineofcode;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.util.Set;

public final class Config {
    private static final Dotenv environment = Dotenv.load();

    /**
     * Get a value from the config.
     *
     * @param key The key to find.
     * @return String
     */

    public static String get(String key) {
        return environment.get(key);
    }

    /**
     * Get all config entries.
     *
     * @return Set<DotenvEntry>
     */

    public static Set<DotenvEntry> getAll() {
        return environment.entries();
    }
}

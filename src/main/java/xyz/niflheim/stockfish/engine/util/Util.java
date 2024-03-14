package xyz.niflheim.stockfish.engine.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Util {
    private static final Log log = LogFactory.getLog(Util.class);

    public static final String ASSETS_LOCATION = "assets/engines/";
    public static final String ENGINE_FILE_NAME_PREFIX = "stockfish_";
    public static final String ENGINE_FILE_NAME_SUFFIX = "_X64";
    public static final String FILE_MASK = ENGINE_FILE_NAME_PREFIX + "??" + ENGINE_FILE_NAME_SUFFIX + "*";

    public static Map<String, Integer> SUPPORTED_VERSIONS = new TreeMap<>(Comparator.reverseOrder());

    static {
        try {
            try (DirectoryStream<Path> assetsDir = Files.newDirectoryStream(
                    Paths.get(ASSETS_LOCATION), FILE_MASK)) {

                for (Path executable : assetsDir) {
                    log.debug("Supported engines:");
                    log.debug(executable.toString());
                    SUPPORTED_VERSIONS.put(executable.toString(),
                            Integer.parseInt(executable.toString().substring(executable.toString().indexOf(ENGINE_FILE_NAME_PREFIX) + ENGINE_FILE_NAME_PREFIX.length(),
                                    executable.toString().indexOf(ENGINE_FILE_NAME_PREFIX) + ENGINE_FILE_NAME_PREFIX.length() + 2)));
                }
            }
            if (SUPPORTED_VERSIONS.isEmpty()) {
                log.fatal("No engines found. Exiting...");
                System.exit(-1);
            }
        } catch (IOException e) {
            log.fatal("Error accessing assets location. Exiting...");
            System.exit(-1);
        }
    }
}

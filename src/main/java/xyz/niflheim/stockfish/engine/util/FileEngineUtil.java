package xyz.niflheim.stockfish.engine.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishEngineException;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileEngineUtil {
    private static final Log log = LogFactory.getLog(FileEngineUtil.class);

    public static final String ASSETS_LOCATION = "assets/engines/";
    public static final String ENGINE_FILE_NAME_PREFIX = "stockfish_";
    public static final String ENGINE_FILE_NAME_SUFFIX = "_x64";
    public static final String FILE_MASK = ENGINE_FILE_NAME_PREFIX + "??" + ENGINE_FILE_NAME_SUFFIX + "*";

    public static Set<Integer> SUPPORTED_VERSIONS = new TreeSet<>(Comparator.reverseOrder());
    public static Set<Path> SUPPORTED_VERSIONS_PATHS = new TreeSet<>(Comparator.reverseOrder());

    static {

//         void listFilesForFolder(final File folder) {
//            for (final File fileEntry : folder.listFiles()) {
//                if (fileEntry.isDirectory()) {
//                    listFilesForFolder(fileEntry);
//                } else {
//                    System.err.println(fileEntry.getAbsolutePath());
//                }
//            }
//        }

        StringBuilder sb = new StringBuilder("  ");
        try {
            log.fatal("bla!!!");
            try (DirectoryStream<Path> assetsDir = Files.newDirectoryStream(
                    Paths.get(ASSETS_LOCATION))) {
                log.fatal("bla!");
                Pattern pattern = Pattern.compile("[1-9][0-9]");
                log.fatal("Supported engines:");
                for (Path executable : assetsDir) {
                    log.fatal(executable.toAbsolutePath() + " " + Files.exists(executable) + " " + Files.isWritable(executable) + " " +  Files.isReadable(executable) + " " + Files.isExecutable(executable));
                    sb.append(executable + ",");
                    SUPPORTED_VERSIONS_PATHS.add(executable);
                    String mydata = executable.toString();
                    Matcher matcher = pattern.matcher(mydata);
                    if (matcher.find()) {
                        SUPPORTED_VERSIONS.add(Integer.parseInt(matcher.group(0)));
                    }
                }
            }
            if (SUPPORTED_VERSIONS.isEmpty()) {
                log.fatal("Following engines were not found:"
                        + sb.delete(sb.length()-2, sb.length()-1) + "in " + new File("").getAbsolutePath() + "/" + ASSETS_LOCATION + ". Exiting...");

                File folder = new File("");
                System.err.println(folder.getAbsolutePath());
                try (Stream<Path> paths = Files.walk(Paths.get(ASSETS_LOCATION))) {
                    paths
                            .filter(Files::isRegularFile)
                            .forEach((f) -> System.out.println("[" + folder.getAbsolutePath()
                            + " " + Files.isWritable(f) + " " +  Files.isReadable(f) + " " + Files.isExecutable(f) + "]"
                            ));
                }
                System.err.println("------------------------------------------");

                System.exit(-1);
            }
        } catch (IOException e) {
            log.fatal("Error accessing assets location. Exiting...");
            System.exit(-1);
        }
    }



    public static String getPath(Variant variant, String override) {
        return getPath(variant, override, null);
    }

    public static String getPath(Variant variant, String override, Integer requestedEngineVersion) {
        int engineVersion;
        if (requestedEngineVersion != null) {
            if (SUPPORTED_VERSIONS.contains(requestedEngineVersion)) {
                engineVersion = requestedEngineVersion;
            } else {
                engineVersion = SUPPORTED_VERSIONS.iterator().next();
                log.info("Version " + requestedEngineVersion + " not found. Defaulting to highest available: " + engineVersion);
            }
        } else {
            engineVersion = SUPPORTED_VERSIONS.iterator().next();
            log.info("No version was specified. Defaulting to highest available: " + engineVersion);
        }

        StringBuilder path = new StringBuilder(override == null || "".equals(override.trim()) ?
                ASSETS_LOCATION + ENGINE_FILE_NAME_PREFIX + engineVersion + ENGINE_FILE_NAME_SUFFIX :
                override +  ENGINE_FILE_NAME_PREFIX + engineVersion + ENGINE_FILE_NAME_SUFFIX);

        if (System.getProperty("os.name").toLowerCase().contains("win"))
            switch (variant) {
                case DEFAULT:
                    path.append(".exe");
                    break;
                case BMI2:
                    path.append("_bmi2.exe");
                    break;
                case POPCNT:
                    path.append("_popcnt.exe");
                    break;
                default:
                    throw new StockfishEngineException("Illegal variant provided.");
            }
        else
            switch (variant) {
                case DEFAULT:
                    break;
                case BMI2:
                    path.append("_bmi2");
                    break;
                case MODERN:
                    path.append("_modern");
                    break;
                default:
                    throw new StockfishEngineException("Illegal variant provided.");
            }
        log.fatal("------------------------------------------");
        path.insert(0, new File("").getAbsolutePath() + System.getProperty("file.separator"));
        log.fatal(path);

        Path f = Paths.get(path.toString());
        System.out.println("exists 1: " + Files.exists(f));
//        Optional<Path> op = SUPPORTED_VERSIONS_PATHS.stream().filter(p -> p.toAbsolutePath().toString().contains(path.toString())).findFirst();
//        if(!op.isPresent()) {
//            System.out.println("path not present");
//        } else {
//            System.out.println("exists 2: " + Files.exists(op.get()));
//        }

        for (Iterator<Path> iterator = SUPPORTED_VERSIONS_PATHS.iterator(); iterator.hasNext(); ) {
            Path p =  iterator.next();
            System.out.println(p.toAbsolutePath().toString() + "   ?????===   " + path.toString());
            if (p.toAbsolutePath().toString().equals(path.toString())) {
                System.out.println("exists 2: " + path);
                break;
            }

        }


        SUPPORTED_VERSIONS_PATHS.forEach(p -> System.out.println(p.toAbsolutePath() + " " + Files.exists(p)));

        log.fatal("------------------------------------------");

        return path.toString();
    }
}

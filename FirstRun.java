import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FirstRun {

    private static final String firstRunMarker = ".FIRST_RUN_MARKER";

    private static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    private static Path getModsmanRunnerPath(Path instancePath) {
        String scriptName = "modsman-cli";
        if (isWindows())
            scriptName += ".bat";
        Path scriptPath = Paths.get(".modsman", "bin", scriptName);
        return instancePath.resolve(scriptPath).toAbsolutePath().normalize();
    }

    private static Path resolveMinecraftPath(Path instancePath, String... sub) {
        Path modsPath = Paths.get(".minecraft", sub);
        return instancePath.resolve(modsPath).toAbsolutePath().normalize();
    }

    private static Path getFirstRunMarkerPath(Path instancePath) {
        return resolveMinecraftPath(instancePath, firstRunMarker);
    }

    private static String[] buildModsmanCommand(Path instancePath, String targetLine) {
        ArrayList<String> list = new ArrayList<>();
        if (!isWindows())
            list.add("sh");
        list.add(getModsmanRunnerPath(instancePath).toString());
        list.add("-M");
        list.add(resolveMinecraftPath(instancePath, targetLine).toString());
        list.add("reinstall-all");
        return list.toArray(new String[list.size()]);
    }

    private static void log(String s) {
        System.out.println("[FirstRun] " + s);
    }

    public static void main(String[] args) throws Exception {
        Path instancePath = Paths.get(args[0]);

        Path firstRunMarkerPath = getFirstRunMarkerPath(instancePath);
        if (!Files.exists(firstRunMarkerPath))
            return;
        log(firstRunMarker + " found");

        BufferedReader firstRunReader = Files.newBufferedReader(firstRunMarkerPath);
        String firstRunLine;
        while ((firstRunLine = firstRunReader.readLine()) != null) {
            firstRunLine = firstRunLine.trim();
            if (firstRunLine.isEmpty() || firstRunLine.startsWith("#"))
                continue;

            log("attempting to invoke modsman in " + firstRunLine);

            String[] modsmanArgs = buildModsmanCommand(instancePath, firstRunLine);
            log(Arrays.toString(modsmanArgs));

            Process proc = Runtime.getRuntime().exec(modsmanArgs);
            String outputLine;
            BufferedReader output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((outputLine = output.readLine()) != null)
                log(outputLine);
            output.close();

            int exitCode = proc.waitFor();
            proc.destroy();

            if (exitCode != 0) {
                log("install failed; exiting preserving " + firstRunMarker);
                return;
            }
        }

        log("all installs succeeded; deleting " + firstRunMarker);
        Files.delete(getFirstRunMarkerPath(instancePath));
    }
}
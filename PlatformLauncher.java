import java.io.*;
import java.util.*;

public class PlatformLauncher {
    public static void main(String[] args) throws Exception {
        boolean isWindows = System.getProperty("os.name").contains("Windows");

        List<String> finalArgs = new ArrayList<>();
        if (isWindows) {
            finalArgs.add("powershell");
            finalArgs.add("-file");
            finalArgs.add(args[0] + ".ps1");
        } else {
            finalArgs.add("sh");
            finalArgs.add(args[1] + ".sh");
        }
        for (int i = 1; i < args.length; i++) {
            finalArgs.add(args[i]);
        }

        Process proc = new ProcessBuilder(finalArgs).redirectErrorStream(true)
                .directory(new File(System.getProperty("user.dir"))).start();
        BufferedReader output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = output.readLine()) != null)
            System.out.println(line);
        System.exit(proc.waitFor());
    }
}
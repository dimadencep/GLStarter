package ml.dima_dencep.server;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String version = "0.4";
    public static List<String> cmd = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        Config config = new Config(new File("turbostarter.json"));

        //Adding arguments - start
        Log.inf("Starting...");

        if (config.update) {
            new Updater(version);
        }

        cmd.add(config.javaPath);

        if (config.addJarJVMArgs) {
            cmd.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        }

        cmd.addAll(config.jvmArgs);

        cmd.add("-cp");
        cmd.add(config.wrapperjar + File.pathSeparator + config.serverjar);
        cmd.add(config.wrapperclass);

        if (config.addJarArgs) {
            for (String arg : args) {
                cmd.add(arg);
            }
        }

        cmd.addAll(config.args);

        if (config.debug) {
            Log.inf("Check the command: " + cmd.toString().replace("[", "").replace("]", "").replace(",", ""));
        }
        //Adding arguments - end

        ProcessBuilder server_work = new ProcessBuilder(cmd);
        server_work.redirectErrorStream(true);
        server_work.redirectInput(Redirect.INHERIT);
        server_work.redirectOutput(Redirect.INHERIT);
        server_work.redirectError(Redirect.INHERIT);

        Log.inf("Starting server...");

        Process server_process = server_work.start();
        server_process.waitFor();

        Log.inf("Server stopped!");
    }
}


package ml.dima_dencep.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class Updater {
    private final JSONParser parser;

    public Updater(String version) {
        this.parser = new JSONParser();
        Log.inf("Check updates...");
        try {
            JSONObject updates = getUpdates();
            if (updates.get("latest").toString().equals(version)) {
                Log.inf("Updates not found!");
                return;
            } else {
                String lastVers = updates.get("latest").toString();

                JSONObject info = (JSONObject) updates.get(lastVers);
                Log.inf("Found new version: " + lastVers + ", runnung: " + version + "!");
                Log.inf("Last changes: " + info.get("info").toString());

                Log.inf("Wait 20 seconds...");
                Thread.sleep(20000);
                Log.inf("Done!");
                return;
            }

        } catch (Exception e) {
            Log.inf("Failed get updates: " + e.getLocalizedMessage());
        }
    }

    public JSONObject getUpdates() throws Exception {
        InputStream is = new URL("https://raw.githubusercontent.com/dimadencep/GLStarter/main/updates.json").openStream();
        try (InputStreamReader reader = new InputStreamReader(is, Charset.forName("UTF-8"))) {
            JSONObject obj = (JSONObject) parser.parse(reader);
            return obj;
        } finally {
            is.close();
        }
    }
}

package ml.dima_dencep.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class Config {
    public String javaPath = "java";
    public boolean debug = false;
    public String serverjar = "Core.jar";
    public String wrapperjar = "ServerWrapper.jar";
    public String wrapperclass = "pro.gravit.launcher.server.ServerWrapper";
    public boolean update = true;
    public List<String> jvmArgs = Collections.emptyList();
    public List<String> args = Collections.emptyList();
    public boolean addJarArgs = true;
    public boolean addJarJVMArgs = true;

    public Config(File configFile) {
        JSONParser parser = new JSONParser();
        if (!configFile.isDirectory() && configFile.exists()) {
            Log.inf("Read config...");
            try {
                JSONObject data = (JSONObject) parser.parse(new FileReader(configFile.getPath()));
                this.javaPath = data.get("Java").toString();
                this.debug = Boolean.parseBoolean(data.get("debug").toString());
                this.serverjar = data.get("serverjar").toString();
                this.wrapperjar = data.get("wrapperjar").toString();
                this.update = Boolean.parseBoolean(data.get("update").toString());
                this.wrapperclass = data.get("wrapperclass").toString();
                this.jvmArgs = (List<String>) data.get("JVMargs");
                this.args = (List<String>) data.get("args");
                this.addJarArgs = Boolean.parseBoolean(data.get("addJarArgs").toString());
                this.addJarJVMArgs = Boolean.parseBoolean(data.get("addJarJVMArgs").toString());
                check();
            } catch (Exception e) {
                Log.inf("Failed to read config: " + e.getLocalizedMessage());
            }

        } else {
            Log.inf("Create config...");
            FileWriter fileWriter = null;
            try (InputStreamReader reader = new InputStreamReader(Config.class.getResourceAsStream("/turbostarter.json"), StandardCharsets.UTF_8)) {
                JSONObject obj = (JSONObject) parser.parse(reader);
                fileWriter = new FileWriter(configFile);
                fileWriter.write(obj.toJSONString());
            } catch (Exception e) {
                configFile.delete();
                Log.inf("Failed to create config: " + e.getLocalizedMessage());
            }  finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    Log.inf("Failed to save config: " + e.getLocalizedMessage());
                }
            }
        }
    }

    public void check() {
        Log.inf("Checking config...");
        int nulls = 0;
        if (javaPath==null) {
            nulls++;
        }
        if (serverjar==null) {
            nulls++;
        }
        if (wrapperjar==null) {
            nulls++;
        }
        if (wrapperclass==null) {
            nulls++;
        }
        if (jvmArgs==null) {
            nulls++;
        }
        if (args==null) {
            nulls++;
        }

        Log.inf("Found " + nulls + " bad parametrs!");
        if (nulls>=3) {
            Log.inf("PLEASE, UPDATE CONFIG!");
        }
    }

}

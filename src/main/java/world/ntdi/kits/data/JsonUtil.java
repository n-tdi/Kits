package world.ntdi.kits.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import world.ntdi.kits.Kits;

import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private Kits kits;
    private List<Kit> kitList;
    private Gson gson;
    private File file;

    public JsonUtil(Kits kits) {
        this.kits = kits;
        this.gson = new GsonBuilder().disableHtmlEscaping().create();

        if (!kits.getDataFolder().exists()) kits.getDataFolder().mkdirs();
        this.file = new File(kits.getDataFolder(), "data.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readKits() {
        if (file.length() <= 0) {
            kitList = new ArrayList<Kit>();
        } else {
            Type token = new TypeToken<List<Kit>>() {}.getType();
            Reader reader = null;
            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            kitList = gson.fromJson(reader, token);
        }
    }

    public void storeKits() {
        if (!kitList.isEmpty()) {
            Writer writer = null;
            try {
                String json = gson.toJson(kitList);
                writer = new FileWriter(file, false);
                gson.toJson(json, writer);
                writer.flush();
                writer.close();

                kits.getLogger().info("Saved Kits!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Kit> getKits() {
        return kitList;
    }

    public void setKits(List<Kit> kits) {
        this.kitList = kits;
    }

    public  void addKit(Kit kit) {
        kitList.add(kit);
    }
}

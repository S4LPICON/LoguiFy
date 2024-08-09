package ah.s4lpicon.auth;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class PasswordManager {
    private FileConfiguration config;
    private File file;

    public PasswordManager() {
        file = new File("plugins/Auth", "Passwords.yml");
        if (!file.exists()) {
            // Create the file if it does not exist
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    // Method to add a password
    public void addPassword(String playerName, String password) {
        config.set("players." + playerName, password);
        saveConfig();
    }

    // Method to save changes to the file
    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to verify a password
    public boolean verifyPassword(String playerName, String password) {
        String storedPassword = config.getString("players." + playerName);
        return password.equals(storedPassword);
    }

    public boolean playerExists(String playerName) {
        return config.getString("players." + playerName) != null;
    }
}

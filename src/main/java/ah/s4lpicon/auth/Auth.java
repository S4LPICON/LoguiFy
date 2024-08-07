package ah.s4lpicon.auth;

import org.bukkit.plugin.java.JavaPlugin;
import ah.s4lpicon.auth.CommandsManager;
import org.bukkit.plugin.PluginManager;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
public final class Auth extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("logueo").setExecutor(new CommandsManager());
        // Registrar eventos
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Manager(), this);
        pm.registerEvents(new LoginManager(), this);
        saveResource("Passwords.yml", false);//guardar el archivo de vaina
        getLogger().info("El plugin se ha habilitado correctamente");
    }
    // Método para guardar un recurso desde el JAR del plugin
    @Override
    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + getFile());
        }

        File outFile = new File(getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                Files.copy(in, outFile.toPath());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("El plugin se ha deshabilitado correctamente");
    }
}
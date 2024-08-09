package ah.s4lpicon.auth;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginManager implements Listener {

    private boolean loggedIn;
    private boolean isLogin;

    private PasswordManager passwordManager;

    private Player player;
    private Inventory chestInventory;

    private ArrayList<Integer> password;
    private List<Integer> itemSlots = new ArrayList<>(Arrays.asList(
            15, 16, 17, 18, 19, 21, 22, 24, 25, 26,
            27, 28, 30, 31, 33, 34, 35, 6, 7, 8));

    private List<Integer> customModelData = new ArrayList<>(Arrays.asList(
            15, 16, 17, 18, 19, 21, 22, 24, 25, 26,
            27, 28, 30, 31, 33, 34, 35, 6, 7, 8));

    private List<String> names = new ArrayList<>(Arrays.asList(
            "1", "2", "3", "Log In", "Log In",
            "Register", "Register", "4", "5", "6",
            "Log In", "Log In", "Register", "Register", "7",
            "8", "9", "Delete Digit", "0", "Submit"));

    @Override
    public String toString() {
        return "LoginManager{" +
                "\nloggedIn=" + loggedIn +
                ", \nisLogin=" + isLogin +
                ", \npasswordManager=" + passwordManager +
                ", \nplayer=" + player +
                ", \nchestInventory=" + chestInventory +
                ", \npassword=" + password +
                ", \nitemSlots=" + itemSlots +
                ", \ncustomModelData=" + customModelData +
                ", \nnames=" + names +
                "\n}";
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isLoggingIn() {
        return isLogin;
    }

    public void setLoggingIn(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public LoginManager(Player player) {
        password = new ArrayList<>();
        passwordManager = new PasswordManager();
        this.player = player;
    }

    public void addDigit(int num) {
        if (password.size() < 5) {
            password.add(num);
            updatePasswordDisplay();
        }
    }

    public void changeInventory(Player player, String name) {
        password.clear();
        // Close the old inventory
        player.closeInventory();
        // Create a new inventory with the new name (one-liner)
        openLoginInventory(player, name);
    }

    public void deleteDigit() {
        if (password.size() > 0 && password.get(0) != null)
            password.remove(password.size() - 1);
        chestInventory.clear(password.size() + 2);
    }

    @SuppressWarnings("deprecation")
    public void updatePasswordDisplay() {
        for (int i = 0; i < password.size(); i++) {
            if (password.get(i) != null) {
                ItemStack item = new ItemStack(Material.PRISMARINE_SHARD, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.WHITE + "" + password.get(i));
                meta.setCustomModelData(customModelData.get(i));
                item.setItemMeta(meta);
                chestInventory.setItem(i + 2, item);
            }
        }
    }

    public boolean sendAndVerifyPassword(Player player) {
        if (password.size() == 5) {
            // Send and verify the password

            if (isLogin) {
                // Logging in
                if (passwordManager.verifyPassword(player.getName(), password.toString())) {
                    player.sendMessage("Successfully Logged In");
                    reset();
                    // TELEPORT TO THE GAME (TESTING)
                    World world = player.getWorld(); // Get the player's current world

                    // Create a new location with the specified coordinates
                    Location location = new Location(world, player.getX(), player.getY() + 7, player.getZ());
                    player.teleport(location);
                    // END OF TESTING
                    return true;
                } else {
                    player.sendMessage("Incorrect Password");
                    reset();
                    return false;
                }

            } else {
                // Registering
                // Attempt to verify if the user already exists to prevent re-registration
                if (!passwordManager.playerExists(player.getName())) {
                    passwordManager.addPassword(player.getName(), password.toString());
                    player.sendMessage("Player Successfully Registered!");
                    reset();
                    // TELEPORT TO THE GAME
                    return true;
                } else {
                    player.sendMessage("This Player Has Already Been Registered");
                    reset();
                    return false;
                }
            }
        } else {
            player.sendMessage("The password must have 5 digits.");
        }
        return false;
    }

    public void reset() {
        password.clear();
        chestInventory.close();
    }

    @SuppressWarnings("deprecation")
    public void openLoginInventory(Player player, String name) {
        if (name == null) {
            name = "Select an Option";
        }
        chestInventory = Bukkit.createInventory(null, 9, name);
        player.openInventory(chestInventory);

        for (int i = 0; i < itemSlots.size(); i++) {
            ItemStack item = new ItemStack(Material.PRISMARINE_SHARD, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + names.get(i));
            meta.setCustomModelData(customModelData.get(i));
            item.setItemMeta(meta);
            player.getInventory().setItem(itemSlots.get(i), item);
        }
    }
}

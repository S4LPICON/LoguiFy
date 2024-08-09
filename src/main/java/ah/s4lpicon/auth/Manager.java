package ah.s4lpicon.auth;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class Manager implements Listener {

    private static ArrayList<LoginManager> users = new ArrayList<>();

    //hola

    public static String getAllDebugInfo(String name) {
        String info = "All Users: " + users.toString() +
                "\n Is Logged In?: " + users.get(findUser(name)).isLoggedIn() +
                "\n Is Logging In?: " + users.get(findUser(name)).isLoggingIn();
        return info;
    }

    public static void openInventory(Player player) {
        int index = findUser(player.getName());
        if (index != -1 && index < users.size()) {
            users.get(index).openLoginInventory(player, null);
        } else {
            player.kick();
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LoginManager loginManager = new LoginManager(player);

        users.add(loginManager);

        player.setWalkSpeed(0f); // Disable movement
        player.setFlySpeed(0f); // Disable flight

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

        // Prevent the default join message from being sent
        event.setJoinMessage(null);

        // // Specific coordinates to teleport the player to
        // double x = 8.5;
        // double y = -58.0;
        // double z = 7.5;
        // World world = event.getPlayer().getWorld(); // Get the player's current world

        // // Create a new location with the specified coordinates
        // Location location = new Location(world, x, y, z);

        // Teleport the player to the new location
        // event.getPlayer().teleport(location);

        users.get(findUser(player.getName())).openLoginInventory(event.getPlayer(), null);
    }

    public static int findUser(String name) {
        for (LoginManager user : users) {
            if (Objects.equals(user.getPlayerName(), name)) {
                return users.indexOf(user);
            }
        }
        return -1;
    }

    public void removeUser(String name) {
        users.remove(findUser(name));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        player.getInventory().clear();
        int userIndex = findUser(player.getName());
        if (userIndex != -1) {
            if (users.get(userIndex).isLoggedIn()) {
                users.remove(userIndex);
            }
        }
    }

    public void addDigit(int digit, int position) {
        users.get(position).addDigit(digit);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        int userIndex = findUser(player.getName());
        if (userIndex != -1) {
            users.remove(userIndex);
        }
        // You can also clear inventories, potion effects, etc.
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTitle().equals("Select an Option")
                || event.getView().getTitle().equals("Logging In")
                || event.getView().getTitle().equals("Registering")) {

            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true); // Prevent items from being moved

            int position = findUser(player.getName());
            int slot = event.getSlot();
            if (slot == 18 || slot == 19 || slot == 27 || slot == 28) {
                // Logging In
                users.get(position).setLoggingIn(true);
                users.get(position).changeInventory(player, "Logging In");
            } else if (slot == 21 || slot == 22 || slot == 30 || slot == 31) {
                // Registering
                users.get(position).setLoggingIn(false);
                users.get(position).changeInventory(player, "Registering");
            }
            if (event.getView().getTitle().equals("Logging In")
                    || event.getView().getTitle().equals("Registering")) {

                ItemStack clickedItem = event.getCurrentItem();

                if (clickedItem != null && clickedItem.getType() != null) {

                    switch (event.getSlot()) {
                        case 15:
                            addDigit(1, position);
                            break;
                        case 16:
                            addDigit(2, position);
                            break;
                        case 17:
                            addDigit(3, position);
                            break;
                        case 24:
                            addDigit(4, position);
                            break;
                        case 25:
                            addDigit(5, position);
                            break;
                        case 26:
                            addDigit(6, position);
                            break;
                        case 33:
                            addDigit(7, position);
                            break;
                        case 34:
                            addDigit(8, position);
                            break;
                        case 35:
                            addDigit(9, position);
                            break;
                        case 6:
                            users.get(position).deleteDigit();
                            break;
                        case 7:
                            addDigit(0, position);
                            break;
                        case 8:
                            if (users.get(position).sendAndVerifyPassword(player)) {
                                removeUser(player.getName());
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}

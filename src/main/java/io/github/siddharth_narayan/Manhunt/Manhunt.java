package io.github.siddharth_narayan.Manhunt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.attribute.Attribute;

public final class Manhunt extends JavaPlugin implements Listener {

	// define needed variables
	ArrayList<Player> speedrunners;
	ArrayList<Player> hunters;
	Server server = getServer();
	Collection<? extends Player> allPlayers = server.getOnlinePlayers();

	// remeber that it was like this <Player, Location> before if anything goes
	// wrong
	Map<Player, Location> initialSpeedrunnerLocations = new HashMap<>();

	// Message plugin as activated and register for events
	@Override
	public void onEnable() {
		getLogger().info("Plugin activated!");
		getServer().getPluginManager().registerEvents(this, this);
	}

	// Message plugin as deactivated
	@Override
	public void onDisable() {
		getLogger().info("Plugin deactivated :(");
	}

	// distance function
	private static double distance(double L1x, double L1y, double L2x, double L2y) {
		return Math.sqrt((L1x - L2x) * (L1x - L2x) + (L1y - L2y) * (L1y - L2y));
	}

	// Receive command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// info for debugging purposes
		getLogger().info("sender was " + sender.getName());
		getLogger().info("command was " + command.getName());
		getLogger().info("label was " + label);
		getLogger().info("there was/were " + Integer.toString(args.length) + " argument(s)");
		for (int counter = 0; counter < args.length; counter++) {
			getLogger().info(args[counter]);
		}

		// execute code if command is /manhunt
		if (command.getName().equals("manhunt")) {

			// set PvP off and set collision off
			Bukkit.getPlayer(sender.getName()).getWorld().setPVP(false);
			for (Player player : allPlayers) {
				player.setCollidable(false);
			}

			// Set player 	to full health and clear any items
			for (Player player : Bukkit.getPlayer(sender.getName()).getWorld().getPlayers()) {
				// player.getInventory().clear();
				player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			}

			// Set speedrunners as players, defined by arguements
			// remember that it used to be ArrayList<Player>
			speedrunners = new ArrayList<>();
			for (int counter = 0; counter < args.length; counter++) {
				speedrunners.add(server.getPlayerExact(args[counter]));
			}

			// Set hunters as all players not speedrunners
			// remember that it used to be ArrayList<Player>
			hunters = new ArrayList<>();
			for (Player player : allPlayers) {
				if (!speedrunners.contains(player)) {
					hunters.add(player);
				}
			}

			// Give hunters compasses
			for (Player hunter : hunters) {
				hunter.getInventory().setItem(8, new ItemStack(Material.COMPASS, 1));
			}

			// Log the starting location of all speedrunners
			for (Player speedrunner : speedrunners) {
				initialSpeedrunnerLocations.put(speedrunner, speedrunner.getLocation());
			}
		}

		return true;

	}

	// Update compasses when player moves, also start game if pvp is set to false
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMoveEvent(PlayerMoveEvent moveEvent) {

		// this if is only here to stop null errors from being thrown if the roles have
		// not been assigned yet
		if (hunters != null && speedrunners != null) {

			// getting the player who moved, and check if it is a speedrunner
			// if the player is a speedrunner, and the game has not started (pvp set to
			// false), then check distance
			// if distance is more than one block, start the game
			Player playerMoved = moveEvent.getPlayer();

			if ((!playerMoved.getWorld().getPVP()) && (speedrunners.contains(playerMoved))) {
				Location endingLocation = moveEvent.getTo();
				Location initialLocation = initialSpeedrunnerLocations.get(playerMoved);

				if (distance(initialLocation.getX(), initialLocation.getY(), endingLocation.getX(),
						endingLocation.getY()) > 1) {
					playerMoved.getWorld().setPVP(true);

					for (Player player : allPlayers) {
						player.setCollidable(true);
					}
				}
			}

			/*
			 * for (World world : worlds) { if (!world.getPVP() &&
			 * speedrunners.contains(playerMoved)) { getLogger().info("move");
			 * 
			 * Location endingLocation = moveEvent.getTo(); Location initialLocation =
			 * initialLocations.get(playerMoved);
			 * 
			 * if (distance(initialLocation.getX(), endingLocation.getX(),
			 * initialLocation.getY(), endingLocation.getY()) > 1) { world.setPVP(true); } }
			 */

			// find the closest speedrunner for each hunter
			// set the compass to the closest speedruner
			for (Player hunter : hunters) {
				if(!hunter.isDead()){

					Location hunterLocation = hunter.getLocation();
					double shortestDistance = 0;
					Player closestPlayer = null;

					for (Player speedrunner : speedrunners) {

						if (hunter.getWorld().getEnvironment() == speedrunner.getWorld().getEnvironment()) {
							Location speedrunnerlocation = speedrunner.getLocation();

							// distance formula, shortest distance and closest player checker
							double distance = distance(hunterLocation.getX(), speedrunnerlocation.getX(),
									hunterLocation.getY(), speedrunnerlocation.getY());
							if (shortestDistance == 0 || distance < shortestDistance) {
								shortestDistance = distance;
								closestPlayer = speedrunner;
							}
						}
					}

					// hunter.setCompassTarget(closestPlayer.getLocation());
					if (closestPlayer != null) {
						int compassSlot = hunter.getInventory().first(Material.COMPASS);
						
						if(!(compassSlot == -1)){
							ItemStack compass = hunter.getInventory().getItem(compassSlot);
							CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
							compassMeta.setLodestone(closestPlayer.getLocation());
							compassMeta.setLodestoneTracked(false);
							compass.setItemMeta(compassMeta);
						}

					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {

		if ((hunters != null) && (hunters.contains(event.getPlayer()))) {
			event.getPlayer().getInventory().setItem(8, new ItemStack(Material.COMPASS, 1));
		}

		if ((speedrunners != null) && (speedrunners.contains(event.getPlayer()))){
			speedrunners.remove(event.getPlayer());
			event.getPlayer().setGameMode(GameMode.SPECTATOR);
		}
	}

}

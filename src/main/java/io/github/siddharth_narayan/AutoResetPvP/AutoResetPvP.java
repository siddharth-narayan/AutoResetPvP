package io.github.siddharth_narayan.AutoResetPvP;


import java.util.Collection;
import java.util.Iterator;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;


public final class AutoResetPvP extends JavaPlugin {

	//Message plugin as activated
	@Override
	public void onEnable() {
		getLogger().info("Plugin activated!");
	}
	
	//Message plugin as deactivated
	@Override
	public void onDisable() {
		getLogger().info("Plugin deactivated :(");
	}
	
	//Receive command, figure out what it is, and if it /reset, execute command
	//debug commands
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		getLogger().info("sender was " + sender.getName());
		getLogger().info("command was " + command.getName());
		getLogger().info("label was " + label);
		getLogger().info("there was/were " + Integer.toString(args.length) + " argument(s)");
		for (int counter = 0; counter < args.length; counter++) {
			getLogger().info(args[counter]);
		}
		
		//execute code if command is /reset
		if(command.getName().equals("reset")) {
			getLogger().info("resetting inventory right now");
			Server server = getServer();
			Collection<? extends Player> players = server.getOnlinePlayers();
			
			//find all players, modify inventory
			Iterator<? extends Player> playerator = players.iterator();
			while(playerator.hasNext()) {
				Player player = playerator.next();
				PlayerInventory playerInventory = player.getInventory();
				
				//clear all inventory
				playerInventory.clear();	
				player.getInventory().setArmorContents(null);

				//heal all players
				AttributeInstance playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
				player.setHealth(playerMaxHealth.getValue());
				
				//give player items
				//decide argument, and give items according to it
				if (args.length > 0) { 

					if (args[0].equals("leather")) {
						ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
						ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
						ItemStack leatherPants = new ItemStack(Material.LEATHER_LEGGINGS);
						ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
						player.getInventory().setHelmet(leatherHelmet);
						player.getInventory().setChestplate(leatherChestplate);
						player.getInventory().setLeggings(leatherPants);
						player.getInventory().setBoots(leatherBoots);
						player.getInventory().addItem(new ItemStack(Material.WOOD_AXE, 1));
						player.getInventory().addItem(new ItemStack(Material.SHIELD));
					}
					
					if (args[0].equals("chainmail")) {
						ItemStack chainmailHelmet = new ItemStack(Material.CHAINMAIL_HELMET);
						ItemStack chainmailChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
						ItemStack chainmailPants = new ItemStack(Material.CHAINMAIL_LEGGINGS);
						ItemStack chainmailBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
						player.getInventory().setHelmet(chainmailHelmet);
						player.getInventory().setChestplate(chainmailChestplate);
						player.getInventory().setLeggings(chainmailPants);
						player.getInventory().setBoots(chainmailBoots);
						player.getInventory().addItem(new ItemStack(Material.STONE_AXE, 1));
						player.getInventory().addItem(new ItemStack(Material.SHIELD));
					}
					
					if (args[0].equals("iron") ) {
						ItemStack ironHelmet = new ItemStack(Material.IRON_HELMET);
						ItemStack ironChestplate = new ItemStack(Material.IRON_CHESTPLATE);
						ItemStack ironPants = new ItemStack(Material.IRON_LEGGINGS);
						ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
						player.getInventory().setHelmet(ironHelmet);
						player.getInventory().setChestplate(ironChestplate);
						player.getInventory().setLeggings(ironPants);
						player.getInventory().setBoots(ironBoots);
						player.getInventory().addItem(new ItemStack(Material.IRON_AXE, 1));
						player.getInventory().addItem(new ItemStack(Material.SHIELD));
					}
					
					if (args[0].equals("diamond") ) {
						ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET);
						ItemStack diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
						ItemStack diamondPants = new ItemStack(Material.DIAMOND_LEGGINGS);
						ItemStack   diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
						player.getInventory().setHelmet(diamondHelmet);
						player.getInventory().setChestplate(diamondChestplate);
						player.getInventory().setLeggings(diamondPants);
						player.getInventory().setBoots(diamondBoots);
						player.getInventory().addItem(new ItemStack(Material.DIAMOND_AXE, 1));
						player.getInventory().addItem(new ItemStack(Material.SHIELD));
					}
					
					//player.getInventory().addItem(new ItemStack(Material.WOOD, 32));
					//player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
					//ItemStack healthPotion = new ItemStack(Material.POTION);
					//healthPotion.setDurability((short) 16421); // Healing 2
					//player.getInventory().addItem(new ItemStack(healthPotion));
					player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
				}
			
			}
			
		

		}
		return true;
		
	}
}
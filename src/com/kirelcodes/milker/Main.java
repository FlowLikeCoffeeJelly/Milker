package com.kirelcodes.milker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	
	private static Main _instance = null;

	@Override
	public void onEnable()
	{
		_instance = this;
		if(!Constants.init())
		{
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldn't load constants");
		}
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(label.equalsIgnoreCase("milker"))
		{
			//Been awhile since I used this method for commands
			if(!(sender instanceof Player))
			{
				sender.sendMessage(ChatColor.RED + "This command is for players only");
				return false;
			}
			Player player = (Player) sender;
			MilkMan pand = new MilkMan(player.getLocation());
			if(args.length == 2)
			{
				pand.setCustomName(args[1]);
			}
		}
		return super.onCommand(sender, command, label, args);
	}
	
	public static Main getInstance()
	{
		return _instance;
	}
	
}

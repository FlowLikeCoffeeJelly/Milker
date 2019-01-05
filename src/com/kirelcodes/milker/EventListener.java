package com.kirelcodes.milker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.kirelcodes.miniaturepets.api.events.mobs.MobDeathEvent;

public class EventListener implements Listener
{
	@EventHandler
	public void mobDeath(MobDeathEvent e)
	{
		if(!(e.getMob() instanceof MilkMan))
			return;
		e.getDrops().clear();
		e.setDroppedExp(20);
		ItemStack[] items = ((MilkMan)e.getMob()).getInventory().getContents();
		for(int i =0; i <items.length; ++i)
		{
			if(items[i] != null)
			{
				e.getDrops().add(items[i].clone());
			}
		}
	}
}

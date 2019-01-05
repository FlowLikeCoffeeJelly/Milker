package com.kirelcodes.milker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.kirelcodes.milker.paths.CowPathfinder;
import com.kirelcodes.milker.paths.MoneyPathfinder;
import com.kirelcodes.miniaturepets.mob.Mob;

public class MilkMan extends Mob implements InventoryHolder
{
	
	private Inventory _inventory;
	
	public MilkMan(Location location)
	{
		super(location, Constants.getMilkerContainer());
		_inventory = Bukkit.createInventory(this, 9* 3);
		clearNavigation();
		getPathManager().addPathfinder(new MoneyPathfinder(this));
		getPathManager().addPathfinder(new CowPathfinder(this));
	}
	
	@Override
	protected void afterTick()
	{
		super.afterTick();
	}

	@Override
	public Inventory getInventory()
	{
		return _inventory;
	}

	
	
}

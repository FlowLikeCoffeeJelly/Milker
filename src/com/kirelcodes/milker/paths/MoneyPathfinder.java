package com.kirelcodes.milker.paths;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.kirelcodes.milker.MilkMan;
import com.kirelcodes.miniaturepets.MiniaturePets;
import com.kirelcodes.miniaturepets.mob.pathfinding.Pathfinder;

public class MoneyPathfinder extends Pathfinder
{

	private MilkMan _milkMan;
	private Item _targetItem = null;

	public MoneyPathfinder(MilkMan milker)
	{
		_milkMan = milker;
	}
	
	@Override
	public boolean shouldStart()
	{
		return hasMilk() && ((_targetItem != null && !_targetItem.isDead()) || scanForIngot());
	}

	private boolean hasMilk()
	{
		boolean gotMilk = false;
		Inventory inv = _milkMan.getInventory();
		ItemStack[] content = inv.getContents();
		for(int i = 0; i < content.length && !gotMilk; ++i)
		{
			if(content[i] != null && content[i].getType() == Material.MILK_BUCKET)
				gotMilk = true;
		}
		return gotMilk;
	}
	
	private boolean removeMilk()
	{
		boolean gotMilk = false;
		Inventory inv = _milkMan.getInventory();
		ItemStack[] content = inv.getContents();
		for(int i = 0; i < content.length && !gotMilk; ++i)
		{
			if(content[i] != null && content[i].getType() == Material.MILK_BUCKET)
			{
				gotMilk = true;
				content[i] = null;
			}
			
		}
		return gotMilk;
	}

	
	private boolean scanForIngot()
	{
		for (Entity e : _milkMan.getNavigator().getNearbyEntities(10, 10, 10))
		{

			if (!(e instanceof Item))
				continue;

			if (!e.isOnGround())
				continue;

			Item item = (Item) e;
			if (item.getItemStack().getType() == Material.GOLD_INGOT)
			{
				this._targetItem = item;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean keepWorking()
	{
		if(_targetItem == null || _targetItem.isDead() || !hasMilk())
		{
			try
			{
				_milkMan.stopPathfinding();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_targetItem = null;
		}
		return hasMilk() && _targetItem != null && !_targetItem.isDead();
	}

	@Override
	public void onStart()
	{
		try
		{
			_milkMan.stopPathfinding();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean canBeInterrupted()
	{
		return false;
	}

	@Override
	public void updateTask()
	{
		if(_targetItem == null || _targetItem.isDead())
		{
			_targetItem = null;
			return;
		}
		if(_milkMan.getNavigator().getLocation().distanceSquared(_targetItem.getLocation()) <= 1)
		{
			_targetItem.remove();
			_milkMan.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET));
			if(removeMilk())
				_milkMan.getLocation().getWorld().dropItemNaturally(_milkMan.getLocation(), new ItemStack(Material.MILK_BUCKET));
			_targetItem = null;
			return;
		}
		try
		
		{
			_milkMan.setTargetLocation(_targetItem.getLocation());
		}
		catch(Exception e)
		{
			if(MiniaturePets.isDebug())
				e.printStackTrace();
		}
	}

}

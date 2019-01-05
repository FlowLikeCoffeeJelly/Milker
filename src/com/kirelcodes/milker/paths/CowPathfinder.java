package com.kirelcodes.milker.paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.kirelcodes.milker.MilkMan;
import com.kirelcodes.miniaturepets.MiniaturePets;
import com.kirelcodes.miniaturepets.mob.pathfinding.Pathfinder;

public class CowPathfinder extends Pathfinder
{
	private MilkMan _milkMan;
	private Cow _targetCow;
	private Map<Cow, Integer> _cooldownMap;

	public CowPathfinder(MilkMan milker)
	{
		_milkMan = milker;
		_cooldownMap = new HashMap<>();
	}

	
	
	@Override
	public boolean shouldStart()
	{
		return (_targetCow != null && !_targetCow.isDead()) || scanForCows();
	}

	private boolean scanForCows() {
		for (Entity e : _milkMan.getNavigator().getNearbyEntities(10, 10, 10)) {
			if (!(e instanceof Cow))
				continue;
			if (!e.isOnGround())
				continue;
			if (onCooldown((Cow) e))
				continue;
			this._targetCow = (Cow) e;
			return true;
		}
		return false;
	}

	
	@Override
	public boolean keepWorking()
	{
		if(_targetCow == null || _targetCow.isDead())
		{
			try
			{
				_milkMan.stopPathfinding();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			_targetCow = null;
		}
		return _targetCow != null && !_targetCow.isDead();
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

	
	private void startCooldown(Cow cow) {
		_cooldownMap.put(cow, 75 * 2);
	}

	private boolean onCooldown(Cow cow) {
		return _cooldownMap.containsKey(cow);
	}

	@Override
	public void afterTask() {
		List<Cow> removeCows = new ArrayList<>();
		for (Entry<Cow, Integer> entry : _cooldownMap.entrySet()) {
			_cooldownMap.replace(entry.getKey(), entry.getValue() - 1);
			if (_cooldownMap.get(entry.getKey()) < 0)
				removeCows.add(entry.getKey());
		}
		for(Cow cow : removeCows)
			_cooldownMap.remove(cow);
	}

	@Override
	public void updateTask()
	{
		if(_targetCow == null || _targetCow.isDead())
		{
			_targetCow = null;
			return;
		}
		if(_milkMan.getNavigator().getLocation().distanceSquared(_targetCow.getLocation()) <= 1)
		{
			_milkMan.getInventory().addItem(new ItemStack(Material.MILK_BUCKET));
			startCooldown(_targetCow);
			_targetCow = null;
			return;
		}
		try
		
		{
			_milkMan.setTargetLocation(_targetCow.getLocation());
		}
		catch(Exception e)
		{
			if(MiniaturePets.isDebug())
				e.printStackTrace();
		}
	}

}

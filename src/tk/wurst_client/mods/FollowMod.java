/*
 * Copyright � 2014 - 2016 | Wurst-Imperium | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.entity.Entity;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Bypasses;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.EntityUtils;
import tk.wurst_client.utils.EntityUtils.TargetSettings;

@Info(
	description = "A bot that follows the closest entity.\n" + "Very annoying.",
	name = "Follow",
	help = "Mods/Follow")
@Bypasses(ghostMode = false)
public class FollowMod extends Mod implements UpdateListener
{
	private Entity entity;
	private float range = 12F;
	private float distance = 1F;
	
	private TargetSettings targetSettingsFind = new TargetSettings()
	{
		@Override
		public boolean targetFriends()
		{
			return true;
		}
		
		@Override
		public float getRange()
		{
			return range;
		}
	};
	
	private TargetSettings targetSettingsKeep = new TargetSettings()
	{
		@Override
		public boolean targetFriends()
		{
			return true;
		}
		
		@Override
		public boolean targetBehindWalls()
		{
			return true;
		};
		
		@Override
		public boolean targetPlayers()
		{
			return true;
		}
		
		@Override
		public boolean targetAnimals()
		{
			return true;
		}
		
		@Override
		public boolean targetMonsters()
		{
			return true;
		}
		
		@Override
		public boolean targetGolems()
		{
			return true;
		}
		
		@Override
		public boolean targetSleepingPlayers()
		{
			return true;
		}
		
		@Override
		public boolean targetInvisiblePlayers()
		{
			return true;
		}
		
		@Override
		public boolean targetInvisibleMobs()
		{
			return true;
		}
		
		@Override
		public boolean targetTeams()
		{
			return false;
		}
	};
	
	@Override
	public String getRenderName()
	{
		if(entity != null)
			return "Following " + entity.getName();
		else
			return "Follow";
	}
	
	@Override
	public void onEnable()
	{
		entity = EntityUtils.getClosestEntity(targetSettingsFind);
		wurst.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		// check if player died, entity died or entity disappeared
		if(mc.player.getHealth() <= 0
			|| !EntityUtils.isCorrectEntity(entity, targetSettingsKeep))
		{
			entity = null;
			setEnabled(false);
			return;
		}
		
		// jump if necessary
		if(mc.player.isCollidedHorizontally && mc.player.onGround)
			mc.player.jump();
		
		// swim up if necessary
		if(mc.player.isInWater() && mc.player.posY < entity.posY)
			mc.player.motionY += 0.04;
		
		// follow entity
		EntityUtils.faceEntityClient(entity);
		mc.gameSettings.keyBindForward.pressed =
			mc.player.getDistanceToEntity(entity) > distance;
	}
	
	@Override
	public void onDisable()
	{
		wurst.events.remove(UpdateListener.class, this);
		if(entity != null)
			mc.gameSettings.keyBindForward.pressed = false;
	}
	
	public void setEntity(Entity entity)
	{
		this.entity = entity;
	}
}

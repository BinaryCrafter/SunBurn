package org.walterweight.sunburn;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.Random;

public class PlayerEventHandler
{


	Random random = new Random();

	@SubscribeEvent
	public void LivingUpdateEvent(LivingEvent.LivingUpdateEvent event)
	{
		Entity entity = event.entity;

		if (!(entity instanceof EntityPlayer))
			return;

		EntityPlayer entityPlayer = (EntityPlayer) entity;

		if (entityPlayer.ticksExisted < SunBurn.ticksBeforeAffectedBySunburn)
			return;

		World world = entity.worldObj;

		if (world.isDaytime() && !world.isRemote)
		{
			float brightness = entity.getBrightness(1.0F);

			if (brightness <= SunBurn.sunLightRequiredToCatchFire)
				return;

			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.posY);
			int z = MathHelper.floor_double(entity.posZ);

			if (!world.canBlockSeeTheSky(x, y, z))
				return;

			if (SunBurn.helmetProtectsAgainstSun && helmetOffersProtection((EntityPlayer)entity))
				return;

			if (random.nextFloat()*100F >= SunBurn.chanceOfCatchingFirePerTick)
				return;

			entity.setFire(SunBurn.amountOfDamage);

		}
	}


	private boolean helmetOffersProtection(EntityPlayer entity)
	{
		ItemStack helmet = entity.getCurrentArmor(3);
		if (helmet == null)
			return false;

		if (!helmet.isItemStackDamageable())
			return true;

		if (SunBurn.helmetRequiresFireProtection && !helmetHasFireProtection(helmet))
			return false;

		if (random.nextInt(100) > SunBurn.chanceOfHelmetTakingDamagePerTick)
			return true;

		helmet.setItemDamage(helmet.getItemDamageForDisplay() + 1);

		if (helmet.getItemDamageForDisplay() < helmet.getMaxDamage())
			return true;

		entity.renderBrokenItemStack(helmet);
		entity.setCurrentItemOrArmor(4, (ItemStack) null);
		return false;
	}


	private boolean helmetHasFireProtection(ItemStack helmet)
	{
		NBTTagList enchantments = helmet.getEnchantmentTagList();
		if (enchantments == null)
			return false;

		for (int loop = 0; loop <enchantments.tagCount(); loop++)
		{
			NBTTagCompound enchantment = enchantments.getCompoundTagAt(loop);
			if (enchantment.getInteger("id") == 1)
				return true;
		}
		return false;
	}
}

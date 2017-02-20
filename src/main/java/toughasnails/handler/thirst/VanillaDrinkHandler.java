/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.handler.thirst;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.thirst.ThirstHandler;

public class VanillaDrinkHandler 
{
    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            ItemStack stack = player.getHeldItem(player.getActiveHand());
            ThirstHandler thirstHandler = (ThirstHandler)ThirstHelper.getThirstData(player);

            if (thirstHandler.isThirsty())
            {
                // For some reason the stack size can be zero for water bottles, which breaks everything.
                // As a workaround, we temporarily set it to 1
                boolean zeroStack = false;

                if (stack.getCount() <= 0)
                {
                    stack.setCount(1);
                    zeroStack = true;
                }

                if (stack.getItem().equals(Items.MILK_BUCKET))
                {
                    thirstHandler.addStats(6, 0.7F);
                }
                else if (stack.getItem().equals(Items.POTIONITEM))
                {
                    if ( PotionUtils.getFullEffectsFromItem(stack).isEmpty())
                    {
                        thirstHandler.addStats(7, 0.5F);
                    }
                    else
                    {
                        //Still fill thirst for other potions, but less than water
                        thirstHandler.addStats(4, 0.3F);
                    }
                }

                if (zeroStack) stack.setCount(0);
            }
        }
    }
}

package com.paulBoxman.mysterymod.events;

import com.paulBoxman.mysterymod.MysteryMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = MysteryMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {

  @SubscribeEvent
  public static void tntOnJump(LivingEvent.LivingJumpEvent event) {

    LivingEntity entity = event.getEntityLiving();

    if (!(entity instanceof PlayerEntity)) { return; }
    if (entity.isInWater()){ return; }
    if (entity.isInLava()){ return; }
    if (entity.isOnLadder()){ return; }

    World world = entity.getEntityWorld();


    double roundedX = Math.round(entity.getPosX() - 0.5f);
    double roundedY = Math.ceil(entity.getPosY());
    double roundedZ = Math.round(entity.getPosZ() - 0.5f);

//    BlockPos player_block_pos = entity.func_233580_cy_();
    BlockPos target_block_pos = new BlockPos(roundedX, roundedY - 1, roundedZ);
    BlockState target_block = world.getBlockState(target_block_pos);

//    List<BlockState> targetBlocks = new ArrayList<BlockState>();
//    for (int i = -1; i <= 1; i++) {
//      for (int j = -1; j <= 1; j++) {
//        BlockPos target_block_pos = new BlockPos(player_block_pos.getX() + i,
//                roundedY - 1,
//                player_block_pos.getZ() + j);
//        BlockState target_block = world.getBlockState(target_block_pos);
//
//        if (target_block.isSolid()){
//          targetBlocks.add(target_block);
//        }
//      }
//    }

    if (target_block.getExplosionResistance(world, target_block_pos, null) > 600.0f){ return; }
//    if (!target_block.isSolid()) { return; }

    world.removeBlock(target_block_pos, false);
    TNTEntity new_tnt = new TNTEntity(world, target_block_pos.getX() + 0.5f, target_block_pos.getY(), target_block_pos.getZ() + 0.5f, entity);
    world.addEntity(new_tnt);
    new_tnt.playSound(SoundEvents.ENTITY_TNT_PRIMED, 1f, 1f);
  }


//  @SubscribeEvent
//  public static void sunlightDamage(LivingEvent.LivingUpdateEvent event) {
//
//    LivingEntity player = event.getEntityLiving();
//    if (!(player instanceof PlayerEntity)) { return; }
//    World world = player.getEntityWorld();
//    BlockPos playerPosition = player.func_233580_cy_();
//
//    if (world.canBlockSeeSky(playerPosition) && world.isDaytime()){
//      MysteryMod.LOGGER.info("SKY");
//      if (!player.isBurning()) {
//        player.setFire(8);
//      }
//
//      player.playSound(SoundEvents.BLOCK_BELL_RESONATE, 0.5f, 0.5f);
//    }
//  }
}

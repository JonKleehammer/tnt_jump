package com.paulBoxman.mysterymod.events;

import com.paulBoxman.mysterymod.MysteryMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MysteryMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModClientEvents {

  @SubscribeEvent
  public static void tntOnJump(LivingEvent.LivingJumpEvent event) {

    if (!Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()){
      return;
    }

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
    BlockPos targetBlockPos = new BlockPos(roundedX, roundedY - 1, roundedZ);
    BlockState targetBlock = world.getBlockState(targetBlockPos);

//    List<BlockState> targetBlocks = new ArrayList<BlockState>();
//    for (int i = -1; i <= 1; i++) {
//      for (int j = -1; j <= 1; j++) {
//        BlockPos targetBlockPos = new BlockPos(player_block_pos.getX() + i,
//                roundedY - 1,
//                player_block_pos.getZ() + j);
//        BlockState targetBlock = world.getBlockState(targetBlockPos);
//
//        if (targetBlock.isSolid()){
//          targetBlocks.add(targetBlock);
//        }
//      }
//    }

    if (targetBlock.getExplosionResistance(world, targetBlockPos, null) > 600.0f){ return; }
//    if (!targetBlock.isSolid()) { return; }
    if (!jumpable(world, targetBlockPos, entity)) {

      BlockPos entityBlockPos = entity.func_233580_cy_();
      int targetX = entityBlockPos.getX();
      int targetZ = entityBlockPos.getZ();

      double xPos = entity.getPosX();
      double zPos = entity.getPosZ();

      double xOffset = xPos - targetX;
      double zOffset = zPos - targetZ;

      BlockPos closestBlockPos = null;
      double closestDistance = 999f;

      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <=1; j++){
          if (i == 0 && j == 0) {
            continue;
          }
          int adjX = targetX + i;
          int adjZ = targetZ + j;
          double distance = Math.pow((Math.pow(xPos - adjX, 2) + Math.pow(zPos - adjZ, 2)), 0.5f);

          if (distance < closestDistance) {
            BlockPos adjBlockPos = new BlockPos(adjX, entityBlockPos.getY() - 1, adjZ);
            // check if valid block
            if (jumpable(world, adjBlockPos, entity)) {
              // new closest jumpable block
              closestBlockPos = adjBlockPos;
              closestDistance = distance;
            }
          }
        }
      }

      if (closestBlockPos == null){
        return;
      }

      targetBlock = world.getBlockState(closestBlockPos);
      targetBlockPos = closestBlockPos;
    }

    world.removeBlock(targetBlockPos, false);
    TNTEntity new_tnt = new TNTEntity(world, targetBlockPos.getX() + 0.5f, targetBlockPos.getY(), targetBlockPos.getZ() + 0.5f, entity);
    world.addEntity(new_tnt);
    new_tnt.playSound(SoundEvents.ENTITY_TNT_PRIMED, 1f, 1f);
  }

  private static boolean jumpable(World world, BlockPos targetBlockPos, LivingEntity entity) {
    BlockState targetBlock = world.getBlockState(targetBlockPos);

    return !(targetBlock.getBlock() == Blocks.WATER ||
            targetBlock.getBlock() == Blocks.AIR ||
            targetBlock.getBlock() == Blocks.LAVA ||
            targetBlock.isLadder(world, targetBlockPos, entity));
  }


//  @SubscribeEvent
//  public static void jumpKeyDown(InputEvent.KeyInputEvent event) {
//    if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()){
//      int x = 1;
//      MysteryMod.LOGGER.info("KEYDOWN");
//    }
//    if (Minecraft.getInstance().gameSettings.keyBindJump.isPressed()){
//      int x = 2;
//      MysteryMod.LOGGER.info("KEYPRESSED");
//
//    }
//  }

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

package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.event.level.LevelEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.network.protocol.LevelEventPacket;

import java.util.Random;

public class BlockItemFrame extends BlockTransparent {

    public BlockItemFrame() {
        this(0);
    }

    public BlockItemFrame(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return ITEM_FRAME_BLOCK;
    }

    @Override
    public String getName() {
        return "Item Frame";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        BlockEntityItemFrame blockEntity = (BlockEntityItemFrame) getLevel().getBlockEntity(this);

        if (blockEntity == null) {
            CompoundTag nbt = new CompoundTag()
                    .putString("id", BlockEntity.ITEM_FRAME)
                    .putInt("x", getFloorX())
                    .putInt("y", getFloorY())
                    .putInt("z", getFloorZ())
                    .putByte("ItemRotation", 0)
                    .putFloat("ItemDropChance", 1.0f);
            blockEntity = new BlockEntityItemFrame(getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);
        }

        if (blockEntity.getItem().getId() == 0) {
            blockEntity.setItem(Item.get(item.getId(), item.getDamage(), 1));
            if (player != null) {
                if (player.isSurvival()) {
                    int count = item.getCount();
                    if (--count <= 0) {
                        player.getInventory().setItemInHand(new ItemBlock(new BlockAir()));
                        return true;
                    }

                    item.setCount(count);
                    player.getInventory().setItemInHand(item);
                }
            }

            /*LevelEventPacket pk = new LevelEventPacket();
            pk.x = (int) this.x;
            pk.y = (int) this.y;
            pk.z = (int) this.z;
            pk.evid = LevelEventPacket.EVENT_SOUND_ITEM_FRAME_ITEM_ADDED;
            pk.data = 0;

            this.level.addChunkPacket((int) pk.x >> 4, (int) pk.z >> 4, pk);*/

        } else {
            int itemRot = blockEntity.getItemRotation();
            if (itemRot == 7) itemRot = 0;
            else itemRot++;
            blockEntity.setItemRotation(itemRot);
        }
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        BlockEntityItemFrame itemFrame = (BlockEntityItemFrame) this.getLevel().getBlockEntity(this);

        if (itemFrame == null) {
            return new int[][]{{Item.ITEM_FRAME, 0, 1}};
        }

        int chance = new Random().nextInt(100) + 1;

        if (chance <= (itemFrame.getItemDropChance() * 100)) {
            return new int[][]{{Item.ITEM_FRAME, 0, 1}, {itemFrame.getItem().getId(), itemFrame.getItem().getDamage(), 1}};
        }

        return new int[][]{{Item.ITEM_FRAME, 0, 1}};
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        if (!target.isTransparent() && face > 1 && !block.isSolid()) {
            switch (face) {
                case Vector3.SIDE_NORTH:
                    this.meta = 3;
                case Vector3.SIDE_SOUTH:
                    this.meta = 2;
                case Vector3.SIDE_EAST:
                    this.meta = 0;
                case Vector3.SIDE_WEST:
                    this.meta = 1;
                    break;
                default:
                    return false;
            }

            this.getLevel().setBlock(block, this, true, true);
            CompoundTag nbt = new CompoundTag()
                    .putString("id", BlockEntity.ITEM_FRAME)
                    .putInt("x", (int) block.x)
                    .putInt("y", (int) block.y)
                    .putInt("z", (int) block.z)
                    .putByte("ItemRotation", 0)
                    .putFloat("ItemDropChance", 1.0f);

            if (item.hasCustomBlockData()) {
                item.getCustomBlockData().getTags().forEach((key, v) -> nbt.put(key, v));
            }

            new BlockEntityItemFrame(getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);
            return true;
        }
        return false;
    }
}

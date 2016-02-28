package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

import java.util.Random;

/**
 * @author Box.
 */
public class EntityCreeper extends EntityMob {
    public static final int NETWORK_ID = 33;

    public static final int DATA_POWERED = 19;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityCreeper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public final boolean isPowered() {
        return getDataPropertyBoolean(DATA_POWERED);
    }

    public void setPowered(boolean powered){
        this.namedTag.putBoolean("IsPowered",powered);
        this.dataProperties.putBoolean(DATA_POWERED, powered);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setHealth(20);

        if (this.namedTag.getBoolean("IsPowered")) {
            this.dataProperties.putBoolean(DATA_POWERED, true);
        }
    }


    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = this.getNetworkId();
        pk.eid = this.getId();
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }

    @Override
    public Item[] getDrops() {
        int random = new Random().nextInt(50);
        if(random > 20){
            return new Item[]{Item.get(Item.GUNPOWDER,0,1)};
        }
        return new Item[0];
    }
}

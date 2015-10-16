package cn.nukkit.network.protocol;

import cn.nukkit.item.Item;
import cn.nukkit.utils.Binary;

import java.util.Map;
import java.util.UUID;

/**
 * @author Nukkit Project Team
 */
public class AddPlayerPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.ADD_PLAYER_PACKET;

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

    public UUID uuid;
    public String username;
    public long eid;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float pitch;
    public float yaw;
    public Item item;
    public Map<Integer, Object[]> metadata;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putUUID(this.uuid);
        this.putString(this.username);
        this.putLong(this.eid);
        this.putFloat(this.x);
        this.putFloat(this.y);
        this.putFloat(this.z);
        this.putFloat(this.speedX);
        this.putFloat(this.speedY);
        this.putFloat(this.speedZ);
        this.putFloat(this.yaw);
        this.putFloat(this.yaw);
        this.putFloat(this.pitch);
        this.putItem(this.item);
        this.put(Binary.writeMetadata(this.metadata));
    }
}

package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class TileEntityDataPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.TILE_ENTITY_DATA_PACKET;

    public int x;
    public byte y;
    public int z;
    public byte[] namedTag;

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.x = this.getInt();
        this.y = this.getByte();
        this.z = this.getInt();
        this.namedTag = this.get(getCursor() - getOffset());
    }

    @Override
    public void encode() {
        this.putInt(this.x);
        this.putByte(this.y);
        this.putInt(this.z);
        this.put(this.namedTag);
    }
}
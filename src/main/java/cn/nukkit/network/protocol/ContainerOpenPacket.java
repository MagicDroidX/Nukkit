package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ContainerOpenPacket extends DataPacket {
    public static final byte NETWORK_ID = Info.CONTAINER_OPEN_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public byte windowId;
    public byte type;
    public short slots;
    public int x;
    public int y;
    public int z;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.windowId);
        this.putByte(this.type);
        this.putShort(this.slots);
        this.putInt(this.x);
        this.putInt(this.y);
        this.putInt(this.z);
    }
}

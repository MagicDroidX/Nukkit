package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class FullChunkDataPacket extends DataPacket {

    public static final byte NETWORK_ID = Info.FULL_CHUNK_DATA_PACKET;

    public int chunkX;
    public int chunkZ;
    public byte[] data;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.putInt(this.chunkX);
        this.putInt(this.chunkZ);
        this.putInt(this.data.length);
        this.put(this.data);
    }

    @Override
    public byte getNetworkId() {
        return NETWORK_ID;
    }

}

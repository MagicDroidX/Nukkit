package cn.nukkit.network.protocol;

public class ResourcePackClientResponsePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET;

    public byte unknownByte;
    public short unknownShort;

    @Override
    public void decode() {
        this.unknownByte = (byte) this.getByte();
        this.unknownShort = (short) this.getShort();
    }

    @Override
    public void encode() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}

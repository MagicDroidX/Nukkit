package cn.nukkit.block;

import cn.nukkit.item.Tool;
import cn.nukkit.utils.BlockColor;

/**
 * Created by Pub4Game on 26.12.2015.
 */
public class IronTrapdoor extends Trapdoor {

    public IronTrapdoor() {
        this(0);
    }

    public IronTrapdoor(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return IRON_TRAPDOOR;
    }

    @Override
    public String getName() {
        return "Iron Trapdoor";
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public double getResistance() {
        return 25;
    }

    @Override
    public int getToolType() {
        return Tool.TYPE_PICKAXE;
    }


    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }
}

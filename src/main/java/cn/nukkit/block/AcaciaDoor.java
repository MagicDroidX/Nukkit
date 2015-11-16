package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;

public class AcaciaDoor extends Door{

    public AcaciaDoor(){ this(0); }

    public AcaciaDoor(int meta) {
        super(Block.ACACIA_DOOR_BLOCK, meta);
    }

    @Override
    public String getName(){
        return "Acacia Door Block";
    }

    @Override
    public boolean canBeActivated(){
        return true;
    }

    @Override
    public double getHardness(){
        return 3;
    }

    @Override
    public int getToolType(){
        return Tool.TYPE_AXE;
    }

    @Override
    public int[][] getDrops(Item item){
        return new int[][]{
                {Item.ACACIA_DOOR, 0, 1}
        };
    }
}
package cn.nukkit.item.enchantment;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemTool;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class Enchantment implements Cloneable {

    public static final int TYPE_INVALID = -1;

    public static final int TYPE_ARMOR_PROTECTION = 0;
    public static final int TYPE_ARMOR_FIRE_PROTECTION = 1;
    public static final int TYPE_ARMOR_FALL_PROTECTION = 2;
    public static final int TYPE_ARMOR_EXPLOSION_PROTECTION = 3;
    public static final int TYPE_ARMOR_PROJECTILE_PROTECTION = 4;
    public static final int TYPE_ARMOR_THORNS = 5;
    public static final int TYPE_WATER_BREATHING = 6;
    public static final int TYPE_WATER_SPEED = 7;
    public static final int TYPE_WATER_AFFINITY = 8;
    public static final int TYPE_WEAPON_SHARPNESS = 9;
    public static final int TYPE_WEAPON_SMITE = 10;
    public static final int TYPE_WEAPON_ARTHROPODS = 11;
    public static final int TYPE_WEAPON_KNOCKBACK = 12;
    public static final int TYPE_WEAPON_FIRE_ASPECT = 13;
    public static final int TYPE_WEAPON_LOOTING = 14;
    public static final int TYPE_MINING_EFFICIENCY = 15;
    public static final int TYPE_MINING_SILK_TOUCH = 16;
    public static final int TYPE_MINING_DURABILITY = 17;
    public static final int TYPE_MINING_FORTUNE = 18;
    public static final int TYPE_BOW_POWER = 19;
    public static final int TYPE_BOW_KNOCKBACK = 20;
    public static final int TYPE_BOW_FLAME = 21;
    public static final int TYPE_BOW_INFINITY = 22;
    public static final int TYPE_FISHING_FORTUNE = 23;
    public static final int TYPE_FISHING_LURE = 24;

    public static final int RARITY_COMMON = 0;
    public static final int RARITY_UNCOMMON = 1;
    public static final int RARITY_RARE = 2;
    public static final int RARITY_MYTHIC = 3;

    public static final int ACTIVATION_EQUIP = 0;
    public static final int ACTIVATION_HELD = 1;
    public static final int ACTIVATION_SELF = 2;

    public static final int SLOT_NONE = 0;
    public static final int SLOT_ALL = 0b11111111111111;
    public static final int SLOT_ARMOR = 0b1111;
    public static final int SLOT_HEAD = 0b1;
    public static final int SLOT_TORSO = 0b10;
    public static final int SLOT_LEGS = 0b100;
    public static final int SLOT_FEET = 0b1000;
    public static final int SLOT_SWORD = 0b10000;
    public static final int SLOT_BOW = 0b100000;
    public static final int SLOT_TOOL = 0b111000000;
    public static final int SLOT_HOE = 0b1000000;
    public static final int SLOT_SHEARS = 0b10000000;
    public static final int SLOT_FLINT_AND_STEEL = 0b10000000;
    public static final int SLOT_DIG = 0b111000000000;
    public static final int SLOT_AXE = 0b1000000000;
    public static final int SLOT_PICKAXE = 0b10000000000;
    public static final int SLOT_SHOVEL = 0b10000000000;
    public static final int SLOT_FISHING_ROD = 0b100000000000;
    public static final int SLOT_CARROT_STICK = 0b1000000000000;

    public static final String[] words = {"the", "elder", "scrolls", "klaatu", "berata", "niktu", "xyzzy", "bless", "curse", "light", "darkness", "fire", "air", "earth", "water", "hot", "dry", "cold", "wet", "ignite", "snuff", "embiggen", "twist", "shorten", "stretch", "fiddle", "destroy", "imbue", "galvanize", "enchant", "free", "limited", "range", "of", "towards", "inside", "sphere", "cube", "self", "other", "ball", "mental", "physical", "grow", "shrink", "demon", "elemental", "spirit", "animal", "creature", "beast", "humanoid", "undead", "fresh", "stale"};

    protected static Enchantment[] enchantments;

    public static void init() {
        enchantments = new Enchantment[256];

        enchantments[TYPE_ARMOR_PROTECTION] = new Enchantment(TYPE_ARMOR_PROTECTION, "%enchantment.protect.all", RARITY_COMMON, ACTIVATION_EQUIP, SLOT_ARMOR, 4);
        enchantments[TYPE_ARMOR_FIRE_PROTECTION] = new Enchantment(TYPE_ARMOR_FIRE_PROTECTION, "%enchantment.protect.fire", RARITY_UNCOMMON, ACTIVATION_EQUIP, SLOT_ARMOR, 4);
        enchantments[TYPE_ARMOR_FALL_PROTECTION] = new Enchantment(TYPE_ARMOR_FALL_PROTECTION, "%enchantment.protect.fall", RARITY_UNCOMMON, ACTIVATION_EQUIP, SLOT_FEET, 4);
        enchantments[TYPE_ARMOR_EXPLOSION_PROTECTION] = new Enchantment(TYPE_ARMOR_EXPLOSION_PROTECTION, "%enchantment.protect.explosion", RARITY_UNCOMMON, ACTIVATION_EQUIP, SLOT_ARMOR, 4);
        enchantments[TYPE_ARMOR_PROJECTILE_PROTECTION] = new Enchantment(TYPE_ARMOR_PROJECTILE_PROTECTION, "%enchantment.protect.projectile", RARITY_UNCOMMON, ACTIVATION_EQUIP, SLOT_ARMOR, 4);
        enchantments[TYPE_ARMOR_THORNS] = new Enchantment(TYPE_ARMOR_THORNS, "%enchantment.thorns", RARITY_UNCOMMON, ACTIVATION_EQUIP, SLOT_ARMOR, 3);
        enchantments[TYPE_WATER_BREATHING] = new Enchantment(TYPE_WATER_BREATHING, "%enchantment.water.breathing", RARITY_UNCOMMON, ACTIVATION_EQUIP, SLOT_HEAD, 3);
        enchantments[TYPE_WATER_SPEED] = new Enchantment(TYPE_WATER_SPEED, "%enchantment.water.speed", RARITY_UNCOMMON, ACTIVATION_EQUIP, SLOT_FEET, 3);
        enchantments[TYPE_WATER_AFFINITY] = new Enchantment(TYPE_WATER_AFFINITY, "%enchantment.water.affinity", RARITY_UNCOMMON, ACTIVATION_EQUIP, SLOT_HEAD, 1);
        enchantments[TYPE_WEAPON_SHARPNESS] = new Enchantment(TYPE_WEAPON_SHARPNESS, "%enchantment.weapon.sharpness", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_SWORD, 5);
        enchantments[TYPE_WEAPON_SMITE] = new Enchantment(TYPE_WEAPON_SMITE, "%enchantment.weapon.smite", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_SWORD, 5);
        enchantments[TYPE_WEAPON_ARTHROPODS] = new Enchantment(TYPE_WEAPON_ARTHROPODS, "%enchantment.weapon.arthropods", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_SWORD, 5);
        enchantments[TYPE_WEAPON_KNOCKBACK] = new Enchantment(TYPE_WEAPON_KNOCKBACK, "%enchantment.weapon.knockback", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_SWORD, 2);
        enchantments[TYPE_WEAPON_FIRE_ASPECT] = new Enchantment(TYPE_WEAPON_FIRE_ASPECT, "%enchantment.weapon.fire", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_SWORD, 2);
        enchantments[TYPE_WEAPON_LOOTING] = new Enchantment(TYPE_WEAPON_LOOTING, "%enchantment.weapon.looting", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_SWORD, 3);
        enchantments[TYPE_MINING_EFFICIENCY] = new Enchantment(TYPE_MINING_EFFICIENCY, "%enchantment.mining.efficiency", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_PICKAXE, 5);
        enchantments[TYPE_MINING_SILK_TOUCH] = new Enchantment(TYPE_MINING_SILK_TOUCH, "%enchantment.mining.silktouch", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_PICKAXE, 1);
        enchantments[TYPE_MINING_DURABILITY] = new Enchantment(TYPE_MINING_DURABILITY, "%enchantment.mining.durability", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_ALL, 3);
        enchantments[TYPE_MINING_FORTUNE] = new Enchantment(TYPE_MINING_FORTUNE, "%enchantment.mining.fortune", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_PICKAXE, 3);
        enchantments[TYPE_BOW_POWER] = new Enchantment(TYPE_BOW_POWER, "%enchantment.bow.power", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_BOW, 5);
        enchantments[TYPE_BOW_KNOCKBACK] = new Enchantment(TYPE_BOW_KNOCKBACK, "%enchantment.bow.knockback", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_BOW, 2);
        enchantments[TYPE_BOW_FLAME] = new Enchantment(TYPE_BOW_FLAME, "%enchantment.bow.flame", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_BOW, 1);
        enchantments[TYPE_BOW_INFINITY] = new Enchantment(TYPE_BOW_INFINITY, "%enchantment.bow.infinity", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_BOW, 1);
        enchantments[TYPE_FISHING_FORTUNE] = new Enchantment(TYPE_FISHING_FORTUNE, "%enchantment.fishing.fortune", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_FISHING_ROD, 3);
        enchantments[TYPE_FISHING_LURE] = new Enchantment(TYPE_FISHING_LURE, "%enchantment.fishing.lure", RARITY_UNCOMMON, ACTIVATION_HELD, SLOT_FISHING_ROD, 3);

        EnchantmentLevelTable.init();
    }

    public static int getEnchantAbility(Item item) {
        switch (item.getId()) {
            case Item.BOOK:
            case Item.BOW:
            case Item.FISHING_ROD:
                return 4;
        }

        if (item.isArmor()) {
            switch (item.getTier()) {
                case ItemArmor.TIER_CHAIN:
                    return 12;
                case ItemArmor.TIER_IRON:
                    return 9;
                case ItemArmor.TIER_DIAMOND:
                    return 10;
                case ItemArmor.TIER_LEATHER:
                    return 15;
                case ItemArmor.TIER_GOLD:
                    return 25;
            }
        }

        if (item.isTool()) {
            switch (item.getTier()) {
                case ItemTool.TIER_WOODEN:
                    return 15;
                case ItemTool.TIER_STONE:
                    return 5;
                case ItemTool.TIER_DIAMOND:
                    return 10;
                case ItemTool.TIER_IRON:
                    return 14;
                case ItemTool.TIER_GOLD:
                    return 22;
            }
        }

        return 0;
    }

    public static int getEnchantWeight(int enchantmentId) {
        switch (enchantmentId) {
            case TYPE_ARMOR_PROTECTION:
                return 10;
            case TYPE_ARMOR_FIRE_PROTECTION:
                return 5;
            case TYPE_ARMOR_FALL_PROTECTION:
                return 2;
            case TYPE_ARMOR_EXPLOSION_PROTECTION:
                return 5;
            case TYPE_WATER_BREATHING:
                return 2;
            case TYPE_WATER_AFFINITY:
                return 2;
            case TYPE_WEAPON_SHARPNESS:
                return 10;
            case TYPE_WEAPON_SMITE:
                return 5;
            case TYPE_WEAPON_ARTHROPODS:
                return 5;
            case TYPE_WEAPON_KNOCKBACK:
                return 5;
            case TYPE_WEAPON_FIRE_ASPECT:
                return 2;
            case TYPE_WEAPON_LOOTING:
                return 2;
            case TYPE_MINING_EFFICIENCY:
                return 10;
            case TYPE_MINING_SILK_TOUCH:
                return 1;
            case TYPE_MINING_DURABILITY:
                return 5;
            case TYPE_MINING_FORTUNE:
                return 2;
            case TYPE_BOW_POWER:
                return 10;
            case TYPE_BOW_KNOCKBACK:
                return 2;
            case TYPE_BOW_FLAME:
                return 2;
            case TYPE_BOW_INFINITY:
                return 1;
        }
        return 0;
    }

    public static Enchantment getEnchantment(int id) {
        try {
            if (enchantments[id] != null) {
                return enchantments[id].clone();
            }
        } catch (Exception e) {
            return new Enchantment(TYPE_INVALID, "unknown", 0, 0, 0, 0);
        }
        return new Enchantment(TYPE_INVALID, "unknown", 0, 0, 0, 0);
    }

    public static Enchantment getEnchantmentByName(String name) {
        try {
            short id = Enchantment.class.getField("TYPE_" + name.toUpperCase()).getByte(null);
            return getEnchantment(id);
        } catch (Exception e) {
            return null;
        }
    }

    private int id;
    private int level = 1;
    private int maxLevel;
    private String name;
    private int rarity;
    private int activationType;
    private int slot;

    private Enchantment(int id, String name, int rarity, int activationType, int slot, int maxLevel) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.activationType = activationType;
        this.slot = slot;
        this.maxLevel = maxLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Enchantment) {
            return ((Enchantment) obj).getId() == this.getId() && ((Enchantment) obj).getLevel() == this.getLevel();
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRarity() {
        return rarity;
    }

    public int getActivationType() {
        return activationType;
    }

    public int getSlot() {
        return slot;
    }

    public boolean hasSlot(int slot) {
        return (this.slot & slot) > 0;
    }

    public int getLevel() {
        return level;
    }

    public Enchantment setLevel(int level) {
        this.level = level;
        return this;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public Enchantment clone() {
        try {
            return (Enchantment) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static String generateName() {
        int count = ThreadLocalRandom.current().nextInt(3, 6);
        HashSet<String> set = new HashSet<>();
        while (set.size() < count) {
            set.add(Enchantment.words[ThreadLocalRandom.current().nextInt(0, Enchantment.words.length)]);
        }

        String[] words = set.stream().toArray(String[]::new);
        return String.join(" ", words);
    }
}

package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemMinecart;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.AddEntityPacket;

import static cn.nukkit.item.Item.*;

/**
 * Author: Adam Matthew [larryTheCoder]
 * <p>
 * Nukkit Project
 */
public abstract class EntityMinecartAbstract extends EntityVehicle {

    /**
     * Minecart: Nukkit Project 
     * --- 
     * Made by @larryTheCoder 
     * DO NOT COPY!
     */
    private boolean hasCurvedRail;
    private String name;
    private final int[][][] blockFace = new int[][][]{{{0, 0, -1}, {0, 0, 1}},
    {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0},
    {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}},
    {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1},
    {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    private double currentSpeed = 0;

    // anyone can tell me what these DATA_FLAGS are?
    public static final int DATA_VEHICLE_DISPLAY_BLOCK = 20;
    public static final int DATA_VEHICLE_DISPLAY_DATA = 20;
    public static final int DATA_VEHICLE_DISPLAY_OFFSET = 21;
    public static final int DATA_VEHICLE_CUSTOM_DISPLAY = 22;

    /**
     * Get the type of the minecart id:
     * <p>
     * 0. Minecart empty, 1. Minecart with chest, 2. Minecart with furnace 3.
     * Minecart with TNT, 4. Minecart with Mob spawner, 5. Minecart with hopper,
     * 6. Minecart with command block.
     *
     * @return Integer
     */
    public abstract int getMineId();

    public EntityMinecartAbstract(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getHeight() {
        return 0.7F; // Yes
    }

    @Override
    public float getWidth() {
        return 0.98F; // Yes
    }

    @Override
    protected float getDrag() {
        return 0.1F; // Exactly? Not really
    }

    @Override
    protected float getGravity() {
        return 0.5f; // This true!
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (closed) {
            return false;
        }

        int tickDiff = currentTick - lastUpdate;

        if (tickDiff <= 0) {
            return false;
        }
        lastUpdate = currentTick;

        boolean hasUpdate = this.entityBaseTick(tickDiff);

        if (isAlive()) {
            if (y < -64.0D) {
                kill();
            }
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            motionY -= 0.03999999910593033D;
            int dx = MathHelper.floor(x);
            int dy = MathHelper.floor(y);
            int dz = MathHelper.floor(z);

            // Check if the rail exsits
            if (isRail(level.getBlock(new Vector3(dx, dy - 1, dz)))) {
                --dy; // check again down
            }

            double speeds = 0.4D; // The current exact minecart speed
            double drag = 0.0078125; // The minecart drag
            Block block = level.getBlock(new Vector3(dx, dy, dz)); // get the rail

            // Now start to check if the block is 'Rail'
            if (isRail(block)) {
                // Okay we got the rail, now get the Minecart damage
                int l = level.getBlock(new Vector3(dx, dy, dz)).getDamage();

                // Make it runs and see whats happend
                processMovement(dx, dy, dz, speeds, drag, block, l);
                if (block.equals(Block.ACTIVATOR_RAIL)) {
                    // If the minecart are TNT, we explode it
                    activate(dx, dy, dz, (l & 8) != 0);
                }
            } else {
                // return slow down minecart
                setSlowdown(speeds);
            }

            // Curved rail
            pitch = 0.0F;
            double befX = lastX - x;
            double befZ = lastZ - z;

            // rotation check #1 (executed below)
            if (befX * befX + befZ * befZ > 0.001D) {
                yaw = (float) (Math.atan2(befZ, befX) * 180.0D / 3.141592653589793D);
                if (hasCurvedRail) { // Has curved rail rotate 90'
                    yaw += 180.0F;
                }
            }

            // Rotation execution
            double rotate = setMinecartRotation(yaw - lastYaw);

            if (rotate < -170.0D || rotate >= 170.0D) {
                yaw += 180.0F;
                hasCurvedRail = !hasCurvedRail;
            }

            setRotation(yaw % 360.0F, pitch % 360.0F);

            // Entity colliding (eg. When minecart and minecart hits together
            // It will moved to opposite direction)
            for (Entity entity : level.getNearbyEntities(this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D), this)) {
                if (entity != this.linkedEntity && entity instanceof EntityMinecartAbstract) {
                    ((EntityMinecartAbstract) entity).onCollideWithVehicle(this);
                }
            }
            
            // Any suggestion?
            hasUpdate = true;
        }
        
        // This (code) will check if the entity (mobs|player) is dead!
        if (this.linkedEntity != null && !this.isAlive() || this.linkedEntity != null) {
            if (!this.linkedEntity.isAlive()) {
                if (this.linkedEntity.riding == this) {
                    this.linkedEntity.riding = null;
                }

                this.linkedEntity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
                this.linkedEntity = null;
            } else if (!this.isAlive()) {
                if (this.linkedEntity.riding == this) {
                    this.linkedEntity.riding = null;
                }

                this.linkedEntity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
                this.linkedEntity = null;
            }
        }
        return hasUpdate || !onGround || Math.abs(motionX) > 0.00001 || Math.abs(motionY) > 0.00001 || Math.abs(motionZ) > 0.00001;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (source instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
            if (damager instanceof Player) {
                if (((Player) damager).isCreative()) {
                    kill();
                }
                if (getHealth() <= 0) {
                    if (((Player) damager).isSurvival()) {
                        level.dropItem(this, dropItem());
                    }
                    close();
                }
            }
        }

        EntityEventPacket pk = new EntityEventPacket();
        pk.eid = super.getId();
        pk.event = EntityEventPacket.HURT_ANIMATION;
        Server.broadcastPacket(hasSpawned.values(), pk);

        return true;
    }

    @Override
    public void close() {
        super.close();

        if (linkedEntity instanceof Player) {
            linkedEntity.riding = null;
            linkedEntity = null;
        }

        SmokeParticle particle = new SmokeParticle(this);
        level.addParticle(particle);
    }

    @Override
    public void initEntity() {
        super.initEntity();

        setName("Minecart");
    }

    @Override
    public boolean onInteract(Player p, Item item) {
        if (linkedEntity != null) {
            return false;
        }

        if (this instanceof EntityMinecartEmpty) {
            this.mount(p); // Simple
        }
        return true;
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityUniqueId = getId();
        pk.entityRuntimeId = getId();
        pk.type = getNetworkId();
        pk.x = (float) x;
        pk.y = (float) y;
        pk.z = (float) z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        pk.metadata = dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }

    @Override
    public void onCollideWithVehicle(Entity entity) {
        timing.startTiming();
        if (entity != riding) {
            if (entity instanceof EntityLiving && !(entity instanceof EntityHuman) && motionX * motionX + motionZ * motionZ > 0.01D && linkedEntity == null && entity.riding == null) {
                if (riding == null) {
                    this.mount(entity); // Just like MC logic: Entity will ride minecart
                }
            }

            double motiveX = entity.x - x;
            double motiveZ = entity.z - z;
            double square = motiveX * motiveX + motiveZ * motiveZ;

            if (square >= 9.999999747378752E-5D) {
                square = Math.sqrt(square);
                motiveX /= square;
                motiveZ /= square;
                double next = 1.0D / square;

                if (next > 1.0D) {
                    next = 1.0D;
                }

                motiveX *= next;
                motiveZ *= next;
                motiveX *= 0.10000000149011612D;
                motiveZ *= 0.10000000149011612D;
                motiveX *= (double) (1.0F - y);
                motiveZ *= (double) (1.0F - y);
                motiveX *= 0.5D;
                motiveZ *= 0.5D;
                if (entity instanceof EntityMinecartAbstract) {
                    double desinityX = entity.x - x;
                    double desinityZ = entity.z - z;
                    Vector3 vector = new Vector3(desinityX, 0.0D, desinityZ);
                    Vector3 vector1 = new Vector3((double) MathHelper.cos((float) yaw * 3.1415927F / 180.0F), 0.0D, (double) MathHelper.sin((float) yaw * 3.1415927F / 180.0F));
                    double desinityXZ = Math.abs(vector.dot(vector1));

                    if (desinityXZ < 0.800000011920929D) {
                        return;
                    }

                    double motX = entity.motionX + motionX;
                    double motZ = entity.motionZ + motionZ;

                    if (((EntityMinecartAbstract) entity).getMineId() == 2 && getMineId() != 2) {
                        motionX *= 0.20000000298023224D;
                        motionZ *= 0.20000000298023224D;
                        setMotion(entity.motionX - motiveX, 0.0D, entity.motionZ - motiveZ);
                        entity.motionX *= -0.949999988079071D;
                        entity.motionZ *= -0.949999988079071D;
                    } else if (((EntityMinecartAbstract) entity).getMineId() != 2 && getMineId() == 2) {
                        entity.motionX *= 0.20000000298023224D;
                        entity.motionZ *= 0.20000000298023224D;
                        setMotion(entity, motionX + motiveX, 0.0D, motionZ + motiveZ);
                        motionX *= -0.949999988079071D;
                        motionZ *= -0.949999988079071D;
                    } else {
                        motX /= 2.0D;
                        motZ /= 2.0D;
                        motionX *= 0.20000000298023224D;
                        motionZ *= 0.20000000298023224D;
                        setMotion(motX - motiveX, 0.0D, motZ - motiveZ);
                        entity.motionX *= -0.20000000298023224D;
                        entity.motionZ *= -0.20000000298023224D;
                        setMotion(entity, motX + motiveX, 0.0D, motZ + motiveZ);
                    }
                } else {
                    setMotion(motiveX, 0.0D, motiveZ);
                }
            }
        }
        timing.stopTiming();
    }

    public Item dropItem() {
        return new ItemMinecart();
    }

    protected void activate(int x, int y, int z, boolean flag) {
    }

    protected void setSlowdown(double slow) {
        if (motionX < -slow) {
            motionX = -slow;
        }

        if (motionX > slow) {
            motionX = slow;
        }

        if (motionZ < -slow) {
            motionZ = -slow;
        }

        if (motionZ > slow) {
            motionZ = slow;
        }

        if (onGround) {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }

        moveMinecart(motionX, motionY, motionZ);
        if (!onGround) {
            motionX *= 0.949999988079071D;
            motionY *= 0.949999988079071D;
            motionZ *= 0.949999988079071D;
        }
    }

    protected void processMovement(int dx, int dy, int dz, double speed, double drag, Block block, int rail) {
        fallDistance = 0.0F;
        Vector3 vector = getNextRail(x, y, z);

        y = (double) dy;
        boolean hasCurved = false;
        boolean redstoneMultiply = false;

        if (block.equals(Block.POWERED_RAIL)) {
            hasCurved = true;
            redstoneMultiply = true;
        }

        if (isRedstonePowered(block)) {
            rail &= 7;
        }

        if (rail >= 2 && rail <= 5) {
            y = (double) (dy + 1);
        }

        if (rail == 2) {
            motionX -= drag;
        }

        if (rail == 3) {
            motionX += drag;
        }

        if (rail == 4) {
            motionZ += drag;
        }

        if (rail == 5) {
            motionZ -= drag;
        }

        int[][] facing = blockFace[rail];
        double facing1 = (double) (facing[1][0] - facing[0][0]);
        double facing2 = (double) (facing[1][2] - facing[0][2]);
        double speedOnTurns = Math.sqrt(facing1 * facing1 + facing2 * facing2);
        double realFacing = motionX * facing1 + motionZ * facing2;

        if (realFacing < 0.0D) {
            facing1 = -facing1;
            facing2 = -facing2;
        }

        double squareOfFame = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (squareOfFame > 2.0D) {
            squareOfFame = 2.0D;
        }

        motionX = squareOfFame * facing1 / speedOnTurns;
        motionZ = squareOfFame * facing2 / speedOnTurns;
        double expectedSpeed;
        double playerYawNeg; // PlayerYawNegative
        double playerYawPos; // PlayerYawPositive
        double motion;

        if (linkedEntity != null && linkedEntity instanceof EntityLiving) {
            expectedSpeed = currentSpeed;
            if (expectedSpeed > 0.0D) {
                playerYawNeg = -Math.sin((double) (linkedEntity.yaw * 3.1415927F / 180.0F));
                playerYawPos = Math.cos((double) (linkedEntity.yaw * 3.1415927F / 180.0F));
                motion = motionX * motionX + motionZ * motionZ;
                if (motion < 0.01D) {
                    motionX += playerYawNeg * 0.1D;
                    motionZ += playerYawPos * 0.1D;
                    redstoneMultiply = false;
                }
            }
        }

        if (redstoneMultiply) {
            expectedSpeed = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if (expectedSpeed < 0.03D) {
                motionX *= 0.0D;
                motionY *= 0.0D;
                motionZ *= 0.0D;
            } else {
                motionX *= 0.5D;
                motionY *= 0.0D;
                motionZ *= 0.5D;
                getServer().getLogger().info("COMPLICATED 1");
            }
        }

        playerYawNeg = (double) dx + 0.5D + (double) facing[0][0] * 0.5D;
        playerYawPos = (double) dz + 0.5D + (double) facing[0][2] * 0.5D;
        motion = (double) dx + 0.5D + (double) facing[1][0] * 0.5D;
        double wallOfFame = (double) dz + 0.5D + (double) facing[1][2] * 0.5D;

        facing1 = motion - playerYawNeg;
        facing2 = wallOfFame - playerYawPos;
        double motX;
        double motZ;

        if (facing1 == 0.0D) {
            x = (double) dx + 0.5D;
            expectedSpeed = z - (double) dz;
        } else if (facing2 == 0.0D) {
            z = (double) dz + 0.5D;
            expectedSpeed = x - (double) dx;
        } else {
            motX = x - playerYawNeg;
            motZ = z - playerYawPos;
            expectedSpeed = (motX * facing1 + motZ * facing2) * 2.0D;
        }

        x = playerYawNeg + facing1 * expectedSpeed;
        z = playerYawPos + facing2 * expectedSpeed;
        setPosition(new Vector3(x, y + (double) getHeight(), z));
        motX = motionX;
        motZ = motionZ;
        if (linkedEntity != null) {
            motX *= 0.75D;
            motZ *= 0.75D;
        }

        if (motX < -speed) {
            motX = -speed;
        }

        if (motX > speed) {
            motX = speed;
        }

        if (motZ < -speed) {
            motZ = -speed;
        }

        if (motZ > speed) {
            motZ = speed;
        }

        moveMinecart(motX, 0.0D, motZ);
        if (facing[0][1] != 0 && MathHelper.floor(x) - dx == facing[0][0] && MathHelper.floor(z) - dz == facing[0][2]) {
            setPosition(new Vector3(x, y + (double) facing[0][1], z));
        } else if (facing[1][1] != 0 && MathHelper.floor(x) - dx == facing[1][0] && MathHelper.floor(z) - dz == facing[1][2]) {
            setPosition(new Vector3(x, y + (double) facing[1][1], z));
        }

        multiplyMotion();
        Vector3 vector1 = getNextRail(x, y, z);

        if (vector1 != null && vector != null) {
            double d14 = (vector.y - vector1.y) * 0.05D;

            squareOfFame = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if (squareOfFame > 0.0D) {
                motionX = motionX / squareOfFame * (squareOfFame + d14);
                motionZ = motionZ / squareOfFame * (squareOfFame + d14);
            }

            setPosition(new Vector3(x, vector1.y, z));
        }

        int floorX = MathHelper.floor(x);
        int floorZ = MathHelper.floor(z);

        if (floorX != dx || floorZ != dz) {
            squareOfFame = Math.sqrt(motionX * motionX + motionZ * motionZ);
            motionX = squareOfFame * (double) (floorX - dx);
            motionZ = squareOfFame * (double) (floorZ - dz);
        }

        if (hasCurved) {
            getServer().getLogger().info("COMPLICATED 2");
            double newMovie = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (newMovie > 0.01D) {
                double nextMovie = 0.06D;

                motionX += motionX / newMovie * nextMovie;
                motionZ += motionZ / newMovie * nextMovie;
            } else if (rail == 1) {
                if (level.getBlock(new Vector3(dx - 1, dy, dz)).isNormalBlock()) {
                    motionX = 0.02D;
                } else if (level.getBlock(new Vector3(dx + 1, dy, dz)).isNormalBlock()) {
                    motionX = -0.02D;
                }
            } else if (rail == 0) {
                if (level.getBlock(new Vector3(dx, dy, dz - 1)).isNormalBlock()) {
                    motionZ = 0.02D;
                } else if (level.getBlock(new Vector3(dx, dy, dz + 1)).isNormalBlock()) {
                    motionZ = -0.02D;
                }
            }
        }

    }

    protected double setMinecartRotation(double rotate) {
        rotate %= 360.0;
        if (rotate >= 180.0) {
            rotate -= 360.0;
        }

        if (rotate < -180.0) {
            rotate += 360.0;
        }

        return rotate;
    }

    protected void multiplyMotion() {
        if (this.linkedEntity != null) {
            this.motionX *= 0.996999979019165D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.996999979019165D;
        } else {
            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9599999785423279D;
        }
    }

    public void setMotion(Entity ent, double x, double y, double z) {
        ent.setMotion(new Vector3(x, y, z));
    }

    public void setMotion(double x, double y, double z) {
        super.setMotion(new Vector3(x, y, z));
        super.updateMovement();
    }

    public double calculateVelocityWYaw(double yaw) {
        yaw %= 360.0D;
        if (yaw >= 180.0D) {
            yaw -= 360.0D;
        }

        if (yaw < -180.0D) {
            yaw += 360.0D;
        }

        return yaw;
    }

    @SuppressWarnings("UnusedAssignment")
    public Vector3 getNextRail(double dx, double dy, double dz) {
        int checkX = MathHelper.floor(dx);
        int checkY = MathHelper.floor(dy);
        int checkZ = MathHelper.floor(dz);

        if (isRail(level.getBlock(new Vector3(checkX, checkY - 1, checkZ)))) {
            --checkY;
        }

        Block block = level.getBlock(new Vector3(checkX, checkY, checkZ));

        if (isRail(block)) {
            int l = level.getBlock(new Vector3(checkX, checkY, checkZ)).getDamage();

            dy = (double) checkY;
            if (isRedstonePowered(block)) {
                l &= 7;
            }

            if (l >= 2 && l <= 5) {
                dy = (double) (checkY + 1);
            }

            int[][] facing = blockFace[l];
            double rail;
            // Genisys minstake (Doesn't check surrounding more exactly)
            double nextOne = (double) checkX + 0.5D + (double) facing[0][0] * 0.5D;
            double nextTwo = (double) checkY + 0.5D + (double) facing[0][1] * 0.5D;
            double nextThree = (double) checkZ + 0.5D + (double) facing[0][2] * 0.5D;
            double nextFour = (double) checkX + 0.5D + (double) facing[1][0] * 0.5D;
            double nextFive = (double) checkY + 0.5D + (double) facing[1][1] * 0.5D;
            double nextSix = (double) checkZ + 0.5D + (double) facing[1][2] * 0.5D;
            double nextSeven = nextFour - nextOne;
            double nextEight = (nextFive - nextTwo) * 2.0D;
            double nextMax = nextSix - nextThree;

            if (nextSeven == 0.0D) {
                dx = (double) checkX + 0.5D;
                rail = dz - (double) checkZ;
            } else if (nextMax == 0.0D) {
                dz = (double) checkZ + 0.5D;
                rail = dx - (double) checkX;
            } else {
                double whatOne = dx - nextOne;
                double whatTwo = dz - nextThree;

                rail = (whatOne * nextSeven + whatTwo * nextMax) * 2.0D;
            }

            dx = nextOne + nextSeven * rail;
            dy = nextTwo + nextEight * rail;
            dz = nextThree + nextMax * rail;
            if (nextEight < 0.0D) {
                ++dy;
            }

            if (nextEight > 0.0D) {
                dy += 0.5D;
            }

            return new Vector3(dx, dy, dz);
        } else {
            return null;
        }
    }

    private void moveMinecart(double motionX, double motionY, double motionZ) {
        move(motionX, motionY, motionZ);
        super.updateMovement();
    }

    /**
     * Used to multiply the minecart current speed
     *
     * @param speed The speed of the minecart that will be calculated
     */
    public void setCurrentSpeed(double speed) {
        this.currentSpeed = speed;
    }

    /**
     * Define if the Block is rail
     *
     * @param block Block of the current target
     * @return boolean
     */
    private boolean isRail(Block block) {
        switch (block.getId()) {
            case RAIL:
            case POWERED_RAIL:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
                return true;
            default:
                return false;
        }
    }

    /**
     * Define if the Block is Redstone-powered rail
     *
     * @param block Block of the current target
     * @return boolean
     */
    private boolean isRedstonePowered(Block block) {
        switch (block.getId()) {
            case POWERED_RAIL:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
                return true;
            default:
                return false;
        }
    }
}

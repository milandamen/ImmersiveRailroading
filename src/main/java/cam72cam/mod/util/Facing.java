package cam72cam.mod.util;

import net.minecraft.util.EnumFacing;

public enum Facing {
    DOWN(EnumFacing.DOWN),
    UP(EnumFacing.UP),
    NORTH(EnumFacing.NORTH),
    SOUTH(EnumFacing.SOUTH),
    WEST(EnumFacing.WEST),
    EAST(EnumFacing.EAST),
    ;

    public final EnumFacing internal;

    Facing(EnumFacing internal) {
        this.internal = internal;
    }

    public static Facing from(EnumFacing facing) {
        switch (facing) {
            case DOWN:
                return DOWN;
            case UP:
                return UP;
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
            default:
                return null;
        }
    }

    public Facing getOpposite() {
        switch (this) {
            case DOWN:
                return UP;
            case UP:
                return DOWN;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case EAST:
                return WEST;
            default:
                return null;
        }
    }
}
package io.github.lukas2005.supernaturalcreatures.entity.goals;

import io.github.lukas2005.supernaturalcreatures.Utils;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AttackMeleeNoSunGoal extends MeleeAttackGoal {


    public AttackMeleeNoSunGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    @Override
    public boolean shouldExecute() {
        boolean flag = super.shouldExecute();
        if (flag) {
            LivingEntity entitylivingbase = this.attacker.getAttackTarget();
            if (entitylivingbase != null) {
                double distance = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getBoundingBox().minY, entitylivingbase.posZ);
                if (distance <= this.getAttackReachSqr(entitylivingbase)) {
                    return true;
                }
            }
            boolean avoidSun = true;
            if (attacker.getNavigator() instanceof GroundPathNavigator) {
                try {
                    Field f = Utils.getField(GroundPathNavigator.class, "shouldAvoidSun");
                    avoidSun = f.getBoolean(attacker.getNavigator());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (avoidSun) {

                Path path = null;
                try {
                    Field f = Utils.getField(MeleeAttackGoal.class, "path");
                    path = (Path) f.get(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (path != null) {
                    if (attacker.getEntityWorld().canBlockSeeSky(new BlockPos(MathHelper.floor(this.attacker.posX), (int) (this.attacker.getBoundingBox().minY + 0.5D), MathHelper.floor(this.attacker.posZ)))) {
                        return false;
                    }

                    ArrayList<PathPoint> l = new ArrayList<>();
                    for (int j = 0; j < path.getCurrentPathLength(); ++j) {
                        PathPoint pathpoint2 = path.getPathPointFromIndex(j);

                        if (this.attacker.getEntityWorld().canBlockSeeSky(new BlockPos(pathpoint2.x, pathpoint2.y, pathpoint2.z))) {
                            l.add(pathpoint2);
                            return path.getCurrentPathLength() > 1;
                        }
                    }
                    for (PathPoint p : l) {
                        path.setPoint(p.index, null);
                    }
                    l.clear();
                    l = null;
                }

            }

        }
        return flag;
    }
}
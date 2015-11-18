
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class InstantEnchantParticle extends GenericParticle{
	public InstantEnchantParticle(Vector3 pos){
		super(pos, Particle.TYPE_MOB_SPELL_INSTANTANEOUS);
	}
}

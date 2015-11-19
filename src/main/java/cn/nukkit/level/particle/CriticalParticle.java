
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class CriticalParticle extends GenericParticle{
	public CriticalParticle(Vector3 pos){
		this(pos, 2);
	}

	public CriticalParticle(Vector3 pos, int scale){
		super(pos, Particle.TYPE_CRITICAL, scale);
	}
}

package com.trbboi.math;

public class Vector3f {
	public static void normalize( float[] v, int offset ) {
		float dist = dot( v, offset, v, offset );
		dist = (float)Math.sqrt( dist );

		v[0 + offset] /= dist; v[1 + offset] /= dist; v[2 + offset] /= dist;
	}

	public static float dot( float[] v1, int offset1, float[] v2, int offset2 ) {
		return 	v1[0 + offset1] * v2[0 + offset2] + 
						v1[1 + offset1] * v2[1 + offset2] + 
						v1[2 + offset1] * v2[2 + offset2];
	}

	public static void scale( float[] out, int offset, float []v, int voffset, float scale ) {
		out[0 + offset] = v[0 + voffset] * scale;
		out[1 + offset] = v[1 + voffset] * scale;
		out[2 + offset] = v[2 + voffset] * scale;
	}
}

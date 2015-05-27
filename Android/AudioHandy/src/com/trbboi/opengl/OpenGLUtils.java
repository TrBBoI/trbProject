package com.trbboi.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class OpenGLUtils {
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_SHORT = 2;
	public static final int BYTES_PER_INT = 4;

	public static final float []BLACK = { 0f, 0f, 0f, 1f };
	public static final float []GREY = { 0.5f, 0.5f, 0.5f, 1f };
	public static final float []WHITE = { 1f, 1f, 1f, 1f };
	public static final float []RED   = { 1f, 0f, 0f, 1f };
	public static final float []GREEN = { 0f, 1f, 0f, 1f };
	public static final float []BLUE  = { 0f, 0f, 1f, 1f };

	public static final float []LIGHT_RED   = { 0.97265625f, 0.16015625f, 0.16015625f, 1f };
	public static final float []LIGHT_GREEN = { 0.16015625f, 0.97265625f, 0.16015625f, 1f };
	public static final float []LIGHT_BLUE  = { 0.16015625f, 0.16015625f, 0.97265625f, 1f };

	public static final float []DARK_RED   = { 0.5f, 0.0546875f, 0.0546875f, 1f };
	public static final float []DARK_GREEN = { 0.0546875f, 0.5f, 0.0546875f, 1f };
	public static final float []DARK_BLUE  = { 0.0546875f, 0.0546875f, 0.5f, 1f };

	public static final float []LIGHT_YELLOW = { 0.97265625f, 0.97265625f, 0.16015625f, 1f };
	public static final float []LIGHT_ORANGE = { 0.97265625f, 0.6015625f, 0.16015625f, 1f };

	public static final float []DARK_YELLOW = { 0.78125f, 0.78125f, 0.0546875f, 1f };
	public static final float []DARK_ORANGE = { 0.78125f, 0.45703125f, 0.0546875f, 1f };
	
	public static final float []DARK_GREY = { 0.33203125f, 0.33203125f, 0.33203125f, 1f };
	public static final float []LIGHT_GREY = { 0.6640625f, 0.6640625f, 0.6640625f, 1f };

	public static FloatBuffer CreateFloatBuffer( float[] vtx ) {
		FloatBuffer fb;
		ByteBuffer bb = ByteBuffer.allocateDirect( vtx.length * BYTES_PER_FLOAT );
    bb.order( ByteOrder.nativeOrder() );
    fb = bb.asFloatBuffer();
    fb.put( vtx );
    fb.position( 0 );
    return fb;
	}

	public static ShortBuffer CreateShortBuffer( short[] vtx ) {
		ShortBuffer sb;
		ByteBuffer bb = ByteBuffer.allocateDirect( vtx.length * BYTES_PER_SHORT );
    bb.order( ByteOrder.nativeOrder() );
    sb = bb.asShortBuffer();
    sb.put( vtx );
    sb.position( 0 );
    return sb;
	}

	public static IntBuffer CreateIntBuffer( int[] vtx ) {
		IntBuffer ib;
		ByteBuffer bb = ByteBuffer.allocateDirect( vtx.length * BYTES_PER_INT );
    bb.order( ByteOrder.nativeOrder() );
    ib = bb.asIntBuffer();
    ib.put( vtx );
    ib.position( 0 );
    return ib;
	}

	public static String matrixToString( float []mat ) {
		return  
				mat[0] + " " + mat[4] + " " + mat[8] + " " + mat[12] + "\n" +
				mat[1] + " " + mat[5] + " " + mat[9] + " " + mat[13] + "\n" +
				mat[2] + " " + mat[6] + " " + mat[10] + " " + mat[14] + "\n" +
				mat[3] + " " + mat[7] + " " + mat[11] + " " + mat[15] + "\n";
	}

	public static String colorToString( float []mat ) {
		return "R:" + mat[0] + " G:" + mat[1] + " B:" + mat[2] + " A:" + mat[3] + "\n";
	}
}

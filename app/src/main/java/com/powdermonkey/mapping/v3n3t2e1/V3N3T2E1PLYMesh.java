package com.powdermonkey.mapping.v3n3t2e1;

import com.powdermonkey.common.PLYEReader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class V3N3T2E1PLYMesh extends AbstractVBOIndexedV3N3T2E1Mesh {

	private FloatBuffer floatBuffer;
	private IntBuffer intBuffer;
	
	public V3N3T2E1PLYMesh(PLYEReader reader) {
		floatBuffer = reader.getVertices();
		intBuffer = IntBuffer.wrap(reader.getIndices());
	}
	@Override
	protected FloatBuffer getVertices() {
		return floatBuffer;
	}

	@Override
	protected IntBuffer getIndices() {
		return intBuffer;
	}

	@Override
	protected int getIndexCount() {
		return intBuffer.capacity();
	}


}

package com.powdermonkey.mapping.v3n3t2;

import com.powdermonkey.common.PLYReader;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class V3N3T2PLYMesh extends AbstractVBOIndexedV3N3T2Mesh {

	private FloatBuffer floatBuffer;
	private ShortBuffer intBuffer;
	
	public V3N3T2PLYMesh(PLYReader reader) {
		floatBuffer = FloatBuffer.wrap(reader.getVertices());
		intBuffer = ShortBuffer.wrap(reader.getIndices());
	}
	@Override
	protected FloatBuffer getVertices() {
		return floatBuffer;
	}

	@Override
	protected ShortBuffer getIndices() {
		return intBuffer;
	}

	@Override
	protected int getIndexCount() {
		return intBuffer.capacity();
	}


}

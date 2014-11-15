package com.powdermonkey.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class PLYEReader {

	private int vertexCount;
	private int faceCount;
	//private float[] vertices;
    private FloatBuffer verti;
	private int[] faces;

	public PLYEReader(InputStream is) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {

			String nl = reader.readLine();
			if (!nl.equals("ply"))
				throw new IOException("Not a PLY file");

			nl = reader.readLine();
			header: while (nl != null) {
				String[] parts = nl.split(" ");
				if (parts.length > 0) {
					if (parts[0].equals("format")) {
						if (!parts[1].equals("ascii"))
							throw new IOException("PLY reader can only read ascii format");
					} else if (parts[0].equals("property")) {
						// currently not parsed, it is currently assumed the
						// values are x,y,z,nx,ny,nz with optional s,t
					} else if (parts[0].equals("element")) {
						if (parts[1].equals("vertex")) {
							vertexCount = Integer.parseInt(parts[2]);
							System.out.println("Vertex count: " + vertexCount);
						} else if (parts[1].equals("face")) {
							faceCount = Integer.parseInt(parts[2]);
							System.out.println("Face count: " + faceCount);
						}
					} else if (parts[0].equals("end_header")) {
						break header;
					}
				}
				nl = reader.readLine();
			}
			verti = FloatBuffer.allocate(vertexCount * 9);
			for (int i = 0; i < vertexCount; i++) {
                nl = reader.readLine();
                if (nl == null)
                    throw new IOException("Unexpected end of file at vertex: " + i);
                String[] parts = nl.split(" ");
                verti.put(Float.parseFloat(parts[0]));
                verti.put(Float.parseFloat(parts[1]));
                verti.put(Float.parseFloat(parts[2]));
                verti.put(Float.parseFloat(parts[3]));
                verti.put(Float.parseFloat(parts[4]));
                verti.put(Float.parseFloat(parts[5]));
                verti.put(Float.parseFloat(parts[6]));
                verti.put(1.0f-Float.parseFloat(parts[7]));
                verti.put(0);
            }
			faces = new int[faceCount * 3];
            int rfc = 0;
			for (int i = 0; i < faceCount; i++) {
                nl = reader.readLine();
                if (nl == null)
                    throw new IOException("Unexpected end of file at vertex: " + i);
                String[] parts = nl.split(" ");
                if (parts.length == 5) {
                    int a = Integer.parseInt(parts[1]);
                    int b = Integer.parseInt(parts[2]);
                    int c = Integer.parseInt(parts[3]);
                    int d = Integer.parseInt(parts[4]);
                    ensureFaces(rfc);
                    faces[rfc * 3 + 0] = a;
                    verti.put(a * 9 + 8, rfc);
                    faces[rfc * 3 + 1] = b;
                    verti.put(b * 9 + 8, rfc);
                    faces[rfc * 3 + 2] = c;
                    verti.put(c * 9 + 8, rfc);
                    rfc++;
                    ensureFaces(rfc);
                    faces[rfc * 3 + 0] = c;
                    verti.put(c * 9 + 8, rfc);
                    faces[rfc * 3 + 1] = d;
                    verti.put(d * 9 + 8, rfc);
                    faces[rfc * 3 + 2] = a;
                    verti.put(a * 9 + 8, rfc);
                    rfc++;


                } else if (parts.length == 4) {
                    int a = Integer.parseInt(parts[1]);
                    int b = Integer.parseInt(parts[2]);
                    int c = Integer.parseInt(parts[3]);
                    faces[rfc * 3 + 0] = a;
                    verti.put(a * 9 + 8, rfc);
                    faces[rfc * 3 + 1] = b;
                    verti.put(b * 9 + 8, rfc);
                    faces[rfc * 3 + 2] = c;
                    verti.put(c * 9 + 8, rfc);
                    rfc++;
                }
			}
            faceCount = rfc;
            if(faces.length != faceCount) {
                int[] tmp = new int[faceCount * 3];
                System.arraycopy(faces, 0, tmp, 0, tmp.length);
                faces = tmp;
            }
		} finally {
			reader.close();
		}
        verti.rewind();
	}

    private void ensureFaces(int current) {
        if(faces.length < (current + 1) * 3) {
            int[] tmp = new int[current * 6];
            System.arraycopy(faces, 0, tmp, 0, faces.length);
            faces = tmp;
        }
    }

	public static void main(String[] args) {
		try {
			new PLYEReader(new FileInputStream(new File("test.ply")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public int getFaceCount() {
		return this.faceCount;
	}
	
	public FloatBuffer getVertices() {
		return verti;
	}
	
	public int[] getIndices() {
		return faces;
	}
}

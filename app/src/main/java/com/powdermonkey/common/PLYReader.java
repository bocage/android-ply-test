package com.powdermonkey.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PLYReader {

	private int vertexCount;
	private int faceCount;
	private float[] vertices;
	private int[] faces;

	public PLYReader(InputStream is) throws IOException {

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
			vertices = new float[vertexCount * 8];
			for (int i = 0; i < vertexCount; i++) {
				nl = reader.readLine();
				if (nl == null)
					throw new IOException("Unexpected end of file at vertex: " + i);
				String[] parts = nl.split(" ");
				vertices[i * 8 + 0] = Float.parseFloat(parts[0]);
				vertices[i * 8 + 1] = Float.parseFloat(parts[1]);
				vertices[i * 8 + 2] = Float.parseFloat(parts[2]);
				vertices[i * 8 + 3] = Float.parseFloat(parts[3]);
				vertices[i * 8 + 4] = Float.parseFloat(parts[4]);
				vertices[i * 8 + 5] = Float.parseFloat(parts[5]);
				
				if(parts.length > 6) {
					vertices[i * 8 + 6] = Float.parseFloat(parts[6]);
					vertices[i * 8 + 7] = Float.parseFloat(parts[7]);
				}
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
                    faces[rfc * 3 + 1] = b;
                    faces[rfc * 3 + 2] = c;
                    rfc++;
                    ensureFaces(rfc);
                    faces[rfc * 3 + 0] = c;
                    faces[rfc * 3 + 1] = d;
                    faces[rfc * 3 + 2] = a;
                    rfc++;


                } else if (parts.length == 4) {
                    faces[rfc * 3 + 0] = Integer.parseInt(parts[1]);
                    faces[rfc * 3 + 1] = Integer.parseInt(parts[2]);
                    faces[rfc * 3 + 2] = Integer.parseInt(parts[3]);
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
			new PLYReader(new FileInputStream(new File("test.ply")));
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
	
	public float[] getVertices() {
		return vertices;
	}
	
	public int[] getIndices() {
		return faces;
	}
}

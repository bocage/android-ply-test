uniform mat4 u_MVPMatrix;		// constant representing the combined model/view/projection matrix.      		       
uniform mat4 u_MVMatrix;		// constant representing the combined model/view matrix.  
uniform vec4 u_Color;			// color information.
uniform float u_Alpha;			// animation indicator.

attribute vec4 a_Position;		// Per-vertex position information we will pass in.   				
attribute vec3 a_Normal;		// Per-vertex normal information we will pass in.      
attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information we will pass in. 		
attribute float a_Enum;         // Per-vertex face enumerator.

varying vec3 v_Position;		// This will be passed into the fragment shader.       		
varying vec4 v_Color;			// This will be passed into the fragment shader.          		
varying vec3 v_Normal;			// This will be passed into the fragment shader.  
varying vec2 v_TexCoordinate;   // This will be passed into the fragment shader.    		

const float SCALE = 300.0;
// The entry point for our vertex shader.  
void main()                                                 	
{

//mat4 rotation = mat4(
//        vec4(1.0,         0.0,         0.0, 0.0),
//        vec4(0.0,  cos(u_Alpha * a_Enum),  sin(u_Alpha * a_Enum), 0.0),
//        vec4(0.0, -sin(u_Alpha * a_Normal.x),  cos(u_Alpha * a_Normal.y), 0.0),
//        vec4(0.0,         0.0,         0.0, 1.0)
//    );

float tr = 0.0; //u_Alpha * 0.05 * mod(a_Enum + 1.0, 10.0);
float tt = 1.0; //u_Alpha * 1.01;

mat4 rotation = mat4(
        vec4(1.0,         0.0,         0.0, 0.0),
        vec4(0.0,  cos(tr),  sin(tr), 0.0),
        vec4(0.0, -sin(tr),  cos(tr), 0.0),
        vec4(0.0,         0.0,         0.0, 1.0)
    );

//gl_PointSize = 4.0;
	// Transform the vertex into eye space.
	v_Position = vec3(u_MVMatrix * rotation * a_Position);

	// Pass through the color.
	v_Color = u_Color;

	// Pass through the texture coordinate.
	v_TexCoordinate = a_TexCoordinate;

	// Transform the normal's orientation into eye space.
    v_Normal = vec3(u_MVMatrix * rotation * vec4(a_Normal, 0.0));
          
	// gl_Position is a special variable used to store the final position.
	// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
	gl_Position = u_MVPMatrix * rotation * a_Position;
}                                                          
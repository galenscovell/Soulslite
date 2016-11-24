#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    vec3 white = texColor.rgb + vec3(0.75f, 0.75f, 0.75f);
    texColor.rgb = white;
    
    gl_FragColor = texColor * v_color;
}
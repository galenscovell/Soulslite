#ifdef GL_ES
#define LOW lowp
#define MED mediump
#define HIGH highp
    precision mediump float;
#else
#define MED
#define LOW
#define HIGH
#endif

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoords;

const int speed = 2, frequency = 25, amplitude = 1;

#define ALPHA_VALUE_BORDER 0.5


void main() {
    vec2 T = v_texCoord.xy;

    float alpha = 0.0;
    bool allin = true;
    for( float ix = -u_offset; ix < u_offset; ix += u_step )
    {
      for( float iy = -u_offset; iy < u_offset; iy += u_step )
       {
          float newAlpha = texture2D(u_texture, T + vec2(ix, iy) * u_viewportInverse).a;
          allin = allin && newAlpha > ALPHA_VALUE_BORDER;
          if (newAlpha > ALPHA_VALUE_BORDER && newAlpha >= alpha)
          {
             alpha = newAlpha;
          }
      }
    }
    if (allin)
    {
      alpha = 0.0;
    }

    gl_FragColor = vec4(u_color,alpha);
}
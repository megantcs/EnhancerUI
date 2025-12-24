#version 150

uniform vec4 color1;
uniform vec4 color2;
uniform vec4 color3;
uniform vec4 color4;

uniform vec2 uSize;
uniform vec2 uLocation;

uniform float radius;
uniform float borderWidth;
uniform float borderAlpha;

out vec4 fragColor;

float roundedBoxSDF(vec2 center, vec2 size, float radius) {
    return length(max(abs(center) - size + radius, 0.0)) - radius;
}

vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){
    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
    color += mix(0.0019607843, -0.0019607843, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));
    return color;
}

void main() {
    vec2 fragCoord = gl_FragCoord.xy - uLocation - (uSize / 2.0);

    float outerDistance = roundedBoxSDF(fragCoord, uSize / 2.0, radius);
    float innerDistance = roundedBoxSDF(fragCoord, (uSize / 2.0) - borderWidth, max(0.0, radius - borderWidth));
    float borderMask = smoothstep(-1.0, 1.0, innerDistance) - smoothstep(-1.0, 1.0, outerDistance);

    float smoothedAlpha = borderMask * borderAlpha;

    if (smoothedAlpha < 0.01) {
        discard;
    }

    vec2 gradientCoords = (gl_FragCoord.xy - uLocation) / uSize;
    vec3 borderColor = createGradient(gradientCoords, color1.rgb, color2.rgb, color3.rgb, color4.rgb);

    fragColor = vec4(borderColor, smoothedAlpha);
}
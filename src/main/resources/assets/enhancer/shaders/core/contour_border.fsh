#version 150

uniform vec4 borderColor;
uniform vec2 uSize;
uniform vec2 uLocation;

uniform float radius;
uniform float borderWidth;
uniform float dashSize;
uniform float gapSize;

out vec4 fragColor;

float sdBox(vec2 p, vec2 b) {
    vec2 d = abs(p) - b;
    return length(max(d, 0.0)) + min(max(d.x, d.y), 0.0);
}

float sdRoundedBox(vec2 p, vec2 b, float r) {
    if (r <= 0.001) return sdBox(p, b);
    vec2 d = abs(p) - b + r;
    return length(max(d, 0.0)) - r;
}

void main() {
    vec2 p = gl_FragCoord.xy - uLocation - (uSize / 2.0);
    vec2 b = uSize / 2.0;

    float d_outer, d_inner;

    if (radius <= 0.001) {
        d_outer = sdBox(p, b);
        d_inner = sdBox(p, b - borderWidth);
    } else {
        d_outer = sdRoundedBox(p, b, radius);
        d_inner = sdRoundedBox(p, b - borderWidth, max(0.0, radius - borderWidth));
    }

    float border = smoothstep(-0.5, 0.5, d_inner) - smoothstep(-0.5, 0.5, d_outer);
    if (border <= 0.0) discard;

    float dash = 1.0;

    if (radius <= 0.001) {
        vec2 absP = abs(p);

        float distToVertical = min(abs(b.x - absP.x), absP.x);
        float distToHorizontal = min(abs(b.y - absP.y), absP.y);

        if (distToVertical < distToHorizontal) {
            float pos = p.y + b.y;
            if (p.x > 0) pos += 2.0 * b.y;

            float total = dashSize + gapSize;
            float pattern = mod(pos, total);
            dash = step(pattern, dashSize);
        } else {
            float pos = p.x + b.x;
            if (p.y > 0) pos += 2.0 * b.x;


            float total = dashSize + gapSize;
            float pattern = mod(pos, total);
            dash = step(pattern, dashSize);
        }

    } else {
        vec2 cornerArea = b - radius;
        vec2 absP = abs(p);

        bool onStraightSide = (absP.y > cornerArea.y && absP.x <= cornerArea.x) ||
                             (absP.x > cornerArea.x && absP.y <= cornerArea.y);

        if (onStraightSide) {
            float pos = 0.0;

            if (absP.y > cornerArea.y) {
                if (p.y > 0) {
                    pos = p.x + b.x;
                } else {
                    pos = -p.x + b.x;
                }
            } else {
                if (p.x > 0) {
                    pos = -p.y + b.y;
                } else {
                    pos = p.y + b.y;
                }
            }

            float total = dashSize + gapSize;
            float pattern = mod(pos, total);
            dash = step(pattern, dashSize);
        }
    }

    float alpha = borderColor.a * border * dash;
    if (alpha < 0.1) discard;

    fragColor = vec4(borderColor.rgb, alpha);
}
#version 150

uniform vec4 ColorModulator;

in float vertexDistance;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = vertexColor * ColorModulator;
    fragColor = color;
}

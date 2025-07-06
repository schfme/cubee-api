package me.schf.cubee.api.model;

public record Shear(double xy, double xz, double yx, double yz, double zx, double zy) implements Transformation {
}

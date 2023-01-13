
abstract class Shape {

    protected double side1;
    protected double side2;
    protected double side3;
    protected double radius;


    public Shape(double side1, double side2, double side3) {
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    public Shape(double side1, double side2) {
        this.side1 = side1;
        this.side2 = side2;
    }

    public Shape(double radius) {
        this.radius = radius;
    }

    abstract double getPerimeter();

    abstract double getArea();
}

class Triangle extends Shape {
 
    public Triangle(double side1, double side2, double side3) {
        super(side1, side2, side3);
    }

    @Override
    double getPerimeter() {
        return side1 + side2 + side3;
    }

    @Override
    double getArea() {
        double s = (side1 + side2 + side3) / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }
}

class Rectangle extends Shape {

    public Rectangle(double side1, double side2) {
        super(side1, side2);
    }

    @Override
    double getPerimeter() {
        return 2 * (side1 + side2);
    }

    @Override
    double getArea() {
        return side1 * side2;
    }
}

class Circle extends Shape {

    public Circle(double radius) {
        super(radius);
    }

    @Override
    double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }
}

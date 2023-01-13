
class Complex {

    double real;
    double image;

    public void add(Complex anotherNumber) {
        this.real += anotherNumber.real;
        this.image += anotherNumber.image;
    }

    public void subtract(Complex anotherNumber) {
        this.real -= anotherNumber.real;
        this.image -= anotherNumber.image;
    }
}

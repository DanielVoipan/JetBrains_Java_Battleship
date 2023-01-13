class Main {

    int yearModel;
    String make;
    int speed;
    
    public void accelerate() {
        this.speed = this.speed + 5;
    }
    public void brake() {
        if (this.speed > 4) {
            this.speed = this.speed - 5;
        }
    }
    public int getSpeed () {
        return speed;
    }
    public static void main(String[] args) {
        Main car = new Main();
        car.accelerate();
        car.accelerate();
        car.brake();
        car.accelerate();
        car.brake();
        car.brake();
        car.brake();

        System.out.println(car.getSpeed());
    }
}

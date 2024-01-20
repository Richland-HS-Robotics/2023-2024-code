package org.firstinspires.ftc.greenTeamCode.util;

public class Vector3 {
    private double x;
    private double y;
    private double z;



    public Vector3(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Vector3(){
        this.x=0;
        this.y=0;
        this.z=0;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean equals(Vector3 obj){
        return
                this.x == obj.getX() &&
                this.y == obj.getY() &&
                this.z == obj.getZ();

    }


    public void addTo(Vector3 v){
        this.x += v.getX();
        this.y += v.getY();
        this.z += v.getZ();
    }


    public Vector3 add(Vector3 v){
        return new Vector3(this.x+v.getX(),this.y+v.getY(),this.z+v.getZ());
    }


    public void subtractFrom(Vector3 v){
        this.x -= v.getX();
        this.y -= v.getY();
        this.z -= v.getZ();
    }


    public Vector3 subtract(Vector3 v){
        return new Vector3(this.x+v.getX(),this.y+v.getY(),this.z+v.getZ());
    }


}

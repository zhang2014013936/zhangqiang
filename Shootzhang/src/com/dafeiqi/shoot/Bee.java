package com.dafeiqi.shoot;

import java.util.Random;

public class Bee extends FlyingObject implements Award {
    private int xSpeed = 1;
    private int ySpeed = 2;
    private int awardType;

    public Bee() {
        this.image = ShootGame.bee;
        height = image.getHeight();
        y=-height;
        Random rand = new Random();
         x=rand.nextInt(ShootGame.WIDTH-width);
        //x = 100;
       // y = 200;
        awardType = rand.nextInt(2);
    }

    @Override
    public int getType() {
        return awardType;
    }

    @Override
    public void step() {
        x += xSpeed;
        y += ySpeed;
        if (x > ShootGame.WIDTH - width) {
            xSpeed = -1;
        }
        if (x < 0) {
            xSpeed = 1;
        }

    }
    @Override
    public boolean outofBounds(){
        return y>ShootGame.HEIGHT;
    }
}

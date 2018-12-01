package com.dafeiqi.shoot;

/**
 * 敌飞机：是飞行物，也是敌人
 */
public class Airplane extends FlyingObject implements Enemy {
    private int speed =2;
    public Airplane(){
        this.image=ShootGame.airplane;
        width=image.getWidth();
        height=image.getHeight();
        y=-height;
        x=(int)(Math.random()*(ShootGame.WIDTH-width));
        //y=100;
       // x=100;
    }
    public int getScore() {
        return 5;
    }

    public void step(){
        y+=speed;
    }

    @Override
    public boolean outofBounds(){
        return y>ShootGame.HEIGHT;
    }
    }


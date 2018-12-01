package com.dafeiqi.shoot;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.util.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.Arrays;
import javax.swing.JPanel;
import java.util.TimerTask;
import javax.swing.JFrame;


public class ShootGame extends JPanel {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 654;
    private int state;
    public static final int START=0;
    public static final int RUNNING=1;
    public static final int PAUSE=2;
    public static final int GAME_OVER=3;
    private int score =0;
    private Timer timer;
    private int intervel =1000/100;
    public static BufferedImage background;
    public static BufferedImage start;
    public static BufferedImage airplane;
    public static BufferedImage bee;
    public static BufferedImage bullet;
    public static BufferedImage hero0;
    public static BufferedImage hero1;
    public static BufferedImage pause;
    public static BufferedImage gameover;
    private Hero hero = new Hero();
    private FlyingObject[] flyings={};
    private Bullet[] bullets={};
    public ShootGame() {
        flyings = new FlyingObject[2];
        flyings[0] = new Airplane();
        flyings[1] = new Bee();
        bullets = new Bullet[1];
        bullets[0] = new Bullet(200, 350);
    }

    static{
        try {
            background = ImageIO.read(ShootGame.class.getResource("background.png"));
            airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
            bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
            bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
            hero0 = ImageIO.read(ShootGame.class.getResource("hero2.png"));
            hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
            pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
            gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        paintHero(g);
        paintBullets(g);
        paintFlyingObjects(g);
        paintScore(g);
        paintState(g);

    }

    private void paintHero(Graphics g) {
        g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);

    }

    public void paintFlyingObjects(Graphics g) {
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            g.drawImage(f.getImage(), f.getX(), f.getY(), null);
        }
    }

    public void paintBullets(Graphics g) {
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            g.drawImage(b.getImage(), b.getX(), b.getY(), null);
        }
    }


    public void paintScore(Graphics g){
        int x=10;
        int y=25;
        Font font =new Font(Font.SANS_SERIF,Font.BOLD,14);
        g.setColor(new Color(0x3A3B3B));
        g.setFont(font);
        g.drawString("SCORE:"+score,x,y);
        y+=20;
        g.drawString("LIFE:"+hero.getLife(),x,y);
    }

    public void paintState(Graphics g){
        switch (state){
            case START:
                g.drawImage(start,0,0,null);
                break;
            case PAUSE:
                g.drawImage(pause,0,0,null);
                break;
            case GAME_OVER:
                g.drawImage(gameover,0,0,null);
                break;
        }
    }

        public static void main (String[] args){
            JFrame frame = new JFrame("Fly");
            ShootGame game = new ShootGame();
            frame.add(game);
            frame.setSize(WIDTH, HEIGHT);
            frame.setAlwaysOnTop(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            game.action();

        }
    public void action(){// 启动程序代码
        MouseAdapter mouseAdapter1 =new MouseAdapter(){//鼠标移动
            @Override
            public void mouseMoved(MouseEvent e){
                if(state==RUNNING){// 运行时移动英雄机
                    int x=e.getX();
                    int y=e.getY();
                    hero.moveTo(x,y);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e){  //鼠标进入
                if(state==PAUSE){// 暂停时运行
                    state=RUNNING;
                }
            }
            @Override
            public void mouseExited(MouseEvent e){ //鼠标退出
                if(state!=GAME_OVER){
                    state=PAUSE;// 游戏未结束，则设置为暂停
                }
            }
            @Override
            public void mouseClicked(MouseEvent e){ //鼠标点击
                switch (state){
                    case START:
                        state=RUNNING;
                        break;
                    case GAME_OVER:  // 游戏结束，清理现场
                        flyings =new FlyingObject[0];
                        bullets =new Bullet[0];
                        hero=new Hero();
                        score=0;
                        state=START;
                        break;
                }
            }
        };
        this.addMouseListener(mouseAdapter1);     // 处理鼠标点击操作
        this.addMouseMotionListener(mouseAdapter1);  // 处理鼠标滑动操作
        timer = new Timer();    // 主流程控制
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                if(state==RUNNING) {
                    enterAction();  // 飞行物入场
                    stepAction();   // 走一步
                    shootAction();   // 射击
                    bangAction();  // 子弹打飞行物
                    outofBoundsAtion();  // 删除越野飞行物及子弹
                    checkGameOverAction(); //检查游戏结束
                }
                repaint();   //  重绘，调用paint()方法
            }
        },intervel,intervel);

    }

    /**
     * 随机生成飞行物
     * @return  飞行物对象
     */

    public static FlyingObject nextOne(){
        Random random =new Random();
        int type =random.nextInt(20);
        if(type==0){
            return  new Bee();
        }else{
            return  new Airplane();
        }
    }
    int flyEnteredIndex=0;  // 飞行物入场计数
    /**飞行物入场*/
    public void enterAction(){

        flyEnteredIndex++;
        if(flyEnteredIndex %40 ==0){  //400毫秒－－10*40
            FlyingObject obj =nextOne();  // 随机生成一个飞行物
            flyings =Arrays.copyOf(flyings,flyings.length+1);
            flyings[flyings.length-1]=obj;
        }
    }
    public void stepAction(){
        /** 飞行物走一步*/
        for(int i=0;i<flyings.length;i++){
            FlyingObject f =flyings[i];
            f.step();
        }
        for(int i=0;i<bullets.length;i++){
            Bullet b =bullets[i];
            b.step();
        }
        hero.step();
    }
    int shootIndex=0;   // 射击计数
    public void shootAction(){
        shootIndex++;
        if(shootIndex % 30==0){    // 100毫秒发一颗
            Bullet[] bs = hero.shoot();  //英雄打出子弹
            bullets=Arrays.copyOf(bullets,bullets.length+bs.length);// 扩容
            System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);  // 追加数组

        }
    }

    /**
     *     子弹与飞行物之间的碰撞检测
     */


    public void bangAction() {
            for (int i = 0; i < bullets.length; i++) {  // 遍历所有子弹
                Bullet b = bullets[i];
                bang(b);
            }
        }

    /**
     *     子弹和飞行物之间的碰撞检测
      */

    public  void bang(Bullet bullet) {
        int index=-1;  // 击中的飞行物索引
        for(int i=0;i<flyings.length;i++){
            FlyingObject obj =flyings[i];
            if(obj.shootBy(bullet)){  // 判断是否击中
                index=i;  // 记录被击中的飞行物的索引
                break;
            }
        }
        if(index!=-1){   //有击中的飞行物
            FlyingObject one =flyings[index];  //记录被击中的飞行物
            FlyingObject temp =flyings[index];  // 被击中的飞行物与最后一个飞行物交换
            flyings[index]=flyings[flyings.length-1];
            flyings[flyings.length-1]=temp;
            flyings=Arrays.copyOf(flyings,flyings.length-1);  // 删除最后一个飞行物
            if(one instanceof Enemy){    // 检查类型， 是敌人，则加分
                Enemy e=(Enemy)one;  //强制类型转换
                score+=e.getScore();  //加分
            }
            if(one instanceof Award){  // 若为奖励，设置奖励
                Award a=(Award) one;
                int type =a.getType(); //  获取奖励类型
                switch (type){
                    case Award.DOUBLE_FIRE:
                    hero.addDoubleFire();   // 设置双倍火力
                    break;
                    case Award.LIFE:
                        hero.addLife();  //设置加命
                        break;
                }

            }
        }
    }

    public void outofBoundsAtion(){
        int index=0;
        FlyingObject[] flyingLives =new FlyingObject[flyings.length];  // 活着的飞行物
        for (int i=0;i<flyings.length;i++){
            FlyingObject f=flyings[i];
            if(!f.outofBounds()){
                flyingLives[index++]=f;  // 不越野留着
            }
        }
        flyings =Arrays.copyOf(flyingLives,index);   // 将不越野的飞行物留着
        index=0; //重置为0
        Bullet[] bulletLives =new Bullet[bullets.length];
        for(int i=0;i<bullets.length;i++){
            Bullet b=bullets[i];
            if(!b.outofBounds()){
                bulletLives[index++]=b;
            }
        }
        bullets =Arrays.copyOf(bulletLives,index); // 将不越野的留着
    }

    public void checkGameOverAction(){
        if(isGameOver()){
            state =GAME_OVER;  // 改变状态
        }
    }

    public boolean isGameOver(){
        for(int i=0;i<flyings.length ;i++){
            int index = -1;
            FlyingObject obj =flyings[i];
            if(hero.hit(obj)){   //检查英雄与飞行物是否碰撞
                hero.subtractLife();
                hero.setDoubleFire(0);
                index =i;
            }
            if(index!=-1){
                FlyingObject t=flyings[index];
                flyings[index] =flyings[flyings.length-1];
                flyings[flyings.length-1]=t;
                flyings=Arrays.copyOf(flyings,flyings.length-1);
            }
        }
        return  hero.getLife()<=0;
    }


}


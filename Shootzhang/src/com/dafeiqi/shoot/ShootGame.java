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
    public void action(){// �����������
        MouseAdapter mouseAdapter1 =new MouseAdapter(){//����ƶ�
            @Override
            public void mouseMoved(MouseEvent e){
                if(state==RUNNING){// ����ʱ�ƶ�Ӣ�ۻ�
                    int x=e.getX();
                    int y=e.getY();
                    hero.moveTo(x,y);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e){  //������
                if(state==PAUSE){// ��ͣʱ����
                    state=RUNNING;
                }
            }
            @Override
            public void mouseExited(MouseEvent e){ //����˳�
                if(state!=GAME_OVER){
                    state=PAUSE;// ��Ϸδ������������Ϊ��ͣ
                }
            }
            @Override
            public void mouseClicked(MouseEvent e){ //�����
                switch (state){
                    case START:
                        state=RUNNING;
                        break;
                    case GAME_OVER:  // ��Ϸ�����������ֳ�
                        flyings =new FlyingObject[0];
                        bullets =new Bullet[0];
                        hero=new Hero();
                        score=0;
                        state=START;
                        break;
                }
            }
        };
        this.addMouseListener(mouseAdapter1);     // �������������
        this.addMouseMotionListener(mouseAdapter1);  // ������껬������
        timer = new Timer();    // �����̿���
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                if(state==RUNNING) {
                    enterAction();  // �������볡
                    stepAction();   // ��һ��
                    shootAction();   // ���
                    bangAction();  // �ӵ��������
                    outofBoundsAtion();  // ɾ��ԽҰ�����Ｐ�ӵ�
                    checkGameOverAction(); //�����Ϸ����
                }
                repaint();   //  �ػ棬����paint()����
            }
        },intervel,intervel);

    }

    /**
     * ������ɷ�����
     * @return  ���������
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
    int flyEnteredIndex=0;  // �������볡����
    /**�������볡*/
    public void enterAction(){

        flyEnteredIndex++;
        if(flyEnteredIndex %40 ==0){  //400���룭��10*40
            FlyingObject obj =nextOne();  // �������һ��������
            flyings =Arrays.copyOf(flyings,flyings.length+1);
            flyings[flyings.length-1]=obj;
        }
    }
    public void stepAction(){
        /** ��������һ��*/
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
    int shootIndex=0;   // �������
    public void shootAction(){
        shootIndex++;
        if(shootIndex % 30==0){    // 100���뷢һ��
            Bullet[] bs = hero.shoot();  //Ӣ�۴���ӵ�
            bullets=Arrays.copyOf(bullets,bullets.length+bs.length);// ����
            System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);  // ׷������

        }
    }

    /**
     *     �ӵ��������֮�����ײ���
     */


    public void bangAction() {
            for (int i = 0; i < bullets.length; i++) {  // ���������ӵ�
                Bullet b = bullets[i];
                bang(b);
            }
        }

    /**
     *     �ӵ��ͷ�����֮�����ײ���
      */

    public  void bang(Bullet bullet) {
        int index=-1;  // ���еķ���������
        for(int i=0;i<flyings.length;i++){
            FlyingObject obj =flyings[i];
            if(obj.shootBy(bullet)){  // �ж��Ƿ����
                index=i;  // ��¼�����еķ����������
                break;
            }
        }
        if(index!=-1){   //�л��еķ�����
            FlyingObject one =flyings[index];  //��¼�����еķ�����
            FlyingObject temp =flyings[index];  // �����еķ����������һ�������ｻ��
            flyings[index]=flyings[flyings.length-1];
            flyings[flyings.length-1]=temp;
            flyings=Arrays.copyOf(flyings,flyings.length-1);  // ɾ�����һ��������
            if(one instanceof Enemy){    // ������ͣ� �ǵ��ˣ���ӷ�
                Enemy e=(Enemy)one;  //ǿ������ת��
                score+=e.getScore();  //�ӷ�
            }
            if(one instanceof Award){  // ��Ϊ���������ý���
                Award a=(Award) one;
                int type =a.getType(); //  ��ȡ��������
                switch (type){
                    case Award.DOUBLE_FIRE:
                    hero.addDoubleFire();   // ����˫������
                    break;
                    case Award.LIFE:
                        hero.addLife();  //���ü���
                        break;
                }

            }
        }
    }

    public void outofBoundsAtion(){
        int index=0;
        FlyingObject[] flyingLives =new FlyingObject[flyings.length];  // ���ŵķ�����
        for (int i=0;i<flyings.length;i++){
            FlyingObject f=flyings[i];
            if(!f.outofBounds()){
                flyingLives[index++]=f;  // ��ԽҰ����
            }
        }
        flyings =Arrays.copyOf(flyingLives,index);   // ����ԽҰ�ķ���������
        index=0; //����Ϊ0
        Bullet[] bulletLives =new Bullet[bullets.length];
        for(int i=0;i<bullets.length;i++){
            Bullet b=bullets[i];
            if(!b.outofBounds()){
                bulletLives[index++]=b;
            }
        }
        bullets =Arrays.copyOf(bulletLives,index); // ����ԽҰ������
    }

    public void checkGameOverAction(){
        if(isGameOver()){
            state =GAME_OVER;  // �ı�״̬
        }
    }

    public boolean isGameOver(){
        for(int i=0;i<flyings.length ;i++){
            int index = -1;
            FlyingObject obj =flyings[i];
            if(hero.hit(obj)){   //���Ӣ����������Ƿ���ײ
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


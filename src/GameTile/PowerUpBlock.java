package GameTile;

import GameEntity.RedMushroom;
import GameGFX.Sprite;
import Mario.DeadObject;
import Mario.Game;
import Mario.Handler;
import Mario.Id;

import java.awt.*;
import java.util.Random;


public class PowerUpBlock extends Tile {
    private Sprite powerUp;
    //   boolean poppedUp = false;
    private int hitsTaken = 0, spriteY = getY(), coinsGiven, deathCounter = 0;
    private String powerUpName;
    private boolean hasCoin, hasMushroom, change = false, normalState = true, activeHelper = false,mushroomGiven=false;
    private Random random = new Random();


    public PowerUpBlock(int x, int y, int width, int height, boolean solid, Id id, Handler handler, Sprite powerUp, String powerUpname,int tg) {
        super(x, y, width, height, solid, id, handler,tg);
        this.powerUp = Game.dummy;
        this.powerUpName = powerUpname;


        long temp = System.currentTimeMillis() % 2;
        hasCoin = temp > 0;

      

        temp = random.nextInt(100);
        hasMushroom = temp > 80;



    }



    public void addHit() {


        hitsTaken++;
        System.out.println("hit = " + hitsTaken);

        if (hitsTaken < 4)
        {
            change = true;
            DeadObject deadObject=new DeadObject(x,y,Id.powerUp,hitsTaken);
            Handler.deadThings.add(deadObject);
        }

    }

    @Override
    public void render(Graphics g) {


        if (!normalState) {


            g.drawImage(powerUp.getBufferedImage(), x, spriteY, width, height, null);
        }

        if (!activated)
            g.drawImage(Game.specialBrick.getBufferedImage(), x, y, width, height, null);

        if (hitsTaken >= 3) {
            if(hasMushroom && mushroomGiven==false){
                tick();
                return;
            }
        //    System.out.println("in destoryed " + activated + " " + hitsTaken);
            g.drawImage(Game.destroyedSpecialBreak.getBufferedImage(), x, y, width, height, null);
            deathCounter++;

            if (deathCounter == 60)
                super.die();
        }


    }


    @Override
    public void tick() {


        if (Game.paused)
            return;
//        if(poppedUp)
//            JOptionPane.showMessageDialog(null,"poppped up");
        if (change) {

            normalState = false;
            //     System.out.println(false);
            //   change=false;

            if (hasCoin) {
                powerUp = Game.coin;
                spriteY -= 6;
            }


            spriteY--;


            //INja farz jardi power up ha faghat gharch and
            if (spriteY < y - height) {
//                JOptionPane.showMessageDialog(null,"pre trig");
                //implement flower

                normalState = true;
                spriteY = y;
                //   System.out.println(true);


//
//                if (powerUpName.equals("GREEN"))
//                    handler.addEntity(new GreenMushroom(x, spriteY, width, height, Id.greenMushroom, handler));
//
//                if (powerUpName.equals("FLOWER"))
//                    handler.addEntity(new FireFlower(x, spriteY, width, height, Id.fireFlower, handler));

                if (hasCoin) {

                    Game.coins++;
                    coinsGiven++;

                }
                // handler.addTile(new Coin(x,spriteY,width,height,true,Id.coin,handler));
                //   addTile(new Coin(x*64,y*64,64,64,true,Id.coin,this));

//                JOptionPane.showMessageDialog(null,"Triggered");
                change = false;

                if ((hasMushroom && coinsGiven == 3) || (hasMushroom && hasCoin == false && hitsTaken >= 3)) {

                 //   System.out.println("BINGO");
                    spriteY = getY();
                    activated = true;
                    powerUp = Game.redMushroom;
                    mushroomGiven=true;
                    handler.addEntity(new RedMushroom(x, spriteY, width, height, Id.redMushroom, handler,0));
                    handler.addedRedMushroomX.add(x);
                    handler.addedRedMushroomY.add(spriteY);

                } else if (hitsTaken == 3) {
                    activated = true;
                }
            }


        }

    }

    public boolean isActivated() {
        return activated;
    }

    public int getHitsTaken() {
        return hitsTaken;
    }

    public void setHitsTaken(int hitsTaken) {
        this.hitsTaken = hitsTaken;
    }

    public boolean isHasCoin() {
        return hasCoin;
    }

    public boolean isHasMushroom() {
        return hasMushroom;
    }

    public void setHasCoin(boolean hasCoin) {
        this.hasCoin = hasCoin;
    }

    public void setHasMushroom(boolean hasMushroom) {
        this.hasMushroom = hasMushroom;
    }
}

package Mario;
//27 boss
//23 , 24 pipes
//28, Menus and hframe and icons
//30 Drawing and ticking optimization
//31 & 32 koopas
//34 Plants!
//ye marhale ham oon parcham va raftan marhale baad
//37 Soundswwwww
//38 Background levelsImage
//39 Fire balls

import Broadcast.ReaderClient;
import Broadcast.SenderClient;
import GameEntity.Enemy.KoopaState;
import GameEntity.Entity;
import GameEntity.Player;
import GameEntity.RedMushroom;
import GameGFX.Sprite;
import GameGFX.SpriteSheet;
import GameTile.PowerUpBlock;
import Mario.Input.KeyInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Game extends Canvas implements Runnable {


    // public static final int WITDH = 240;
    public static final int WITDH = 270;
    //  public static final int HEIGHT = WITDH / 14 * 10;
    //Height is y
    public static final int HEIGHT = WITDH / 16 * 10;
    public static final int SCALE = 4;
    public static final String TITLE = "Super Mario Bros (HOSSEIN)";
    public static SpriteSheet sheet;
    public static Handler handler;
    public static Sprite grass, greenMushroom, redMushroom, dummy, powerUp, specialBrick, destroyedSpecialBreak, destroyedBrick, ordinaryBrick, destroyedOrdinaryBrick, stair, usedPowerUp, pipeBody, coin, castleBrick, star, castleDoor, prince, fireBall, fireFlower;
    public static Sprite player[][][] = new Sprite[2][3][12];//first index is status, second is frame
    public static Camera cam;
    public static String host = "127.0.0.1";
    public static Sprite[] goomba = new Sprite[8], koopa = new Sprite[8], plant = new Sprite[2], hedgehog = new Sprite[4];
    private ArrayList<BufferedImage> levelsImage = new ArrayList<>();
    private static int deathScreenTime = 0, gameOverTicks, numberOfMaps = 4, currentLevel = 2, endGame;
    public static int coins, lives = 3, fireBalls = 5, savedCoins, playerIndex = 0, port = 50000,FPS=40;
    public static boolean startNext = false, totallyFinished = false, paused = false, showScoreScreen;
    public static JFrame frame;
    private static List<String> allLines;
    //private static ArrayList<String> allLines;
    public static Game game;
    // private sboolean totallyFinished=false;

    //he 10 you 10

    private Thread thread;
    private static boolean running = false;
    public static boolean showDeathScreen = true, gameOver = false;

    private void init() {
        handler = new Handler();
        //   sheet = new SpriteSheet("C:\\Users\\erfan\\Desktop\\dummy\\res\\spritesheet.png");
        sheet = new SpriteSheet("C:\\res\\spritesheet.png");


        cam = new Camera();
        addKeyListener(new KeyInput());

        grass = new Sprite(sheet, 1, 1);
        redMushroom = new Sprite(sheet, 2, 1);
        greenMushroom = new Sprite(sheet, 3, 1);

        //   player=new Sprite(sheet,1,1);

        for (int i = 0; i < 8; i++) {
            player[0][1][i] = new Sprite(sheet, i + 1, 16);
            player[0][0][i] = new Sprite(sheet, i + 9, 16);
            player[0][2][i] = new Sprite(sheet, i + 1, 15);

            player[1][1][i] = new Sprite(sheet, i + 9, 14);
            player[1][0][i] = new Sprite(sheet, i + 3, 13);
            player[1][2][i] = new Sprite(sheet, i + 9, 14);

        }

        //sitting faced right 9. small mario cannot sit
        player[0][1][8] = new Sprite(sheet, 6, 14);//sit faced right
        player[0][2][8] = new Sprite(sheet, 15, 15);//sit faced left

        player[1][1][8] = new Sprite(sheet, 12, 12);
        player[1][2][8] = new Sprite(sheet, 12, 12);


        //sitting faced left 10. small mario cannot sit
        player[0][1][9] = new Sprite(sheet, 7, 14);
        player[0][2][9] = new Sprite(sheet, 16, 15);

        player[1][1][9] = new Sprite(sheet, 13, 12);
        player[1][2][9] = new Sprite(sheet, 13, 12);

        //Jumping to right 10
        player[0][0][10] = new Sprite(sheet, 9, 15);
        player[0][1][10] = new Sprite(sheet, 10, 15);
        player[0][2][10] = new Sprite(sheet, 11, 15);

        player[1][0][10] = new Sprite(sheet, 11, 13);
        player[1][1][10] = new Sprite(sheet, 15, 13);
        player[1][2][10] = new Sprite(sheet, 15, 13);

        //Jumping to left 11
        player[0][0][11] = new Sprite(sheet, 12, 15);
        player[0][1][11] = new Sprite(sheet, 13, 15);
        player[0][2][11] = new Sprite(sheet, 14, 15);

        player[1][0][11] = new Sprite(sheet, 12, 13);
        player[1][1][11] = new Sprite(sheet, 16, 13);
        player[1][2][11] = new Sprite(sheet, 16, 13);


        coin = new Sprite(sheet, 8, 14);

        goomba[0] = new Sprite(sheet, 1, 14);
        goomba[1] = new Sprite(sheet, 2, 14);
        goomba[2] = new Sprite(sheet, 3, 14);

        koopa[0] = new Sprite(sheet, 1, 12);
        koopa[1] = new Sprite(sheet, 2, 12);
        koopa[2] = new Sprite(sheet, 3, 12);
        koopa[3] = new Sprite(sheet, 4, 12);

        //spinning
        koopa[4] = new Sprite(sheet, 5, 12);


        powerUp = new Sprite(sheet, 4, 14);
        usedPowerUp = new Sprite(sheet, 5, 14);

        pipeBody = new Sprite(sheet, 2, 13);

        castleBrick = new Sprite(sheet, 4, 1);
        castleDoor = new Sprite(sheet, 5, 1);
        prince = new Sprite(sheet, 6, 1);

        plant[0] = new Sprite(sheet, 7, 12);
        plant[1] = new Sprite(sheet, 6, 12);

        // pipeHead=new Sprite(sheet,1,13);
        fireBall = new Sprite(sheet, 7, 1);
        fireFlower = new Sprite(sheet, 8, 1);


        star = new Sprite(sheet, 9, 1);

        stair = new Sprite(sheet, 10, 1);
        ordinaryBrick = new Sprite(sheet, 11, 1);
        destroyedOrdinaryBrick = new Sprite(sheet, 12, 1);

        specialBrick = new Sprite(sheet, 13, 1);
        destroyedSpecialBreak = new Sprite(sheet, 14, 1);

        dummy = new Sprite(sheet, 6, 6);


        for (int i = 0; i < 4; i++)
            hedgehog[i] = new Sprite(sheet, i + 8, 12);


        // handler.addEntity(new Player(300,512,64,64,true,Id.player1,handler));
        // handler.addTile(new Wall(200,200,64,64,true,Id.wall,handler));

        if (currentLevel < numberOfMaps)
            handler.createLevel(levelsImage.get(currentLevel));
        else
            JOptionPane.showMessageDialog(null, "All levels finished");

    }

    private synchronized void start() {
//        if(!running)
//            return;

        if (running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();


    }

    private synchronized void stop() {
        //     System.out.println("in stop startNext is "+startNext);

        if (!running)
            return;

        //  System.out.println("after return stop");

        running = false;

        try {
            thread.join();
            // System.out.println("afterJoined");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCoint() {
        coins++;
    }

    @Override
    public void run() {
        createLevels(numberOfMaps);
        while (!totallyFinished) {
            //  System.out.println("in mother loop with current map "+currentLevel);

//            if (currentLevel>=numberOfMaps &&  deathScreenTime == 179)
//            {
//                exit(0);
//             //   break;
//            }

            if (endGame >= 180)
                break;


            init();
            requestFocus();
            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            double delta = 0.0;
            double ns = 1000000000.0 / FPS;
            int frames = 0;
            int ticks = 0;
            running = true;
            showDeathScreen = true;

            while (running) {

                if (endGame != 0)
                    endGame++;

                if (endGame == 360)
                    exit(0);

                //   System.out.println(handler.getTile().size());

                //      System.out.println("im in game loop");

                //  System.out.println(currentLevel);

                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                Handler.clearAll();

                SenderClient senderClient = new SenderClient(handler, host, port);
                senderClient.start();
                //   System.out.println("sender start");

                while (delta > -1) {

                    tick();
                    ticks++;
                    delta--;


                }

                try {
                    senderClient.join();
                    //     System.out.println("Sender joined");
                    ReaderClient readerClient = new ReaderClient(port, host,handler);
                    readerClient.start();
                    //     System.out.println("reader start");
                    readerClient.join();

                    //     System.out.println("reader join");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                render();
                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    //    System.out.println(frames + "  Frames Per Second " + ticks + " Updates Per Second");
                    frames = 0;
                    ticks = 0;
                }
            }
        }
        stop();

    }

    /**
     * To draw and display
     * We have Three buffer strategy
     */
    public void render() {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }


//        Graphics g=bs.getDrawGraphics();
//        g.setColor(Color.BLACK);
//        g.fillRect(0,0,getWidth(),getHeight());
//
//        g.setColor(Color.RED);
//        g.fillRect(200,200,getWidth()-400,getHeight()-400);
//        g.dispose();
//        bs.show();

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (showScoreScreen) {
            g.setFont(new Font("Courier", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString("Scores in level" + (currentLevel), 700, 50);
            g.drawString("Current score : " + (savedCoins), 700, 100);
            g.drawString("Previous scores : ", 700, 150);

            g.setFont(new Font("Courier", Font.BOLD, 20));

            for (int i = allLines.size() - 1; i >= 0; i--)
                g.drawString("[" + i + "] : " + allLines.get(i), 700, 170 + i * 20);


            //move camera


        }

        //handle gameOver
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier", Font.BOLD, 50));
            g.drawString("Game over", 610, 400);

            long currentTime = System.currentTimeMillis();

            gameOverTicks++;


            if (gameOverTicks == 500) {

                Player.status = 0;
                currentLevel = 0;
                gameOver = false;
                lives = 3;


                //   exit(0);
            }


        }


        //handle Coinds
        if (!showDeathScreen && !gameOver && !showScoreScreen) {

            if (KeyInput.infiniteBalls) {
                fireBalls = 5;
                Player.status = 2;
            }


            g.drawImage(coin.getBufferedImage(), 20, 20, 30, 30, null);
            g.setColor(Color.BLUE.WHITE);
            g.setFont(new Font("Courier", Font.BOLD, 20));
            g.drawString("x" + coins, 46, 46);

            //  drawOtherPlayer(g);
     //       drawOtherPlayerFireBalls(g);

            for (int i = 0; i < lives; i++)
                g.drawImage(star.getBufferedImage(), 1150 + 60 * i, 40, 60, 60, null);

            if (Player.status == 2)
                for (int i = 0; i < fireBalls; i++)
                    g.drawImage(fireBall.getBufferedImage(), 1150 + 30 * i, 650, 30, 30, null);

            g.drawString("Level " + (currentLevel + 1), 600, 40);


        }

        if (!gameOver && showDeathScreen && !showScoreScreen) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier", Font.BOLD, 30));
            g.drawImage(Game.player[playerIndex][0][0].getBufferedImage(), 500, 300, 100, 100, null);

            g.drawString("x" + lives, 700, 400);

            //       System.out.println("current lvl is "+currentLevel);
            g.drawString("Level " + (currentLevel + 1), 600, 280);
        }

        if (paused && !showDeathScreen) {
            g.setColor(Color.white);
            g.setFont(new Font("Courier", Font.BOLD, 50));
            g.drawString("Paused", 550, 100);

        }


        //move camera
        g.translate(cam.getX(), cam.getY());

        if (!showDeathScreen)
            handler.render(g);

        g.dispose();
        bs.show();

    }

    private void drawOtherPlayer(Graphics g) {
        int otherPlayer;

        if (playerIndex == 0)
            otherPlayer = 1;

        else
            otherPlayer = 0;

        if (ReaderClient.otherPlayerStatus != -1)
            g.drawImage(player[otherPlayer][ReaderClient.otherPlayerStatus][ReaderClient.otherPlayerFrame].getBufferedImage(), ReaderClient.otherPlayerX, ReaderClient.otherPlayerY, 64, 64, null);


    }

//    private void drawOtherPlayerFireBalls(Graphics g) {
//        for (int i = 0; i < ReaderClient.fireBallX.size(); i++)
//            g.drawImage(fireBall.getBufferedImage(), ReaderClient.fireBallX.get(i), ReaderClient.fireBallY.get(i), 64, 64, null);
//
//    }

    /**
     * To update
     */

    public void tick() {
        handler.tick();


        for (Entity e : handler.getEntity()) {
            if (e.getId() == Id.player1) {
                if (!e.getGoingDownPipe())
                    cam.tick(e);
            }
        }

        //showDeathScreen=false;
        if (showDeathScreen)
            deathScreenTime++;

        //frames * 3 seconds
        if (deathScreenTime == 180) {
            showDeathScreen = false;
            paused = false;
            showScoreScreen = false;

            deathScreenTime = 0;
            handler.clearLevel();

            if (currentLevel != numberOfMaps)
                handler.createLevel(levelsImage.get(currentLevel));
        }


    }

    public Game() {

        Dimension size = new Dimension(WITDH * SCALE, HEIGHT * SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    public static Handler getHandler() {
        return handler;
    }


    public static void main(String[] args) {
        game = new Game();
        JPanel panel = new JPanel();

        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    if (paused)
                        paused = false;

                    else
                        paused = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        frame = new JFrame(TITLE);
        frame.add(panel);

        frame.add(game);
        frame.pack();
        frame.setSize(new Dimension(1400, 800));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.start();

        //System.out.println("After start");


//        while (true) {
//           JOptionPane.showMessageDialog(null,"in while true");
//
//            while(!startNext){
//             //   System.out.println(startNext);
//
//            }

//            System.out.println(startNext);
//
//
//
//            JOptionPane.showMessageDialog(null,"passed first");
//
//            if (Game.currentLevel == Game.numberOfMaps) {
//                JOptionPane.showMessageDialog(null, "Game has finished !");
//                break;
//            }
//            JOptionPane.showMessageDialog(null,"passed second");
//            frame.remove(game);
//             game = new Game();frame = new JFrame(TITLE);
//            frame.add(game);
//            frame.pack();
//            frame.setResizable(true);
//            frame.setLocationRelativeTo(null);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setVisible(true);
//            game.start();
//
//
//            startNext=false;
//        }

    }

    public static void setRunning() {
        running = true;
    }

    public static int getFrameWidth() {
        return WIDTH * SCALE;
    }

    public static Rectangle getVisibleArea() {
        for (int i = 0; i < handler.getEntity().size(); i++) {
            Entity e = handler.getEntity().get(i);

            if (e.getId() == Id.player1) {
                return new Rectangle(e.getX() - (getFrameWidth() / 2 - 5), e.getY() - (getFrameHeight()), getFrameWidth() + 10, getFrameHeight() + 10);
            }
        }

        return null;
    }

    public static int getFrameHeight() {
        return HEIGHT * SCALE;
    }

    public static void goNextLevel() {
        //  System.out.println("YIPIKAII-MOTHER-FUCKER!");
        //  System.out.println("in next level method");

        //  System.out.println("current level"+ currentLevel);
        //  System.out.println("number of maps "+numberOfMaps);

//        if (currentLevel == numberOfMaps ) {
////            JOptionPane.showMessageDialog(null,"Game has finished !");
//            //  running = false;
//            totallyFinished = true;
//            //Handle endgame
//            return;
//

        if (currentLevel == numberOfMaps - 1)
            endGame++;

        currentLevel++;
        running = false;

        // }

    }

    public static boolean isRunning() {
        return running;
    }

    public void createLevels(int numberOfMaps) {
        for (int i = 0; i < numberOfMaps; i++) {
            try {
                BufferedImage bf;
                //  bf = ImageIO.read(new File("C:\\Users\\erfan\\Desktop\\dummy\\res\\level" + (i + 1) + ".png"));
                //            System.out.println("C:\\Users\\erfan\\Desktop\\dummy\\res\\level" + (i + 1) + ".png");
                bf = ImageIO.read(new File("C:\\res\\level" + (i + 1) + ".png"));
                levelsImage.add(bf);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Unable to find map number" + i + 1);
            }
        }
    }


    //////////////////////////////////////////////////////////
    public static void saveScores(int level) {
        try {

            File t = new File("C:\\Game");
            //   System.out.println("dir "+t.isDirectory());

            if (!t.isDirectory())
                t.mkdir();

//            if (t.isFile() && !t.isDirectory()) {
//                deleteFile(t);
//
//                if (!t.isDirectory()) {
//                    t = new File("C:\\Game");
//
//                    System.out.println("mk "+t.mkdirs());
//                }
//            }

            loadScores(level);

            File f = new File("C:\\Game\\scores" + level + ".txt");


            if (new File(f.getAbsolutePath()).exists())
                deleteFile(new File(f.getAbsolutePath()));

            FileWriter fw = new FileWriter("C:\\Game\\scores" + level + ".txt");


            for (int i = 0; i < allLines.size(); i++)
                fw.write(allLines.get(i) + "\r\n");

            fw.write(coins + "\r\n");

            fw.close();
        } catch (Exception ex) {
            //  JOptionPane.showMessageDialog(null, "Unable to save score.", "Eror", 0);
            ex.printStackTrace();
        }


    }


    public static void loadScores(int level) {

        try {
            if (new File("C:\\Game\\scores" + level + ".txt").isFile())
                allLines = Files.readAllLines(Paths.get("C:\\Game\\scores" + level + ".txt"));
            else {
                File f = new File("C:\\Game\\scores" + level + ".txt");
                f.createNewFile();
                allLines = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(File f) {


        if (f.isFile()) {
            try {
                f.delete();


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error happened in deleting process", "Error", 1);
            }
        }

        if (f.isDirectory()) {
            File[] temp = f.listFiles();

            for (int i = 0; i < temp.length; i++)
                deleteFile(temp[i]);

            try {
                if (!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            } catch (Exception ex) {

            }
        }

    }

//    private void updateLiveKoopas() {
//        for (int i = 0; i < ReaderClient.changedKoopas.size(); i++) {
//            for (int j = 0; j < handler.getEntity().size(); j++)
//                if (handler.getEntity().get(j).getId() == Id.koopa && handler.getEntity().get(j).getTag() == ReaderClient.changedKoopas.get(i).getTag()) {
//                    if (handler.getEntity().get(j).getKoopaState() == KoopaState.WALKING) {
//                        handler.getEntity().get(j).setKoopaState(KoopaState.SHELL);
//                        handler.getEntity().get(j).setVelX(0);
//                        break;
//                    }
//
//                    if (handler.getEntity().get(j).getKoopaState() == KoopaState.SHELL) {
//                        handler.getEntity().get(j).setKoopaState(KoopaState.SPINNING);
//                        handler.getEntity().get(j).setVelX(ReaderClient.changedKoopas.get(i).getVelX());
//                        break;
//                    }
//                }
//
//        }
//    }
//
//    private void updateDeadObjects() {
//        for (int i = 0; i < ReaderClient.deadObjects.size(); i++) {
//            DeadObject deadObject = ReaderClient.deadObjects.get(i);
//            System.out.println("DEAD OBJECT ID : "+deadObject.getId());
//
//            switch (deadObject.getId()) {
//                case goomba:
//                    for (int j = 0; j < handler.getEntity().size(); j++)
//                        if (handler.getEntity().get(j).getId() == Id.goomba && handler.getEntity().get(j).getTag() == deadObject.getTag()) {
//                            handler.getEntity().remove(j);
//                            break;
//                        }
//                    break;
//
//                case hedgehog:
//                    for (int j = 0; j < handler.getEntity().size(); j++)
//                        if (handler.getEntity().get(j).getId() == Id.hedgehog && handler.getEntity().get(j).getTag() == deadObject.getTag()) {
//                            handler.getEntity().remove(j);
//                            break;
//                        }
//                    break;
//
//
//                case plant:
//                    for (int j = 0; j < handler.getEntity().size(); j++)
//                        if (handler.getEntity().get(j).getId() == Id.plant && handler.getEntity().get(j).getTag() == deadObject.getTag()) {
//                            handler.getEntity().remove(j);
//                            break;
//                        }
//                    break;
//
//                case koopa:
//                    for (int j = 0; j < handler.getEntity().size(); j++)
//                        if (handler.getEntity().get(j).getId() == Id.koopa && handler.getEntity().get(j).getTag() == deadObject.getTag()) {
//                            handler.getEntity().remove(j);
//                            break;
//                        }
//                    break;
//
//
//                case brick:
//                    for (int j = 0; j < handler.getTile().size(); j++)
//                        if (handler.getTile().get(j).getId() == Id.brick && handler.getTile().get(j).getX()==deadObject.getX() &&  handler.getTile().get(j).getY()==deadObject.getY()) {
//                            handler.getTile().remove(j);
//                            break;
//                        }
//                    break;
//
//
//                case coin:
//                    System.out.println("REMOVING COIN");
//                    for (int j = 0; j < handler.getTile().size(); j++)
//                        if (handler.getTile().get(j).getId() == Id.coin && handler.getTile().get(j).getX()==deadObject.getX() &&  handler.getTile().get(j).getY()==deadObject.getY()) {
//                            handler.getTile().remove(j);
//                            break;
//                        }
//                    break;
//
//
//                case fireFlower:
//                    for (int j = 0; j < handler.getTile().size(); j++)
//                        if (handler.getTile().get(j).getId() == Id.fireFlower && handler.getTile().get(j).getX()==deadObject.getX() &&  handler.getTile().get(j).getY()==deadObject.getY()) {
//                            handler.getTile().remove(j);
//                            break;
//                        }
//                    break;
//
//                case powerUp:
//                    for (int j = 0; j < handler.getTile().size(); j++)
//                        if (handler.getTile().get(j).getId() == Id.powerUp && handler.getTile().get(j).getX()==deadObject.getX() &&  handler.getTile().get(j).getY()==deadObject.getY()) {
//                            ((PowerUpBlock)handler.getTile().get(j)).setHitsTaken(deadObject.getHits());
//                            break;
//                        }
//                    break;
//
//
//
//            }
//        }
//    }
//
//    private void updateMushrooms()
//    {
//        for (int i=0;i<ReaderClient.mushroomX.size();i++)
//            handler.getEntity().add(new RedMushroom(ReaderClient.mushroomX.get(i),ReaderClient.mushroomY.get(i),64,64,Id.redMushroom,handler,Handler.mushroomTags++));
//    }
//
//    private void applyRemoteUpdate() {
//        updateLiveKoopas();
//        updateDeadObjects();
//        updateMushrooms();
//
//
//    }

    public static void showScoreScreen() {
        paused = true;


        saveScores(currentLevel);

//        long time=System.currentTimeMillis();
//
////
////        frame.remove(game);
////
////        while(System.currentTimeMillis()-time<2000)
////        {
////
////        }

        showScoreScreen = true;
        paused = true;
        savedCoins = coins;
        coins = 0;
//
//        frame.add(game);
//        frame.repaint();
//        paused=true;


    }
/////////////////
}
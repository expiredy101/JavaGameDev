package com.game.game_package.scene_components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.game.game_package.BallsGameClass;

public class Player extends Target{
    //Some objects init

    private final OrthographicCamera camera;

    // some phisiscs constans
    private static final float GRAVITY = -5.0f;
    private static final float SPEED = 100f;
    private static final float SLIDESPEED = 0.5f;
    private static final float BounceMultiplayer = 0.3f;
    private static final Integer DefaultMovingConstant = 0;

    //private static final float LERP = 0.1f;
    private static final float boundsMultiplayer = 0.3f;


    //Some movement values
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 centerPosition;

    private boolean isGrounded;

    // everything about drawing stuff
    private final Integer xSize = 100;
    private final Integer ySize = 100;
    private Texture playerSprite;
    private Circle bounds;


    public Player(OrthographicCamera camera, float spawningXCord, float spawningYCord){
        this.camera = camera;
        //camera.makeFolow(this);
        velocity =  new Vector2(DefaultMovingConstant, DefaultMovingConstant);
        position = new Vector2(spawningXCord, spawningYCord);
        isGrounded = false;
        centerPosition = new Vector2(position.x + xSize / 2, position.y + ySize / 2);
        startDrawing();
    }

    public void update(float deltaTime){
        //updateCameraPosition(deltaTime);
        bounds.setPosition(position.x, position.y);
        centerPosition = new Vector2(position.x + (float)xSize / 2, position.y + (float)ySize / 2);
        freeFall();
        updateCurrentPosition(deltaTime);
    }

    private void startDrawing(){
        playerSprite = BallsGameClass.getPlayerSkin();
        bounds = new Circle(position.x, position.y, playerSprite.getHeight());
    }

    public void drawPlayer(Batch batch){

        batch.draw(this.playerSprite, position.x, position.y, xSize, ySize);
    }

    public void AddForce(float xCordToGo, float yCordToGo, double force){
        

        Vector2 MovementVector = new Vector2(SPEED, SPEED);


//        System.out.println(centerPosition.x + " " + xCordToGo);
//        System.out.println(centerPosition.y + " " + yCordToGo);

        float deltaX = xCordToGo - centerPosition.x;
        float deltaY = yCordToGo - centerPosition.y;


        float cosOfVectors = (float) ((centerPosition.x * deltaX + centerPosition.y * deltaY) /
                        (Math.sqrt(centerPosition.x * centerPosition.x + centerPosition.y * centerPosition.y)
                                * Math.sqrt(deltaX * deltaX + deltaY * deltaY)));
        System.out.println(cosOfVectors);

//        cos(a) = 1 - sin(a)^2;
        float sa = cosOfVectors * cosOfVectors - 1;
//        float radians = (float) (cosOfVectors / 180.0d * Math.PI);


//        float radius = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);


        //float angleInDegrees = 90;

//        float x = (float)(lengthOfWay * Math.cos(angleInDegrees * Math.PI / 180F)) + deltaX;
//        float y = (float)(lengthOfWay * Math.sin(angleInDegrees * Math.PI / 180F)) + deltaY;
//        float angleInDegrees = (float) Math.acos((deltaX - deltaX) / lengthOfWay);
//
        //double radians = Math.acos((deltaX * MovementVector.x + deltaY * MovementVector.y) / (lengthOfWay * lengthOfWay));
//        degreeToGo = degreeToGo / 180 * Math.PI;
//        System.out.println(degreeToGo);
////
//        double radians = Math.PI * degreeToGo;

//        float sa = (float) Math.sin(radians);

        MovementVector = new Vector2(cosOfVectors * MovementVector.x - sa * MovementVector.y,
                sa * MovementVector.x + cosOfVectors * MovementVector.y);
//        System.out.println("Vector movement" + MovementVector.x + " " + MovementVector.y);
        velocity.add(MovementVector);

//        float xDirection = SPEED * (deltaX  / lengthOfWay);
//        float yDirection = SPEED * (deltaY / lengthOfWay);
////        System.out.println(xDirection + " " + yDirection);
//        System.out.println("Current X: " + (int) centerPosition.x + " We going to " + (int)xDirection);
//        System.out.println("Current Y: " + (int) centerPosition.y + " We going to " + (int)yDirection);
//        velocity = new Vector2(xDirection, yDirection);
       // System.out.println(velocity);

//        float xCordCoef = xCordToGo / yCordToGo;
//        float yCordCoef = 1 - Math.abs(xCordCoef);
//
//        velocity.add(xCordCoef, yCordCoef);
//        System.out.println(xCordCoef + " " + yCordCoef);

        //velocity.x *= force;
        //velocity.y *= force;
    }

    public void bounce(){
        if (position.y <= 0 | position.y + this.ySize >= BallsGameClass.HEIGHT){
            velocity.y = -velocity.y * boundsMultiplayer;}
        if (position.x <= 0 | position.x + this.xSize >= BallsGameClass.WIDTH)
            velocity.x = -velocity.x * boundsMultiplayer;

    }


    private void updateCurrentPosition(float deltaTime){
        velocity.scl(deltaTime);


        position.add(velocity.x, velocity.y);

        velocity.scl(1/deltaTime);

        position.add(velocity.x, velocity.y);
        if(position.y <= 0){
            position.y = 0;
            if (velocity.x - SLIDESPEED * 2 <= 0 | velocity.x + SLIDESPEED * 2 >= 0){
                velocity.x += -velocity.x * SLIDESPEED;}
            else{
                velocity.x = DefaultMovingConstant;
            }
            bounce();
        }
        bounds.setPosition(position.x, position.y);
        checkCollisionsWithGround();
    }

    public void checkCollisionsWithGround() {
        if (position.x < 0){
            position.x = 0;
            bounce();
        }
        if (position.x > BallsGameClass.WIDTH - this.ySize){
            position.x = BallsGameClass.WIDTH - this.ySize;
            bounce();
        }

        if (position.y > BallsGameClass.HEIGHT - this.ySize){
            bounce();
            position.y = BallsGameClass.HEIGHT - this.ySize;
        }
    }

    private void freeFall(){
        if (this.isInAir()){
            velocity.add(DefaultMovingConstant, GRAVITY);
            }
        else if(!isGrounded){
            isGrounded = true;
            bounce();
//            position.y = 0;
        }
    }
    public boolean isInAir(){
        return ( position.y < BallsGameClass.HEIGHT);


    }

    public void updateCameraPosition(float deltaTime){
        camera.position.set(new Vector3(position.x, position.y, DefaultMovingConstant));
        camera.update();
    }
}

package pipes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import helpers.Gameinfo;

public class Pipes {

    private World world;
    private Body body1, body2;

    private Sprite pipe1, pipe2;

    private final float DISTANCE_BETWEEN_PIPES = 420f;

    private Random random = new Random();

    private OrthographicCamera mainCamera;

    public Pipes(World world, float x){
        this.world = world;
        createPipes(x, getRandomY());
    }

    private void createPipes(float x, float y){
        pipe1 = new Sprite((new Texture("Pipes/Pipe 1.png")));
        pipe2 = new Sprite((new Texture("Pipes/Pipe 2.png")));

        pipe1.setPosition(x, y + DISTANCE_BETWEEN_PIPES);
        pipe2.setPosition(x, y - DISTANCE_BETWEEN_PIPES);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody; // Kinematic bodes are not effected by gravity but they can be moved by forces, like linear impulse

        // Creating body for pipe1
        bodyDef.position.set(pipe1.getX() / Gameinfo.PPM, pipe1.getY() / Gameinfo.PPM);
        body1 = world.createBody(bodyDef);

        // Creating body for pipe2
        // We can use the same bodyDef because we are setting the position again
        bodyDef.position.set(pipe2.getX() / Gameinfo.PPM, pipe2.getY() / Gameinfo.PPM);
        body2 = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((pipe1.getWidth() / 2f) / Gameinfo.PPM, (pipe1.getHeight() / 2f) / Gameinfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        Fixture fixture1 = body1.createFixture(fixtureDef);
        Fixture fixture2 = body2.createFixture(fixtureDef);

        shape.dispose();

    }

    public void drawPipes(SpriteBatch batch){
        batch.draw(pipe1, pipe1.getX() - pipe1.getWidth() / 2f - 1, pipe1.getY() - pipe1.getHeight() / 2f - 2);
        batch.draw(pipe2, pipe2.getX() - pipe2.getWidth() / 2f - 1, pipe2.getY() - pipe2.getHeight() / 2f);
    }

    public void updatePipes(){
        pipe1.setPosition(body1.getPosition().x * Gameinfo.PPM, body1.getPosition().y * Gameinfo.PPM);
        pipe2.setPosition(body2.getPosition().x * Gameinfo.PPM, body2.getPosition().y * Gameinfo.PPM);
    }

    public void movePipes(){
        body1.setLinearVelocity(-1, 0);
        body2.setLinearVelocity(-1, 0);

        if(pipe1.getX() + (Gameinfo.WIDTH / 2f) + 60 <  mainCamera.position.x){
            body1.setActive(false);
            body2.setActive(false);
        }
    }

    public void setMainCamera(OrthographicCamera mainCamera){
        this.mainCamera = mainCamera;
    }

    private float getRandomY(){
        float max = Gameinfo.HEIGHT / 2f + 150f;
        float min = Gameinfo.HEIGHT / 2f - 150f;

        return random.nextFloat() * (max - min) + min;
    }

} // pipes
















































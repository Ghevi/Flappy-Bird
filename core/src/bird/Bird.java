package bird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import helpers.Gameinfo;

public class Bird extends Sprite {

    private World world;
    private Body body;

    public Bird(World world, float x, float y){
        super(new Texture("Birds/Blue/Idle.png"));
        this.world = world;
        setPosition(x, y);
        createBody();

    }

    private void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(getX() / Gameinfo.PPM, getY() / Gameinfo.PPM);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius((getHeight() / 2f) / Gameinfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void drawBirdIdle(SpriteBatch batch){
        batch.draw(this, getX(), getY());
    }

    public void updateBird(){
        setPosition(body.getPosition().x * Gameinfo.PPM, body.getPosition().y * Gameinfo.PPM);
    }

} // bird


























package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ghevi.flappybird.GameMain;

import bird.Bird;
import ground.GroundBody;
import helpers.Gameinfo;

public class Gameplay implements Screen {

    private GameMain game;
    private World world;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera debugCamera;
    private Box2DDebugRenderer debugRenderer;

    private Array<Sprite> bgs = new Array<Sprite>();
    private Array<Sprite> grounds = new Array<Sprite>();

    private Bird bird;

    private GroundBody groundBody;

    public Gameplay(GameMain game){
        this.game = game;

        mainCamera = new OrthographicCamera(Gameinfo.WIDTH, Gameinfo.HEIGHT);
        mainCamera.position.set(Gameinfo.WIDTH / 2f, Gameinfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(Gameinfo.WIDTH, Gameinfo.HEIGHT, mainCamera);

        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Gameinfo.WIDTH / Gameinfo.PPM, Gameinfo.HEIGHT / Gameinfo.PPM);
        debugCamera.position.set(Gameinfo.WIDTH / 2f, Gameinfo.HEIGHT / 2f,0);

        debugRenderer = new Box2DDebugRenderer();

        createBackgrounds();
        createGrounds();

        world = new World(new Vector2(0, -9.8f), true);

        bird = new Bird(world, Gameinfo.WIDTH / 2f - 80, Gameinfo.HEIGHT / 2f);

        groundBody = new GroundBody(world, grounds.get(0));
    }

    private void update(float dt){
        moveBackgrounds();
        moveGrounds();
        birdFlap();
    }

    private void birdFlap(){
        if(Gdx.input.justTouched()){
            bird.birdFlap();
        }
    }

    private void createBackgrounds(){
        for(int i = 0; i < 3; i++){
            Sprite bg = new Sprite(new Texture("Backgrounds/Day.jpg"));
            bg.setPosition(i * bg.getWidth(), 0); // Repeat the background towards right side ->
            bgs.add(bg);
        }
    }

    private void createGrounds(){
        for(int i = 0; i < 3; i++){
            Sprite ground = new Sprite(new Texture("Backgrounds/Ground.png"));
            ground.setPosition(i * ground.getWidth(), -ground.getHeight() / 2f - 55); // Repeat the ground towards right side ->
            grounds.add(ground);
            System.out.println(ground.getHeight());
        }
    }

    private void moveBackgrounds(){
        for(Sprite bg : bgs){
            float x1 = bg.getX() - 2f;
            bg.setPosition(x1, bg.getY());

            if(bg.getX() + Gameinfo.WIDTH + (bg.getWidth() / 2f) < mainCamera.position.x){
                float x2 = bg.getX() + bg.getWidth() * bgs.size;
                bg.setPosition(x2, bg.getY());
            }
        }
    }

    private void moveGrounds(){
        for(Sprite ground : grounds){
            float x1 = ground.getX() - 1f;
            ground.setPosition(x1, ground.getY());

            if(ground.getX() + Gameinfo.WIDTH + (ground.getWidth() / 2f) < mainCamera.position.x){
                float x2 = ground.getX() + ground.getWidth() * grounds.size;
                ground.setPosition(x2, ground.getY());
            }
        }
    }

    private void drawBackgrounds(SpriteBatch batch){
        for(Sprite bg : bgs){
            batch.draw(bg, bg.getX(), bg.getY());
        }
    }

    private void drawGrounds(SpriteBatch batch){
        for(Sprite ground : grounds){
            batch.draw(ground, ground.getX(), ground.getY());
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();

        drawBackgrounds(game.getBatch());
        drawGrounds(game.getBatch());
        bird.drawBirdIdle(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, debugCamera.combined);

        bird.updateBird();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
} // gameplay

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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ghevi.flappybird.GameMain;

import bird.Bird;
import ground.GroundBody;
import helpers.Gameinfo;
import hud.UIHud;
import pipes.Pipes;

public class Gameplay implements Screen , ContactListener {

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

    private UIHud hud;

    private boolean firstTouch;

    private Array<Pipes> pipesArray = new Array<Pipes>();
    private final int DISTANCE_BETWEEN_PIPES = 120;

    // Only for testing, just one pipe
    // private Pipes pipes;

    public Gameplay(GameMain game){
        this.game = game;

        mainCamera = new OrthographicCamera(Gameinfo.WIDTH, Gameinfo.HEIGHT);
        mainCamera.position.set(Gameinfo.WIDTH / 2f, Gameinfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(Gameinfo.WIDTH, Gameinfo.HEIGHT, mainCamera);

        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Gameinfo.WIDTH / Gameinfo.PPM, Gameinfo.HEIGHT / Gameinfo.PPM);
        debugCamera.position.set(Gameinfo.WIDTH / 2f, Gameinfo.HEIGHT / 2f,0);

        debugRenderer = new Box2DDebugRenderer();

        hud = new UIHud(game);

        createBackgrounds();
        createGrounds();

        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(this);

        bird = new Bird(world, Gameinfo.WIDTH / 2f - 80, Gameinfo.HEIGHT / 2f);

        groundBody = new GroundBody(world, grounds.get(0));

        // Only for testing, create one pipe
        // pipes = new Pipes(world, Gameinfo.WIDTH + 120);
        // pipes.setMainCamera(mainCamera);
    }

    private void update(float dt){
        checkForFirstTouch();

        if(bird.isAlive()){
            moveBackgrounds();
            moveGrounds();
            birdFlap();
            updatePipes();
            movePipes();
        }
    }

    private void checkForFirstTouch(){
        if(!firstTouch){
            if(Gdx.input.justTouched()){
                firstTouch = true;
                bird.activateBird();
                createAllPipes();
            }
        }
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

    private void createPipes(){
        Pipes p = new Pipes(world, Gameinfo.WIDTH + DISTANCE_BETWEEN_PIPES);
        p.setMainCamera(mainCamera);
        pipesArray.add(p);
    }

    private void createAllPipes(){
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                createPipes();
            }
        });

        SequenceAction sa = new SequenceAction();
        sa.addAction(Actions.delay(2f));
        sa.addAction(run);

        hud.getStage().addAction(Actions.forever(sa));
    }

    private void drawPipes(SpriteBatch batch){
        for(Pipes pipe : pipesArray){
            pipe.drawPipes(batch);
        }
    }

    private void updatePipes(){
        for(Pipes pipe : pipesArray){
            pipe.updatePipes();
        }
    }

    private void movePipes(){
        for (Pipes pipe : pipesArray) {
            pipe.movePipes();
        }
    }

    private void stopPipes(){
        for(Pipes pipe : pipesArray) {
            pipe.stopPipes();
        }
    }

    private void birdDied(){
        bird.setAlive(false);
        stopPipes();

        // remove the actions in this stage so they dont run forever because of [hud.getStage().addAction(Actions.forever(sa));]
        hud.getStage().clear();
        hud.showScore();

        hud.createButtons();
        Gdx.input.setInputProcessor(hud.getStage());
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

        // draw all pipes
        drawPipes(game.getBatch());

        // remove this later, draw one pipe for testing
        // pipes.drawPipes(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, debugCamera.combined);

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();

        bird.updateBird();

        // remove this later, move one pipe for testing
        // pipes.movePipes();

        // remove this later, update one pipe for testing
        // pipes.updatePipes();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    // Screen interface

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

    // ContactListener interface

    @Override
    public void beginContact(Contact contact) {
        Fixture body1, body2;

        if(contact.getFixtureA().getUserData() == "Bird"){
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if(body1.getUserData() == "Bird" && body2.getUserData() == "Pipe"){
            if(bird.isAlive()){
                System.out.println("dead");
                // Kill the bird
                birdDied();
            }
        }

        if(body1.getUserData() == "Bird" && body2.getUserData() == "Ground"){
            if(bird.isAlive()){
                System.out.println("dead");
                // Kill the bird
                birdDied();
            }
        }

        if(body1.getUserData() == "Bird" && body2.getUserData() == "Score"){
            hud.incrementScore();
            System.out.println("SCORE");
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
} // gameplay

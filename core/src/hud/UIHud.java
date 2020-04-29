package hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ghevi.flappybird.GameMain;

import helpers.Gameinfo;

public class UIHud {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    public UIHud(GameMain game){
        this.game = game;

        gameViewport = new FitViewport(Gameinfo.WIDTH, Gameinfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());
    }



    public Stage getStage(){
        return this.stage;
    }

} // ui hud










































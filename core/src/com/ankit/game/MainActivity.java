package com.ankit.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;


public class MainActivity extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,bird[];
	Texture gameOver;
	int flapState=0;
	float birdY;
	float velocity=0;
	int gameState=0;
	float gravity=2;
	int score=0;
	int scoringTube=0;

	Circle birdCircle;

	Texture topTube;
	Texture bottomTube;
	float gap=400;
	float maxTubeOffset;

	Random random;
	float tubeVelocity=4;
	int numberOfTubes=4;
	float [] tubeX= new float[numberOfTubes];
	float tubeOffset[]=new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle topTubeRectangles[];
	Rectangle bottomTubeRectangles[];
	BitmapFont bitmapFont;
	
	@Override
	public void create () {
		batch= new SpriteBatch();
		background= new Texture("background.png");
		bird= new Texture[2];
		bird[0]= new Texture("bird.png");
		bird[1]= new Texture("bird2.png");
		topTube= new Texture("toptube.png");
		bottomTube= new Texture("bottomtube.png");
		maxTubeOffset=Gdx.graphics.getHeight()/2 -gap/2-100;
		random= new Random();
		distanceBetweenTubes=Gdx.graphics.getWidth()*3/4;
		birdCircle= new Circle();
		topTubeRectangles= new Rectangle[numberOfTubes];
		bottomTubeRectangles= new Rectangle[numberOfTubes];
		bitmapFont=new BitmapFont();
		bitmapFont.setColor(Color.WHITE);
		bitmapFont.getData().setScale(10);
		gameOver= new Texture("gameover.png");
		startGame();

	}
	public void startGame(){
		birdY=Gdx.graphics.getHeight()/2-bird[flapState].getHeight()/2;
		for(int i = 0;i < numberOfTubes ; i++){
			tubeOffset[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight() - gap -200);
			tubeX[i]= Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth()+i*distanceBetweenTubes;

			topTubeRectangles[i]=new Rectangle();
			bottomTubeRectangles[i]=new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState==1) {

			if(tubeX[scoringTube]<(Gdx.graphics.getWidth()/2)){
				score++;
				Gdx.app.log("The score is ",Integer.toString(score));
				if(scoringTube<3)
					scoringTube++;
				else
					scoringTube=0;
			}

			if(Gdx.input.justTouched()){
				//Gdx.app.log("Touch Detected ?","Yes");
				//gameState=1;
				velocity=-30;

			}
			for(int i=0;i<numberOfTubes;i++) {
				if(tubeX[i]<-topTube.getWidth()){

					tubeX[i]+=numberOfTubes*distanceBetweenTubes;
					tubeOffset[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight() - gap -200);

				}
				else {
					 tubeX[i]=tubeX[i] - tubeVelocity;

				}
				tubeX[i] = tubeX[i] - tubeVelocity;
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i]= new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomTubeRectangles[i]= new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
			}
			if(birdY>0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}
			else{
				gameState=2;
			}

		}
		else if(gameState==0) {
			if(Gdx.input.justTouched()){
				//Gdx.app.log("Touch Detected ?","Yes");
				gameState=1;
			}
		}
		else if(gameState==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2 );
			if(Gdx.input.justTouched()){
				//Gdx.app.log("Touch Detected ?","Yes");
				gameState=1;
				startGame();
				score=0;
				scoringTube=0;
				velocity=0;
			}
		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}



		batch.draw(bird[flapState], Gdx.graphics.getWidth() / 2 - bird[flapState].getWidth() / 2, birdY);
		bitmapFont.draw(batch,String.valueOf(score),100,200);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY + bird[flapState].getHeight()/2,bird[flapState].getWidth()/2);


		for(int i=0;i<numberOfTubes;i++){

			if(Intersector.overlaps(birdCircle,topTubeRectangles[i])|| Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){
				Gdx.app.log("Collision Detected ?","Yes");
				gameState=2;
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}

package WizardTD;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import processing.core.PImage;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestApp extends App{
    
    @Override
    public PImage loadImage(String path){
        return null;
    }

    @Override
    public void image(PImage img, float x, float y){
        // do nothing
    }

    @Override
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        return null;
    }

    @Override
    public void noStroke() {
        // do nothing
    }

    @Override
    public void fill(float r, float g, float b) {
        // do nothing
    }

    @Override
    public void rect(float x, float y, float w, float h) {
        // do nothing
    }

    @Override
    public void ellipse(float x, float y, float w, float h) {
        // do nothing
    }

    @Override
    public void stroke(float r, float g, float b) {
        // do nothing
    }

    @Override
    public void strokeWeight(float w) {
        // do nothing
    }

    @Override
    public void line(float x1, float y1, float x2, float y2) {
        // do nothing
    }

    @Override
    public void textSize(float s) {
        // do nothing
    }

    @Override
    public void text(String s, float x, float y) {
        // do nothing
    }

    @Override
    public void noFill() {
        // do nothing
    }
}

public class Testing extends App {
    TestApp testApp;

    /**
     * ASSUME CONFIGTEST FILE ISNT CHANGED FROM WHAT I SUBMITTED
     */
    @BeforeEach
    public void setUpAppMap() {
        testApp = new TestApp();
        testApp.config = readJSON("configTest.json");
        testApp.createStuff();

        testApp.mouseX = 50;
        testApp.mouseY = 50 + App.TOPBAR;
        testApp.ui.placeTower = true;
        testApp.ui.upgradeRange = true;
        testApp.ui.upgradeDamage = true;
        testApp.ui.upgradeSpeed = true;
        testApp.ui.click(testApp);

        assertNotNull(testApp.map);
    }

    @Test
    public void tickCheck(){
        testApp.tick();
    }
    
    @Test
    public void createWave(){
        testApp.map.nextWave();
    }

    @Test
    public void switchMana(){
        testApp.ui.toggleSwitch(testApp, 7);
        testApp.ui.toggleSwitch(testApp, 8);
    }

    @Test
    public void drawMap(){
        testApp.draw();
    }

    @Test
    public void spawnRandomMonster(){
        Wave wave = new Wave(testApp.map.getData().getJSONArray("waves").getJSONObject(0), testApp.map.getRoutes(), testApp);
        wave.createRandomMonster();
    }

    @Test
    public void spawnMonsterAndShoot(){
        ArrayList<Direction> route = new ArrayList<Direction>();
        route.add(Direction.DOWN);

        testApp.map.getWaves().add(new Wave(testApp.map.getData().getJSONArray("waves").getJSONObject(0), testApp.map.getRoutes(), testApp));
        testApp.map.getWaves().get(0).getMonsters().add(new Monster(
            0, 1, 5, 10, 2, route, testApp, 5
        ));
        testApp.map.getWaves().get(0).tick();
        testApp.map.getTowerList().get(0).setFramesCounter(9999);
        testApp.map.tick();
    }

    @Test
    public void placeTower(){
        
    }
    
}
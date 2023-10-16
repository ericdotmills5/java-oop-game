package WizardTD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import processing.data.JSONObject;
import java.util.Random;

public class Wave {
    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    private HashMap<Path, ArrayList<Direction>> rotes = new HashMap<Path, ArrayList<Direction>>();
    private JSONObject waveData;
    private App app;
    private int framesPerSpawn;
    private int waveFrames;
    private int currentFrame;
    private ArrayList<Integer> monsterTypeCounts = new ArrayList<>();
    private int monstersRemaining = 0;
    private Path[] spawnPaths;
    private boolean waveComplete = false;
    
    public static final int FPS = App.FPS;

    public Wave(JSONObject waveData, HashMap<Path, ArrayList<Direction>> rotes, App app) {
        this.rotes = rotes;
        this.waveData = waveData;
        this.app = app;
        this.waveFrames = waveData.getInt("duration") * FPS;
        this.spawnPaths = rotes.keySet().toArray(new Path[rotes.size()]);

        for(int i = 0; i < waveData.getJSONArray("monsters").size(); i++) {
            this.monsterTypeCounts.add(
                waveData.getJSONArray("monsters").getJSONObject(i).getInt("quantity")
            );
            this.monstersRemaining += this.monsterTypeCounts.get(i);
        } // fill with number of each type of monster remaining, and total monsters remaining      

        if (this.monstersRemaining == 0) {
            this.framesPerSpawn = 0; // prevent division by 0 error
        } else{
        this.framesPerSpawn = this.waveFrames / this.monstersRemaining;
        }

        System.out.println("Wave created");
    }

    public JSONObject getData() {
        return this.waveData;
    }

    public boolean getWaveComplete() {
        return this.waveComplete;
    }

    public ArrayList<Monster> getMonsters() {
        return this.monsters;
    }

    public void iterateThroughMonsters() {
        Iterator<Monster> monsterIterator = this.monsters.iterator(); 
        // used since updating elements as we iterate
        while(monsterIterator.hasNext()) { // tick all monsters in array
            Monster monster = monsterIterator.next();
            monster.tick();

            if (!(monster.getExists())) {
                monsterIterator.remove();
            } // remove monsters that finished death animation
        }
    }

    public void createRandomMonster() {
        Random rand = new Random();
        int randMonsterType = rand.nextInt(this.monsterTypeCounts.size()); // choose random monster
        JSONObject type = this.waveData.getJSONArray("monsters").getJSONObject(randMonsterType);

        int randSpawnPath = rand.nextInt(this.spawnPaths.length); // choose random spawn path
        Path spawnPath = this.spawnPaths[randSpawnPath];

        this.monsters.add(new Monster(
            spawnPath.getX(), spawnPath.getY(), type.getDouble("speed"), 
            type.getDouble("hp"), type.getDouble("armour"), 
            this.rotes.get(spawnPath), this.app, type.getDouble("mana_gained_on_kill")
        )); // add new monster type with spawn to array
        
        monsterTypeCounts.set(randMonsterType, monsterTypeCounts.get(randMonsterType) - 1); 
        this.monstersRemaining -= 1; // remove monster from count

        for(int i = 0; i < monsterTypeCounts.size(); i++) {
            if (monsterTypeCounts.get(i) == 0) {
                monsterTypeCounts.remove(i); 
            } // remove monster type from count if none left
        }
    }

    public void tick() {
        this.currentFrame += app.rate;

        this.iterateThroughMonsters();

        // generate new random monster type with random spawn path
        if (this.monstersRemaining > 0 && this.currentFrame >= this.framesPerSpawn) {
            this.createRandomMonster();
            this.currentFrame = 0; // reset frame counter
        } 
        
        if (this.monstersRemaining == 0 && this.monsters.size() == 0) {
            this.waveComplete = true;
        } // if all monsters have been spawned and killed, wave will be removed from map array
    }

    public void draw() {
        for(Monster monster : this.monsters) {
            monster.draw(this.app); // draw all monsters in array
        }
    }
}

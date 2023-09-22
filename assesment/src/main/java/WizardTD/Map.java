package WizardTD;

import java.util.Scanner;

import processing.core.PApplet;

import java.io.File;
import java.io.FileNotFoundException;

public class Map {
    static final int sqrMapSize = 20;

    private Tile[][] land = new Tile[sqrMapSize][sqrMapSize];
    private int[] wizCordsXY = new int[2];
    private App app;

    public Map(String fileLoc, App app){
        this.app = app;
        this.land = this.scan2Matrix(fileIO(fileLoc));
        System.out.println("matrix made");

        this.updateAllPaths();

        System.out.println("Paths rotated");
        
        // assume this is only fileIO method call
    }

    public Tile[][] getLand(){
        return this.land;
    }

    public App getApp(){
        return this.app;
    }

    static Scanner fileIO(String loc){ // read file into scanner obj
        File f = new File(loc);
        Scanner scan;
        try{
            scan = new Scanner(f);
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            return null; // this might be dangerous
        }
        return scan;
    }

    public Tile[][] scan2Matrix(Scanner scan){
        Tile[][] matrix = new Tile[sqrMapSize][sqrMapSize]; // assume level is sqrmapsize
        int i;
        
        for(int j = 0; j < sqrMapSize; j++){ // iterate through each line
            i = 0;
            for(char c: scan.nextLine().toCharArray()){ // iterate through each letter
                switch(c){
                    case 'S':
                        matrix[i][j] = new Shrub(i, j, this);
                        break;
                    case 'X':
                        matrix[i][j] = new Path(i, j, this);
                        break;
                    case 'W':
                        matrix[i][j] = new Wizard(i, j, this);
                        this.wizCordsXY[0] = i;
                        this.wizCordsXY[1] = j;
                        break;
                    case ' ':
                        matrix[i][j] = new Grass(i, j, this); // assume everything else is grass
                        break; // intentionally dont handle _ case easier bug fix
                }
                i++;
            }
            while(i < sqrMapSize){ // fill trailing empty text with grass
                matrix[i][j] = new Grass(i, j, this);
                i++;
            }
            System.out.println("row " + j + " read");

        }

        
        scan.close(); // close scanner
        return matrix;
    }

    public void updateAllPaths(){ // iterate through paths to find type and orientation
        for(Tile[] row: this.land){
            for(Tile entry: row){
                if(entry instanceof Path){
                    ((Path)entry).updatePath(); // type cast into path type
                }
            }
        }
    }

    public void draw(PApplet app){ // draw each element in matrix onto screen
        for(Tile[] row: this.land){
            for(Tile entry: row){
                if(!(entry instanceof Wizard)){ // draw it if its not a wizard house
                    entry.draw(app); 
                } else{ // if it is a wizard house, draw grass under the wizard house
                    Tile wizGrass = new Grass(wizCordsXY[0], wizCordsXY[1], this);
                    wizGrass.draw(app);
                }
            }
        }
        this.land[wizCordsXY[0]][wizCordsXY[1]].draw(app); // draw wizard house last so it is drawn on top layer
    }

    public static void main(String args[]){
        
    }
}








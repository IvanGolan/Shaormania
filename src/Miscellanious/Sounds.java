package Miscellanious;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sounds {

    Clip clip;
    URL soundURL[] = new URL[4];

    public Sounds(){
        soundURL[0] = getClass().getResource("/Sounds/MenuMusic.wav");
        soundURL[1] = getClass().getResource("/Sounds/Level1Music.wav");
        soundURL[2] = getClass().getResource("/Sounds/Level2Music.wav");
    }

    public void setFile(int i){

        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            if(ais != null){
                clip = AudioSystem.getClip();
                clip.open(ais);
            }
            else {
                throw new CantLoadSoundException();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}

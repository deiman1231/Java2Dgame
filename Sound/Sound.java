package Sound;

import Characters.Entity;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import javax.swing.*;

public class Sound extends JFrame {

    public void gunSound(Entity.GunType gun){
        if (gun == Entity.GunType.M4){
            playSound("res\\Sound\\ak47.wav");
        } else if (gun == Entity.GunType.pistol){
            playSound("res\\Sound\\pistol.wav");
        } else if (gun == Entity.GunType.shotGun){
            playSound("res\\Sound\\Shotgun.wav");
        } else if (gun == Entity.GunType.grenadeL){
            playSound("res\\Sound\\rocketl.wav");
        }

    }

    private void playSound(String sound_directory) {
        try {
            URL url = this.getClass().getClassLoader().getResource(sound_directory);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
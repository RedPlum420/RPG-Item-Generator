import javax.sound.sampled.*;
public class SoundGenerator {

    public static void playTone(double freq, int durationMs, double volume) throws LineUnavailableException {
        float sampleRate = 44100;

        //Calculates essetnially the time the sound is going to be played
        byte[] buf = new byte[(int)(durationMs * sampleRate / 1000)];

        //Creates the audioFormat through which the sounds are going to be played
        AudioFormat af = new AudioFormat(sampleRate, 8, 1, false, false);  // unsigned

        for (int i = 0; i < buf.length; i++) {
            //Sound is a wave, thus it could be interpretted using a sin function
            //Based on the frquency we determine the period of the sign wave, so
            // the speakers could later intepret the sin value function and play the sound
            double angle = i / (sampleRate / freq) * 2.0 * Math.PI;

            //Calculates the sample value of the wave (how loud is the noise going to be played)
            int val = (int)(Math.sin(angle) * volume * 127 + 128);

            // Store the sample as a byte in the buffer which will be played
            buf[i] = (byte)(val & 0xFF);
        }

        //SourceDataLine is basiclly the ObjectOutputStream for audio, it creates one matching the audio format
        try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)) {
            sdl.open(af);
            sdl.start();
            //plays the sound with the specified length
            sdl.write(buf, 0, buf.length);
            //Play everything in queue before closing the stream
            sdl.drain();
        }
    }

    // Punchy sound for normal button clicks
    public void playPunch() throws LineUnavailableException {
        playTone(150, 100, 1.0); // 150 Hz, 100ms
        playTone(300, 100, 0.6);  // 300 Hz, 50ms

    }

    // Plays A minor
    public void playChime() throws LineUnavailableException {
       playTone(440, 150, 1.0);
       playTone(523.25, 150, 0.8);
       playTone(659.25, 150, 0.7);
    }
}

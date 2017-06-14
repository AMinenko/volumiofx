package com.anmi.volumiofx.flac;

import org.jflac.FLACDecoder;
import org.jflac.PCMProcessor;
import org.jflac.metadata.StreamInfo;
import org.jflac.util.ByteData;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Decoder implements PCMProcessor {
    private AudioFormat fmt;
    private DataLine.Info info;
    private SourceDataLine line;
    private List listeners = new ArrayList<LineListener>();


    public void decode(String fileName) throws IOException {
        FileInputStream is = new FileInputStream(fileName);
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.addPCMProcessor(this);
        decoder.decode();
    }

    public void decode(InputStream file) throws IOException {
        FLACDecoder decoder = new FLACDecoder(file);
        decoder.addPCMProcessor(this);
        decoder.decode();
    }


    public void addListener(LineListener listener) {
        listeners.add(listener);
    }

    //Somehow Internally used method - AnMi
    @Override
    public void processStreamInfo(StreamInfo streamInfo) {
        try {
           /* if (isPlaying()) {
                stopPlay();
            }*/
            fmt = streamInfo.getAudioFormat();
            info = new DataLine.Info(SourceDataLine.class, fmt, AudioSystem.NOT_SPECIFIED);
            line = (SourceDataLine) AudioSystem.getLine(info);

            //  Add the listeners to the line at this point, it's the only
            //  way to get the events triggered.
            int size = listeners.size();
            for (int index = 0; index < size; index++)
                line.addLineListener((LineListener) listeners.get(index));

            line.open(fmt, AudioSystem.NOT_SPECIFIED);
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            line.drain();
            line.close();
        }
    }

    @Override
    //Somehow Internally used method - AnMi
    public void processPCM(ByteData pcm) {
        line.write(pcm.getData(), 0, pcm.getLen());
    }

    public boolean isPlaying() {
        return line != null && line.isOpen();
    }

    public void stopPlay() {
        line.drain();
        line.close();
    }
}

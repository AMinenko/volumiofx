package com.anmi.volumiofx.flac;

import org.jflac.FLACDecoder;
import org.jflac.PCMProcessor;
import org.jflac.metadata.StreamInfo;
import org.jflac.util.ByteData;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decoder implements PCMProcessor {
    private AudioFormat fmt;
    private DataLine.Info info;
    private SourceDataLine line;
    private List listeners = new ArrayList<LineListener>();
    LineListener lineListener = event -> System.out.println(event.getType());
    List<LineListener> lineListeners = Arrays.asList(lineListener);


    public void decode(InputStream file) throws IOException {
        FLACDecoder decoder = new FLACDecoder(file);
        decoder.addPCMProcessor(this);
        while (!decoder.isEOF()) {
            decoder.decode();
        }
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
                line.addLineListener((LineListener) lineListeners.get(index));

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

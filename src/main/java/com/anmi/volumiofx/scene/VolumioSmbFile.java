package com.anmi.volumiofx.scene;


import jcifs.smb.SmbFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
@AllArgsConstructor
public class VolumioSmbFile {
    private String name;
    private URL url;

    public static VolumioSmbFile ofSmbFile(SmbFile smbFile) {
        if(smbFile==null){
            System.out.println();
        }
        return new VolumioSmbFile(smbFile.getName(), smbFile.getURL());
    }

    @Override
    public String toString() {
        return this.name;
    }
}

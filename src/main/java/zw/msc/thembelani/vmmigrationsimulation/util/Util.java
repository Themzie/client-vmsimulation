package zw.msc.thembelani.vmmigrationsimulation.util;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

public class Util {
    public static String getSampleData (){
            try {
                FileInputStream fis = null;


                fis = new FileInputStream("src/main/resources/static/sampledata.txt");
                return IOUtils.toString(fis, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }


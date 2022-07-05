import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        File savegames = new File ("E://Games//savegames");
        GameProgress save1 = new GameProgress(1,2,3,4);
        GameProgress save2 = new GameProgress(5,6,7,8);
        GameProgress save3 = new GameProgress(9,10,11,12);
        saveGame("E://Games//savegames//save1.dat", save1);
        saveGame("E://Games//savegames//save2.dat", save2);
        saveGame("E://Games//savegames//save3.dat", save3);
        List<String> savesList = new ArrayList<>();
        File[] listFiles = savegames.listFiles();
        for (var file : listFiles) {
            if (file.isFile()) {
                savesList.add(file.getAbsolutePath());
            }
        }
        File zip = new File ("E://Games//savegames//zip.zip");
        zipFiles(String.valueOf(zip), savesList);
        for (var item : listFiles) {
           if (item.getName().contains(".dat")) {
               item.delete();
           }
        }

    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(gameProgress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFiles(String zipPath, List<String> savesList) {
        int counter = 1;
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (var item : savesList) {
                FileInputStream fileInputStream = new FileInputStream(String.valueOf(item));
                try {

                    ZipEntry zipEntry = new ZipEntry("save" + counter + ".dat");
                    zipOutputStream.putNextEntry(zipEntry);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    zipOutputStream.write(buffer);
                    zipOutputStream.closeEntry();
                    counter++;
                } finally {
                    fileInputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
        openZip(String.valueOf(zip), String.valueOf(savegames));
        System.out.println(openProgress("E://Games//savegames//save3.dat"));
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
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (var item : savesList) {
                try (FileInputStream fileInputStream = new FileInputStream(String.valueOf(item))) {
                    ZipEntry zipEntry = new ZipEntry(String.valueOf(Path.of(item).getFileName()));
                    zipOutputStream.putNextEntry(zipEntry);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    zipOutputStream.write(buffer);
                    zipOutputStream.closeEntry();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openZip(String zipPath, String savegames) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))){
            ZipEntry entry;
            String name;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(savegames + "//" + name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fout.write(c);
                }
                fout.flush();
                zis.closeEntry();
                fout.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameProgress openProgress(String path) {
        Object temp = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            temp = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (GameProgress) temp;
    }
}

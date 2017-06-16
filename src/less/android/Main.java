package less.android;

import java.io.IOException;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get("./resources");
            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY
            );

            WatchKey watchKey;

            while ((watchKey = watchService.take()) != null) {
                for (WatchEvent<?> event: watchKey.pollEvents()) {

                    if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                        Path modifiedPath = (Path) event.context();
                        readFile(path.resolve(modifiedPath));
                    }
                }
                watchKey.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void readFile(Path path) {
        try {
            Object[] lines = Files.readAllLines(path).toArray();
            for (Object line: lines) {
                System.out.println((String) line);
            }
            System.out.println("=================>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

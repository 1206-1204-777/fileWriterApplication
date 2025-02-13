import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class FolderWatcherTest {
    private static final String TEST_FOLDER = "test";
    private static final String TEST_FILE = "test/testFile.txt";
    private static final String TEST2_FOLDER = "test/test2";
    private FolderWatcher watcher;
    private ExecutorService executor;

    @BeforeEach
    void setUp() throws IOException {
        // 必要なフォルダーを作成
        Files.createDirectories(Paths.get(TEST_FOLDER));

        // FolderWatcherのインスタンスを作成
        watcher = new FolderWatcher(TEST_FOLDER);

        // 監視を別スレッドで実行
        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> watcher.watchFolder());
    }

    @AfterEach
    void tearDown() throws IOException {
        // 監視を停止
        watcher.stopWatching();
        executor.shutdown();

        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // テスト用フォルダーを削除
        Files.walk(Paths.get(TEST_FOLDER))
             .sorted((a, b) -> b.compareTo(a)) // 子から削除するため逆順ソート
             .forEach(path -> {
                 try {
                     Files.deleteIfExists(path);
                 } catch (IOException ignored) {}
             });
    }

    @Test
    void testFolderCreationOnFileCreate() throws IOException, InterruptedException {
        // testフォルダー内に.txtファイルを作成
        Files.createFile(Paths.get(TEST_FILE));

        // test2フォルダーが作成されるまで最大5秒待機
        Path test2Path = Paths.get(TEST2_FOLDER);
        boolean folderCreated = false;
        for (int i = 0; i < 10; i++) {
            if (Files.exists(test2Path)) {
                folderCreated = true;
                break;
            }
            Thread.sleep(500);
        }

        assertTrue(folderCreated, "test2 フォルダーが作成されるべき");
    }
}

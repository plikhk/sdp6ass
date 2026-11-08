package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileSystemVisitorTest {
    private DirectoryNode root;
    private FileNode file1;
    private FileNode file2;
    private DirectoryNode subDir;
    private FileNode subFile;

    @BeforeEach
    public void setUp() {
        root = new DirectoryNode("root");
        file1 = new FileNode("readme.txt", 100);
        file2 = new FileNode("data.csv", 500);
        subDir = new DirectoryNode("docs");
        subFile = new FileNode("report.pdf", 1000);

        subDir.addChild(subFile);
        root.addChild(file1);
        root.addChild(file2);
        root.addChild(subDir);
    }

    @Test
    public void testT1AndT2SizeAccumulation() {
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);
        assertEquals(1600, sizeVisitor.getTotalSize());
    }

    @Test
    public void testT3AndT4SearchVisitor() {
        SearchVisitor searchVisitor = new SearchVisitor(root, "re", 50);
        root.accept(searchVisitor);
        List<String> paths = searchVisitor.getMatchingPaths();
        assertEquals(2, paths.size());
        assertEquals("/docs/report.pdf", paths.get(0));
        assertEquals("/readme.txt", paths.get(1));
    }

    @Test
    public void testT5StatisticsVisitor() {
        StatisticsVisitor stats = new StatisticsVisitor(root);
        root.accept(stats);
        assertEquals(3, stats.getFileCount());
        assertEquals(2, stats.getDirectoryCount());
        assertEquals(2, stats.getMaxDepth());
    }

    @Test
    public void testT6EmptyStructuresAndNoMatch() {
        DirectoryNode emptyRoot = new DirectoryNode("empty");
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        emptyRoot.accept(sizeVisitor);
        assertEquals(0, sizeVisitor.getTotalSize());
    }

    @Test
    public void testT7StateReusability() {
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);
        assertEquals(1600, sizeVisitor.getTotalSize());
        sizeVisitor.reset();
        root.accept(sizeVisitor);
        assertEquals(1600, sizeVisitor.getTotalSize());
    }

    @Test
    public void testT8InvalidTreeMutations() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad.txt", -10));
        assertThrows(IllegalArgumentException.class, () -> root.addChild(new FileNode("readme.txt", 200)));
    }
}
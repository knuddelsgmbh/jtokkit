package com.knuddels.jtokkit;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.stream.Collectors.toCollection;

public class DataDownloader {

    public static void main(String[] args) throws Exception {
        var bookIds = new int[]{
                // English
                10,
                11,
                16,
                23,
                35,
                36,
                41,
                43,
                45,
                46,
                55,
                74,
                76,
                84,
                98,
                100,
                120,
                132,
                140,
                158,
                161,
                174,
                203,
                205,
                209,
                215,
                219,
                236,
                244,
                345,
                408,
                512,
                514,
                521,
                600,
                730,
                768,
                779,
                829,
                844,
                932,
                996,
                1080,
                1184,
                1232,
                1250,
                1251,
                1260,
                1342,
                1399,
                1400,
                1497,
                1513,
                1661,
                1727,
                1952,
                1998,
                2000,
                2148,
                2542,
                2554,
                2591,
                2600,
                2680,
                2701,
                2814,
                2852,
                3207,
                3296,
                3825,
                4217,
                4300,
                4363,
                5200,
                5827,
                6130,
                6133,
                6753,
                7370,
                8492,
                8800,
                10007,
                11030,
                15399,
                16328,
                20203,
                24869,
                25282,
                25344,
                26184,
                27827,
                28054,
                30254,
                31284,
                35899,
                41445,
                42324,
                58585,
                64317,
                67098,

                // Chinese
                25328,

                // French
                18812,

                // Greek
                39536,

                // Hungarian
                34759,

                // Japanese
                1982,

                // Arabic
                43007,

                // Hebrew
                61024
        };
        SortedSet<Integer> uniqueBookIds = Arrays.stream(bookIds).boxed().collect(toCollection(TreeSet::new));
        assert uniqueBookIds.size() == 100; // top 100 books in txt format

        var rootFolder = Paths.get("benchmark/data");
        uniqueBookIds.parallelStream()
                .forEach(bookId -> downloadBook(bookId, rootFolder));

        var patterns = new String[]{
                "'s", "'t", "'re", "'ve", "'m", "'ll", "'d", "'x",
                "x",
                "123",
                "ő",
                ",.!?:; \n",
                "\n   \n",
                "     ",
                "\t\u000B\u000C\u0085\u00A0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A\u2028\u2029\u202F\u205F\u3000",
                "😀😁😂😃😄😅😆😇😈😉😊😋😌😍😎😏😐😑😒😓😔😕😖😗😘😙😚😛😜😝😞😟😠😡😢😣😤😥😦😧😨😩😪😫😬😭😮😯😰😱😲😳😴😵😶😷😸😹😺😻😼😽😾😿🙀🙁🙂🙃🙄🙅🙆🙇🙈🙉🙊🙋🙌🙍🙎🙏"
        };

        // really long strings, basically a dos attack
        var bytesPerFile = 20_000;
        for (var i = 0; i < patterns.length; i++) {
            var fileName = String.format("test_%d_%d.txt", i, bytesPerFile);
            var path = rootFolder.resolve(fileName);
            generateFile(path, patterns[i], bytesPerFile);
        }

        var sourceCodes = new String[]{
                "https://raw.githubusercontent.com/raspberrypi/linux/1c4c7647c8514dc6c8bf843a8bf69732437e6b98/drivers/acpi/cppc_acpi.c",
                "https://raw.githubusercontent.com/raspberrypi/linux/1c4c7647c8514dc6c8bf843a8bf69732437e6b98/drivers/usb/musb/cppi_dma.c",
                "https://raw.githubusercontent.com/raspberrypi/linux/1c4c7647c8514dc6c8bf843a8bf69732437e6b98/drivers/gpu/drm/amd/include/asic_reg/dcn/dcn_3_2_0_sh_mask.h"
        };
        for (var url : sourceCodes) {
            var fileName = url.substring(url.lastIndexOf('/') + 1);
            downloadUrl(url, rootFolder, fileName);
        }

        var totalSize = calculateTotalFileSize(rootFolder);
        if (totalSize != 99_945_290) {
            throw new AssertionError("Total size did not match expected value, actual: " + totalSize);
        }
    }

    private static void generateFile(Path path, String pattern, int sizeInBytes) {
        var builder = new StringBuilder();
        while (builder.length() < sizeInBytes) {
            builder.append(pattern);
        }

        try {
            Files.writeString(path, builder.toString(), CREATE);
            System.out.println("File created: " + path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void downloadBook(int bookId, Path rootFolder) {
        try {
            var fileName = String.format("%d.txt", bookId);
            var format = String.format("https://www.gutenberg.org/cache/epub/%d/pg%d.txt", bookId, bookId);
            downloadUrl(format, rootFolder, fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void downloadUrl(String url, Path rootFolder, String fileName) throws IOException {
        var fileUrl = new URL(url);
        var localPath = rootFolder.resolve(fileName);
        if (Files.exists(localPath)) {
            System.out.printf("File %s already exists, skipping download.%n", fileName);
        } else {
            System.out.printf("Downloading %s...\n", fileName);
            Files.copy(fileUrl.openStream(), localPath);
        }
    }

    public static long calculateTotalFileSize(Path rootFolder) {
        try (var paths = Files.walk(rootFolder)) {
            return paths.filter(Files::isRegularFile).mapToLong(DataDownloader::fileSize).sum();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static long fileSize(Path path) {
        try {
            var size = Files.size(path);
            return size;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
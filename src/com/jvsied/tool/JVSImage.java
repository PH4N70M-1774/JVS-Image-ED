package com.jvsied.tool;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

//OFFICIAL VERSION: Version 1.0.50
//RELEASE DATE: 12-01-2026 21:15 IST
public class JVSImage {
    public static int CURRENT_VERSION = 1;

    public static int getType(String extension) {
        return switch (extension) {
            case ".jpg" -> 1;
            case ".png" -> 2;
            case ".gif" -> 3;
            case "jpeg" -> 4;
            default -> 0;
        };
    }

    public class Encoder {
        public static void encode(String src, String out) throws IOException {
            if(!out.endsWith("jvsei")){
                throw new RuntimeException("Output file not a valid JVS Image file.");
            }

            byte[] imageBytes = Files.readAllBytes(Path.of(src));

            try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Path.of(out).toFile()))) {

                // Magic
                output.writeBytes("JVSIEF"); // 6 bytes

                // Version
                output.writeByte(CURRENT_VERSION); // 1 byte

                // Type
                output.writeByte(getType(src.substring(src.length() - 4))); // 1 byte

                // Raw image bytes
                output.write(imageBytes);
            }
        }
    }

    public class Decoder {
        public static void view(String src) throws Exception {
            File jvsiFile = new File(src);

            if(!src.endsWith("jvsei")){
                throw new RuntimeException("Source file not a valid JVS Image file.");
            }

            byte[] imageBytes;

            try (DataInputStream in = new DataInputStream(new FileInputStream(jvsiFile))) {

                // ---- MAGIC CHECK ----
                if (in.readByte() != 'J' ||
                        in.readByte() != 'V' ||
                        in.readByte() != 'S' ||
                        in.readByte() != 'I' ||
                        in.readByte() != 'E' ||
                        in.readByte() != 'F') {
                    throw new RuntimeException("Not a JVS Image file");
                }

                // ---- VERSION CHECK ----
                byte version = in.readByte();
                if (version != 1) {
                    throw new RuntimeException("Unsupported JVS Image version: " + version);
                }

                // ---- TYPE BYTE (currently unused) ----
                in.readByte();

                // ---- IMAGE BYTES ----
                imageBytes = in.readAllBytes();
            }

            // ---- Decode image properly ----
            ByteArrayInputStream imgStream = new ByteArrayInputStream(imageBytes);

            BufferedImage img = ImageIO.read(imgStream);

            if (img == null) {
                throw new RuntimeException("Image decode failed");
            }

            // ---- Display FULL QUALITY ----
            JFrame frame = new JFrame("JVSI Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JLabel(new ImageIcon(img)));
            frame.pack(); // VERY IMPORTANT
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        public static void decode(String src, String out) throws Exception {
            File jvsiFile = new File(src);

            if(!src.endsWith("jvsei")){
                throw new RuntimeException("Source file not a valid JVS Image file.");
            }

            byte[] imageBytes;
            String type = "";

            try (DataInputStream in = new DataInputStream(new FileInputStream(jvsiFile))) {

                // ---- MAGIC CHECK ----
                if (in.readByte() != 'J' ||
                        in.readByte() != 'V' ||
                        in.readByte() != 'S' ||
                        in.readByte() != 'I' ||
                        in.readByte() != 'E' ||
                        in.readByte() != 'F') {
                    throw new RuntimeException("Not a JVS Image file");
                }

                // ---- VERSION CHECK ----
                byte version = in.readByte();
                if (version != 1) {
                    throw new RuntimeException("Unsupported JVS Image version: " + version);
                }

                // ---- TYPE BYTE (currently unused) ----
                type = switch (in.readByte()) {
                    case 1 -> "jpg";
                    case 2 -> "png";
                    case 3 -> "gif";
                    case 4 -> "jpeg";
                    default -> "jvsei";
                };

                // ---- IMAGE BYTES ----
                imageBytes = in.readAllBytes();
            }
            if (!out.endsWith(type)) {
                out = out.substring(0, out.lastIndexOf('.') + 1) + type;
            }
            try (FileOutputStream fo = new FileOutputStream(out)) {
                fo.write(imageBytes);
            }
        }
    }
}

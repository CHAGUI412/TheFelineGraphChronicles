package gui.theme;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

public final class CatSprites {

    public enum Character { POLA, MINERVA, NERO, LIMON, NINA }

    private static final Map<Character, BufferedImage> CACHE = new EnumMap<>(Character.class);

    private CatSprites() {
    }

    public static BufferedImage get(Character character) {
        return CACHE.computeIfAbsent(character, CatSprites::load);
    }

    private static BufferedImage load(Character character) {
        String fileName = switch (character) {
            case POLA -> "pola.png";
            case MINERVA -> "minerva.png";
            case NERO -> "nero.png";
            case LIMON -> "limon.png";
            // Nina reutiliza el mismo archivo que Nero: así lo confirmó
            // explícitamente el usuario (la imagen #3 subida es la que
            // se usa para Nina en Misión 2), aunque no calce del todo
            // con la historia -- decisión consciente, no un error.
            case NINA -> "nero.png";
        };
        try (InputStream in = CatSprites.class.getResourceAsStream("/cats/" + fileName)) {
            if (in == null) {
                return null;
            }
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }

    public static void drawAvatar(Graphics2D g2, Character character, int cx, int cy, int diameter) {
        BufferedImage image = get(character);
        if (image == null) {
            return;
        }

        Graphics2D clipped = (Graphics2D) g2.create();
        clipped.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clipped.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        clipped.setClip(new Ellipse2D.Float(cx - diameter / 2f, cy - diameter / 2f, diameter, diameter));

        int srcSize = Math.min(image.getWidth(), image.getHeight());
        int srcX = (image.getWidth() - srcSize) / 2;
        int srcY = (image.getHeight() - srcSize) / 2;

        clipped.drawImage(image,
                cx - diameter / 2, cy - diameter / 2, cx + diameter / 2, cy + diameter / 2,
                srcX, srcY, srcX + srcSize, srcY + srcSize,
                null);

        clipped.setClip(null);
        clipped.setColor(FelineTheme.ACCENT_GOLD);
        clipped.setStroke(new BasicStroke(2f));
        clipped.draw(new Ellipse2D.Float(cx - diameter / 2f, cy - diameter / 2f, diameter, diameter));

        clipped.dispose();
    }
}
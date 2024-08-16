package com.accounting_manager.accounting_engine.Classes.document;

import com.accounting_manager.accounting_engine.Classes.block.Block;
import com.accounting_manager.accounting_engine.Classes.fontcolor.ColorPalette;
import com.accounting_manager.accounting_engine.Classes.geometry.Region;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Log4j2
class _DocumentTest {

    private ColorPalette colorPaletteMock;

    private Region regionMock;

    private Block blockMock;
    @Spy
    private List<Block> blocksMock = new ArrayList<>();

    private _Document document;

    private BufferedImage image;

    private Graphics2D gMock;

    @BeforeEach
    public void setUp() {
        regionMock = mock(Region.class);
        blockMock = mock(Block.class);
        gMock = Mockito.mock(Graphics2D.class);
        colorPaletteMock = mock(ColorPalette.class);
        image = new BufferedImage(768, 1024, BufferedImage.TYPE_INT_ARGB);
        document = new _Document(regionMock, blocksMock);

    }

    @Test
    public void testDraw() {
//        document.Draw(g, colorPalette);
//        assertTrue(g.getColor() == Color.WHITE);

        when(colorPaletteMock.GetCellBlockColor()).thenReturn(Color.white);
        // Create a mock Region object

        when(regionMock.getLeft()).thenReturn(0);
        when(regionMock.getTop()).thenReturn(0);
        when(regionMock.getWidth()).thenReturn(100);
        when(regionMock.getHeight()).thenReturn(100);

        document.Draw(gMock, colorPaletteMock);
        Mockito.verify(gMock).setColor(Color.white);
        Mockito.verify(gMock).fillRect(0,0,100,100);
    }

    @Test
    public void testSaveAsImage() throws IOException {
        // nom de l'image et format
        String path ="src/test/resources/images/";
        String fileName = "test_image";
        String imageFormat = "png";

        document = new _Document(new Region(0,0,758,1500),blocksMock);
        try {
            document.saveAsImage(path+fileName, imageFormat);
        } catch (IOException e) {
            log.error(e);
        }

        // Vérifier que le fichier image a été créé avec succès
        assertTrue(new File(path + fileName + "." + imageFormat).exists());
    }

}

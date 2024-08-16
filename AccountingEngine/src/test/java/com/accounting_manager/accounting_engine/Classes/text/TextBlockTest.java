package com.accounting_manager.accounting_engine.Classes.text;

import com.accounting_manager.accounting_engine.Classes.fontcolor.ColorPalette;
import com.accounting_manager.accounting_engine.Classes.fontcolor.FacFont;
import com.accounting_manager.accounting_engine.Classes.geometry.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextBlockTest {

    @Mock
    private Region regionMock;

    @Mock
    private Graphics2D gMock;

    @Mock
    private ColorPalette colorPaletteMock;

    @Mock
    private FacFont facFontMock;

    @BeforeEach
    public void setUp() {
        when(regionMock.getLeft()).thenReturn(80);
        when(regionMock.getTop()).thenReturn(150);
        when(regionMock.getWidth()).thenReturn(160);
        when(regionMock.getHeight()).thenReturn(30);

        when(colorPaletteMock.GetTextBlockColor()).thenReturn(new Color(212, 212, 212));
        when(facFontMock.getTrueFont()).thenReturn(new Font("Arial", Font.BOLD, 16));
    }

    @Test
    public void testDraw() {
        String text = "SOME TEXT";
        TextBlock textBlock = new TextBlock(regionMock, text, facFontMock);

        textBlock.Draw(gMock, colorPaletteMock);

        verify(gMock).setColor(new Color(212, 212, 212));
        verify(gMock).fillRect(80, 150, 160, 30);
        verify(gMock).setColor(Color.BLACK);
        verify(gMock).setFont(new Font("Arial", Font.BOLD, 16));
        verify(gMock).drawString("SOME TEXT", 80, 150);
    }
}

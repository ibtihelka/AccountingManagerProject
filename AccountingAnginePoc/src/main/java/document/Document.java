package document;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import All.Block;
import fontcolor.ColorPalette;
import geometry.Region;

public class Document extends Block implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4961379465008537685L;

	// <summary Proportion de la largeur de la zone gauche. </summary>
	public static float	ZONE_LEFT_WIDTH = 0.25f;
	
	// <summary Proportion de la largeur de la zone droite. </summary>
	public static float	ZONE_RIGHT_WIDTH = 0.25f;
	
	// <summary>Proportion de l'hauteur de la zone de haut.</summary>
	public static float	ZONE_TOP_HEIGHT = 0.3f;
	
	// <summary> Proportion de l'hauteur de la zone de base. </summary>
	public static float	ZONE_BOTTOM_HEIGHT = 0.2f;
	
	// <summary> Région de la zone <see cref="FacDocumentZone.TOP_LEFT" />. </summary>
	public Region topLeft;
	
	// <summary> Région de la zone <see cref="FacDocumentZone.TOP_CENTER" />. </summary>
	public Region topCenter;
	
	// <summary> Région de la zone <see cref="FacDocumentZone.TOP_RIGHT" />. </summary>
	public Region topRight;
	
	// <summary> Région de la zone <see cref="FacDocumentZone.MIDDLE_LEFT" />. </summary>
	public Region middleLeft;
	
	// <summary> Région de la zone <see cref="FacDocumentZone.MIDDLE_CENTER" />. </summary>
	public Region middleCenter;
	
	// <summary> Région de la zone <see cref="FacDocumentZone.MIDDLE_RIGHT" />. </summary>
	public Region middleRight;
	
	// <summary> Région de la zone <see cref="FacDocumentZone.BOTTOM_LEFT" />. </summary>
	public Region bottomLeft;
	
	// <summary>Région de la zone <see cref="FacDocumentZone.BOTTOM_CENTER" />.</summary>
	public Region bottomCenter;
	
	// <summary>Région de la zone <see cref="FacDocumentZone.BOTTOM_RIGHT" />.</summary>
	public Region bottomRight;
	
	// <summary>Traitement appliqué au document.</summary>
	private DocumentProcessing processing;
	
	// <summary>Rotation appliqué au document.</summary>
	private DocumentRotation rotation;
	
	// <summary>Tableau qui contient tous les blocs.</summary>
	private Block[] allBlocks;
	
	// <summary> Obtient le traitement appliqué au document.</summary>
	public DocumentProcessing getProcessing() {
		return processing;	
	}
	
	//<summary> Obtient la rotation appliqué au document.</summary>
	public DocumentRotation getRotation() {
		return rotation;
	}
	
	// <summary> Setter le traitement appliqué au document.</summary>
	public void setProcessing(DocumentProcessing processing) {
		this.processing = processing;
	}
	
	//<summary> Setter la rotation appliqué au document.</summary>
	public void setRotation(DocumentRotation rotation) {
		this.rotation = rotation;
	}
	
	
	// <summary> Initialise une nouvelle instance de la classe avec des paramétres spécifiés. </summary>
			// <param name="region">Region du document.</param>
			// <param name="blocks">Liste des blocs qui composent le document.</param>
			public Document (Region region, Block [] blocks)
			{
				super(region, blocks);
				// Fixer les regions des zones.
				int	topZoneHeight = (int)(region.getHeight() * Document.ZONE_TOP_HEIGHT);
				int				bottomZoneHeight = (int)(region.getHeight() * Document.ZONE_BOTTOM_HEIGHT);
				int				middleZoneHeight = region.getHeight() - (topZoneHeight + bottomZoneHeight);

				int				leftZoneWidth = (int)(region.getWidth() * Document.ZONE_LEFT_WIDTH);
				int				rightZoneWidth = (int)(region.getWidth() * Document.ZONE_RIGHT_WIDTH);
				int				centerZoneWidth = region.getWidth() - (leftZoneWidth + rightZoneWidth);

				this.topLeft = new Region (0, 0, leftZoneWidth, topZoneHeight);
				this.topCenter = new Region (leftZoneWidth, 0, leftZoneWidth + centerZoneWidth, topZoneHeight);
				this.topRight = new Region (leftZoneWidth + centerZoneWidth, 0, region.getRight(), topZoneHeight);

				this.middleLeft = new Region (0, topZoneHeight, leftZoneWidth, topZoneHeight + middleZoneHeight);
				this.middleCenter = new Region (leftZoneWidth, topZoneHeight, leftZoneWidth + centerZoneWidth, topZoneHeight + middleZoneHeight);
				this.middleRight = new Region (leftZoneWidth + centerZoneWidth, topZoneHeight, region.getRight(), topZoneHeight + middleZoneHeight);

				this.bottomLeft = new Region (0, topZoneHeight + middleZoneHeight, leftZoneWidth, region.getBottom());
				this.bottomCenter = new Region (leftZoneWidth, topZoneHeight + middleZoneHeight, leftZoneWidth + centerZoneWidth, region.getBottom());
				this.bottomRight = new Region (leftZoneWidth + centerZoneWidth, topZoneHeight + middleZoneHeight, region.getRight(), region.getBottom());
				
				
			}
	
	// <summary>Retourne la zone d'une Région.</summary>
	// <param name="region">Une region.</param>
	// <returns>Zone du document de la Région.</returns>
	public DocumentZone GetZone (Region region){
		
			DocumentZone zone=null;
			
			zone.setNumDocumentZone(DocumentZone.UNDEFINED.getNumDocumentZone());

			if (this.topLeft.Share(region))
			
				zone.setNumDocumentZone( zone.getNumDocumentZone() | DocumentZone.TOP_LEFT.getNumDocumentZone());
				
			if (this.middleLeft.Share(region))
				zone.setNumDocumentZone(zone.getNumDocumentZone() | DocumentZone.MIDDLE_LEFT.getNumDocumentZone());
				
			if (this.bottomLeft.Share(region))
				zone.setNumDocumentZone(zone.getNumDocumentZone() | DocumentZone.BOTTOM_LEFT.getNumDocumentZone());
				
			if (this.topRight.Share(region))
				zone.setNumDocumentZone(zone.getNumDocumentZone() | DocumentZone.TOP_RIGHT.getNumDocumentZone());
				
			if (this.middleRight.Share(region))
				zone.setNumDocumentZone(zone.getNumDocumentZone() | DocumentZone.MIDDLE_RIGHT.getNumDocumentZone());
				
			if (this.bottomRight.Share(region))
				zone.setNumDocumentZone(zone.getNumDocumentZone() | DocumentZone.BOTTOM_RIGHT.getNumDocumentZone());
				
			if (this.topCenter.Share(region))
				zone.setNumDocumentZone(zone.getNumDocumentZone() | DocumentZone.TOP_CENTER.getNumDocumentZone());
				
			if (this.middleCenter.Share(region))
				zone.setNumDocumentZone( zone.getNumDocumentZone() | DocumentZone.MIDDLE_CENTER.getNumDocumentZone());
				
			if (this.bottomCenter.Share(region))
				zone.setNumDocumentZone(zone.getNumDocumentZone() | DocumentZone.BOTTOM_CENTER.getNumDocumentZone());

				return zone;
			}
	
			// <summary> Retourne les Régions d'une zone. </summary>
			// <param name="zone">Zone.</param>
			// <returns>Tableau de Régions de la zone passée en paramètre <paramref name="zone" />.</returns>
			public Region [] GetZoneRegions (DocumentZone zone)
			{
				List<Region> regions = new ArrayList<Region>();

				if ((zone.getNumDocumentZone() & DocumentZone.TOP_LEFT.getNumDocumentZone()) == DocumentZone.TOP_LEFT.getNumDocumentZone())
					regions.add(this.topLeft);
				if ((zone.getNumDocumentZone() & DocumentZone.MIDDLE_LEFT.getNumDocumentZone()) == DocumentZone.MIDDLE_LEFT.getNumDocumentZone())
					regions.add(this.middleLeft);
				if ((zone.getNumDocumentZone() & DocumentZone.BOTTOM_LEFT.getNumDocumentZone()) == DocumentZone.BOTTOM_LEFT.getNumDocumentZone())
					regions.add(this.bottomLeft);
				if ((zone.getNumDocumentZone() & DocumentZone.TOP_RIGHT.getNumDocumentZone()) == DocumentZone.TOP_RIGHT.getNumDocumentZone())
					regions.add(this.topRight);
				if ((zone.getNumDocumentZone() & DocumentZone.MIDDLE_RIGHT.getNumDocumentZone()) == DocumentZone.MIDDLE_RIGHT.getNumDocumentZone())
					regions.add(this.middleRight);
				if ((zone.getNumDocumentZone() & DocumentZone.BOTTOM_RIGHT.getNumDocumentZone()) == DocumentZone.BOTTOM_RIGHT.getNumDocumentZone())
					regions.add(this.bottomRight);
				if ((zone.getNumDocumentZone() & DocumentZone.TOP_CENTER.getNumDocumentZone()) == DocumentZone.TOP_CENTER.getNumDocumentZone())
					regions.add(this.topCenter);
				if ((zone.getNumDocumentZone() & DocumentZone.MIDDLE_CENTER.getNumDocumentZone()) == DocumentZone.MIDDLE_CENTER.getNumDocumentZone())
					regions.add(this.middleCenter);
				if ((zone.getNumDocumentZone() & DocumentZone.BOTTOM_CENTER.getNumDocumentZone()) == DocumentZone.BOTTOM_CENTER.getNumDocumentZone())
					regions.add(this.bottomCenter);

				return (Region[]) regions.toArray ();
			}
			
			// <summary> Retourne tous les blocs du document. </summary>
			public Block [] GetAllBlocks ()
			{
				if (this.allBlocks == null)
					this.allBlocks = super.GetAllBlocks(DocumentZone.UNDEFINED, false);

				return this.allBlocks;
			}
			
			// <summary> Sérialize l'instance en cours. </summary>
			// <returns>Flux de données aprés sérialization.</returns>
			public byte [] Serialize () throws IOException
			{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
			    ObjectOutputStream oos = new ObjectOutputStream(bos);
			    
			    oos.writeObject(this);
			    oos.flush();
			    
			     return bos.toByteArray();
			      
			}
			
			
			// <summary> Desérialize une instance du type en cours</summary>
			// <param name="bytes">Flux de données qui contient les données de l'instance.</param>
			// <returns>Une instance du type en cours <see cref="FacDocument" />.</returns>
			public static Document Deserialize (byte [] bytes) throws IOException, ClassNotFoundException
			{
				if (bytes == null)
					return null;
				else
				{
					ByteArrayInputStream in = new ByteArrayInputStream(bytes);
					ObjectInputStream is = new ObjectInputStream(in);
					
					return (Document)is.readObject();
					
				}
				
			}
				
			// Serialize Any Object
			public static byte[] serialize(Object obj) throws IOException {
			    ByteArrayOutputStream out = new ByteArrayOutputStream();
			    ObjectOutputStream os = new ObjectOutputStream(out);
			    os.writeObject(obj);
			    return out.toByteArray();
			}
			
			// Deserialize Any Object
			public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
			    ByteArrayInputStream in = new ByteArrayInputStream(data);
			    ObjectInputStream is = new ObjectInputStream(in);
			    return is.readObject();
			}
			
			// <summary> Dessine le bloc en cours sur une instance d'un graphics.</summary>
			// <param name="g">L'instance sur laquelle on fait le dessin.</param>
			// <param name="colorPalette">Palette des couleurs.</param>
				
			@Override
			public void Draw(Graphics2D g, ColorPalette colorPalette) {
			
				g.setColor(Color.WHITE);
				g.fillRect(this.getRegion().getLeft(), this.getRegion().getTop(), this.getRegion().getWidth(), this.getRegion().getHeight());
				DrawInnerBlocks (g, colorPalette);
			}
			
			// <summary> Sauvegarde le document en cours comme étant une image. </summary>
			// <param name="fileName">Nom complet du fichier.</param>
			// <param name="imageFormat">Format de l'image.</param>
			public void saveAsImage (String fileName, String imageFormat) throws IOException
			{		
				final BufferedImage image = new BufferedImage ( this.getRegion().getWidth(), this.getRegion().getHeight(), BufferedImage.TYPE_INT_ARGB );	
				final Graphics2D g = image.createGraphics ();			
				ColorPalette		p = new ColorPalette ();
				Draw (g, p);
				ImageIO.write ( image, imageFormat, new File ( fileName+"."+imageFormat ) );
				g.dispose();
				System.gc();
			}
			
			// <summary> Sauvegarde le document en cours comme étant une image. </summary>
			// <param name="fileName">Nom complet du fichier sans extension.</param>
			public void saveAsImage (String fileName) throws IOException
			{
				saveAsImage (fileName, "JPEG");
			}
			
				
			
}

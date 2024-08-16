package text;

import java.awt.Color;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import All.Block;
import All._Out;
import document.Document;
import fontcolor.ColorPalette;
import fontcolor.FacFont;
import geometry.Region;
import key.KeyStringChar;

public class TextBlock extends Block implements Serializable {
	
			
			/**
	 * 
	 */
	private static final long serialVersionUID = 6061506486937854776L;

			/// Chaine de caractères qui représente
			/// le contenu du bloc.
			/// </summary>
			private String				text;
			/// <summary>
			/// Police de caractères du texte
			/// <see cref="_Text" />.
			/// </summary>
			private FacFont				font;
			/// <summary>
			/// Indique que le bloc de texte
			/// en cours est un bloc virtuel.
			/// Ce champ est rempli par la méthode <see cref="Split" />.
			/// </summary>
			private boolean				isVirtual;
			/// <summary>
			/// Dans le cas d'une séquence de blocs textes
			/// cet attribut représente le bloc texte suivant
			/// c'est à dire celui à droite de l'instance en cours.
			/// </summary>
			/// <remarks>Une valeur nulle indique que c'est un bloc de fin de séquence.</remarks>
			private TextBlock		nextTextBlock;
			/// <summary>
			/// Dans le cas d'une séquence de blocs textes
			/// cet attribut représente le bloc texte précedent
			/// c'est à dire celui à gauche de l'instance en cours.
			/// </summary>
			/// <remarks>Une valeur nulle indique que c'est un bloc de fin de séquence.</remarks>
			private TextBlock		previousTextBlock;
			/// <summary>
			/// Partie gauche de la séquence du bloc
			/// en cours.
			/// </summary>
			
			private TextBlock []		leftSequence;
			/// <summary>
			/// Partie droite de la séquence du bloc
			/// en cours.
			/// </summary>
			
			private TextBlock []		rightSequence;
			/// <summary>
			/// Séquence du bloc en cours.
			/// </summary>
			
			private TextBlock []		sequence;
			/// <summary>
			/// Indique que l'instance en cours
			/// a été marquée pour une séquence. Ceci
			/// évite que l'instance en cours sera
			/// utilisée plusieurs fois.
			/// </summary>
			
			private boolean				_isSeqMarked;
			/// <summary>
			/// Indique que le bloc a été marqué pour une clé.
			/// Ceci permet de réserver les blocs et de resoudre
			/// les doubles utilisations.
			/// </summary>
			
			private boolean				_isKeyMarked;
			/// <summary>
			/// Taux par défaut de la largeur
			/// maximale entre deux mots d'une séquence.
			/// </summary>
			public static float			SEQ_SPACING_RATE = KeyStringChar.MAX_SEPERATOR_WIDTH;
			

			@Override
			public void Draw(Graphics2D g, ColorPalette colorPalette) {
				
				g.setColor(colorPalette.GetTextBlockColor ());
				g.fillRect(this.getRegion().getLeft(), this.getRegion().getTop(), this.getRegion().getWidth(), this.getRegion().getHeight());

			
				// Chaine de caractères.
				
				g.setColor(Color.BLACK);
				
				g.setFont(this.getFont().getTrueFont());
				
				g.drawString(this.getText(),this.getRegion().getLeft(), this.getRegion().getTop());		
			}



			public String getText() {
				return text;
			}



			public FacFont getFont() {
				return font;
			}



			public boolean isVirtual() {
				return isVirtual;
			}



			public TextBlock getNextTextBlock() {
				return nextTextBlock;
			}



			public TextBlock getPreviousTextBlock() {
				return previousTextBlock;
			}



			public TextBlock[] getLeftSequence() {
				if (this.leftSequence == null)
					UpdateSequences ();
				
				return leftSequence;
			}



			public TextBlock[] getRightSequence() {
				if (this.rightSequence == null)
					UpdateSequences ();
				
				return rightSequence;
			}



			public TextBlock[] getSequence() {
				if (this.sequence == null)
					UpdateSequences ();
				return sequence;
			}



			public boolean is_isSeqMarked() {
				return _isSeqMarked;
			}



			public boolean is_isKeyMarked() {
				return _isKeyMarked;
			}



			public void setText(String text) {
				this.text = text;
			}



			public void setFont(FacFont font) {
				this.font = font;
			}



			public void setVirtual(boolean isVirtual) {
				this.isVirtual = isVirtual;
			}



			public void setNextTextBlock(TextBlock nextTextBlock) {
				this.nextTextBlock = nextTextBlock;
			}



			public void setPreviousTextBlock(TextBlock previousTextBlock) {
				this.previousTextBlock = previousTextBlock;
			}



			public void setLeftSequence(TextBlock[] leftSequence) {
				this.leftSequence = leftSequence;
			}



			public void setRightSequence(TextBlock[] rightSequence) {
				this.rightSequence = rightSequence;
			}



			public void setSequence(TextBlock[] sequence) {
				this.sequence = sequence;
			}



			public void set_isSeqMarked(boolean _isSeqMarked) {
				this._isSeqMarked = _isSeqMarked;
			}



			public void set_isKeyMarked(boolean _isKeyMarked) {
				this._isKeyMarked = _isKeyMarked;
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="region">Region du bloc.</param>
			/// <param name="text">Contenu du bloc.</param>
			/// <param name="font">Police de caractères du texte <paramref name="text" />.</param>
			public TextBlock (Region region, String text, FacFont font){
				
				super(region, new Block [0]);
				// Supprimer le caractère spécial '8232'.
				int			cleanCount = 0;
				
				for (int i = text.length() - 1; i > -1; i --)
				{
					if (text.charAt(i) == 8232)
						cleanCount ++;
					else
						break;
				}

				if (cleanCount != 0)
					text = text.substring (0, text.length() - cleanCount);
				// Filtrer : '[' et ']'.
				
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < text.length(); i++) {
				    char ch = text.charAt(i);
				    if (ch != '[' && ch != ']') {
				        sb.append(ch);
				    }
				}
				
				// Continuer.
				this.text = sb.toString();
				this.font = font;
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés. Ce constructeur permet
			/// la fusion de plusieurs blocs textes.
			/// </summary>
			/// <param name="textBlocks">Liste de blocs textes.</param>
			public TextBlock (TextBlock [] textBlocks) 
			{
				this(Block.GetRegion (textBlocks), TextBlock.getMergedText (textBlocks), textBlocks [0].getFont());
				Document		document = textBlocks [0].getDocument();
				if (document != null)
					this.setDocument(document);

				this.isVirtual = true;
			}
			

			/// <summary>
			/// Retourne la chaine de caractères représentant
			/// le contenu du bloc text.
			/// </summary>
			/// <returns>Chaine de caractères de type <see cref="string" />.</returns>
			@Override
			public String toString(){
				
				return "TextBlock{" + this.text + "}";
			}
			
			
			/// <summary>
			/// Retourne le texte résultat de la fusion
			/// d'une liste de blocs textes.
			/// </summary>
			/// <param name="textBlocks">Tableau de blocs textes.</param>
			/// <returns>Une instance d'une chaine de caractères.</returns>
			private static String getMergedText (TextBlock [] textBlocks)
			{
				String			text = textBlocks [0].getText();

				for (int i = 1; i < textBlocks.length; i ++)
				{text += textBlocks [i].getText();}

				return text;
			}
			
			
			/// <summary>
			/// Divise le bloc texte en cours en 3 blocs textes à partir
			/// de l'indice <paramref name="index" />.
			/// </summary>
			/// <param name="index">Indice de départ de la chaine centrale.</param>
			/// <param name="length">Longeur de la chaine centrale.</param>
			/// <param name="left">Bloc gauche de la division. Une valeur null si aucun bloc gauche ne peut être formé.</param>
			/// <param name="right">Bloc droite de la division. Une valeur null si aucun bloc droite ne peut être formé.</param>
			/// <returns>Bloc centrale de la division.</returns>
			public TextBlock Split (int index, int length,  _Out<TextBlock> left,   _Out<TextBlock> right)
			{
				Region		region = this.getRegion();
				// Partie gauche.
				String			leftString = this.getText().substring (0, index);
				int				value = region.getLeft() + this.getFont().GetWidth (leftString);

				if (value > region.getRight())
					value = region.getRight();

				Region		leftRegion = new Region (region.getLeft(), region.getTop(), value, region.getBottom());

				if (leftString == "")
					left = null;
				else
				{
					left.set(new TextBlock (leftRegion, leftString, this.font));
					left.get().setDocument( this.getDocument()); 
					left.get().isVirtual = true;
				}
				// Partie centrale.
				TextBlock	center;
				String			centralString = this.getText().substring (index, length);
				value = leftRegion.getRight() + this.getFont().GetWidth(centralString);

				if (value > region.getRight())
					value = region.getRight();

				Region		centerRegion = new Region (leftRegion.getRight(), region.getTop(), value, region.getBottom());;

				if (centralString == "")
					center = null;
				else
				{
					center = new TextBlock (centerRegion, centralString, this.font);
					center.setDocument(this.getDocument());
					center.isVirtual = true;
				}
				// Partie droite.
				String			rightString = this.getText().substring (index + length, this.getText().length() - index - length);
				Region		rightRegion = new Region (centerRegion.getRight(), region.getTop(), region.getRight(), region.getBottom());

				if (rightString == "")
					right = null;
				else
				{
					right.set(new TextBlock (rightRegion, rightString, this.font));
					right.get().setDocument(this.getDocument());
					right.get().isVirtual = true;
				}
				// Affecter les valeurs de la séquence.

				// Cas particulier.
				if (left == null && right == null)
					return this;
				// Au moins deux valeurs.
				else
				{
					if (left != null)
					{
						left.get().setPreviousTextBlock(this.getPreviousTextBlock()) ;
						left.get().setNextTextBlock( center);
					}
					
					if (center != null)
					{
						center.setPreviousTextBlock(left.get());
						center.setNextTextBlock(right.get());
					}

					if (right != null)
					{
						right.get().setPreviousTextBlock(center);
						right.get().setNextTextBlock (this.getNextTextBlock());
					}

					return center;
				}
			}
			
			/// <summary>
			/// Retourne la séquence à gauche du bloc en cours.
			/// Le bloc en cours sera inclu.
			/// </summary>
			/// <returns>Tableau de blocks textes <see cref="FacTextBlock" /> qui représente la séquence.</returns>
			private TextBlock [] GetLeftSequence()
			{
				List<TextBlock>	sequence = new ArrayList<TextBlock> ();
				TextBlock		currentBlock = this;

				while (currentBlock != null)
				{
					sequence.add (currentBlock);
					currentBlock = currentBlock.getPreviousTextBlock();
				}

				Collections.reverse (sequence);

				return (TextBlock[]) sequence.toArray ();
			}
			/// <summary>
			/// Retourne la séquence à droite du bloc en cours.
			/// Le bloc en cours sera inclu.
			/// </summary>
			/// <returns>Tableau de blocks textes <see cref="FacTextBlock" /> qui représente la séquence.</returns>
			private TextBlock [] GetRightSequence ()
			{
				List<TextBlock>	sequence = new ArrayList<TextBlock> ();
				TextBlock		currentBlock = this;

				while (currentBlock != null)
				{
					sequence.add (currentBlock);
					currentBlock = currentBlock.getNextTextBlock();
				}

				return (TextBlock[]) sequence.toArray ();
			}
			
			/// <summary>
			/// Met à jour les séquences de droite, gauche et totale.
			/// </summary>
			public void UpdateSequences (){
				
				
				TextBlock []		left = getLeftSequence ();
				TextBlock []		right = getRightSequence ();

				TextBlock []		sequence = new TextBlock [left.length + right.length - 1];
				sequence=Arrays.copyOfRange (left, 0,left.length);

				for (int i = 1; i < right.length; i ++)
				{sequence [left.length + i - 1] = right [i];}

				this.leftSequence = left;
				this.rightSequence = right;
				this.sequence = sequence;
			}
			
			/// <summary>
			/// Retourne la chaine de caratères qui
			/// est concaténation de toutes les chaines
			/// de caractères de la séquence passée en paramètre.
			/// </summary>
			/// <param name="sequence">Tableau de blocs qui représente la séquence.</param>
			/// <param name="sep">Séparetur entre les blocs de la séquance.</param>
			/// <returns>Une chaine de caractères <see cref="string" />.</returns>
			public static String getSequenceString (TextBlock [] sequence, String sep)
			{
				StringBuilder		text = new StringBuilder ();

				for (int i = 0; i < sequence.length; i ++)
				{
					if (i > 0)
						text.append (sep);

					text.append (sequence [i].text);
				}

				return text.toString();
			}
			
			/// <summary>
			/// Retourne la chaine de caratères qui
			/// est concaténation de toutes les chaines
			/// de caractères de la séquence passée en paramètre.
			/// </summary>
			/// <param name="sequence">Tableau de blocs qui représente la séquence.</param>
			/// <returns>Une chaine de caractères <see cref="string" />.</returns>
			public static String GetSequenceString (TextBlock [] sequence){
				
				return TextBlock.getSequenceString (sequence, "");
				
			}
			
			/// <summary>
			/// Retourne une sous séquence de la séquence passée en paramètre
			/// en utilisant le taux d'espacement passé en paramètre.
			/// </summary>
			/// <param name="sequence">Séquence objet de la recherche.</param>
			/// <param name="startBlock">Bloc central de la séquence.</param>
			/// <param name="spacingRate">Taux d'espacement. Une valeur négative n'est pas acceptable.</param>
			/// <returns>Liste des blocs textes qui représentent la sous séquence.</returns>
			public static TextBlock [] GetSubSequence (TextBlock [] sequence, TextBlock startBlock, float spacingRate)
			{
				if (spacingRate < 0)
					throw new  IllegalArgumentException ("Negative value is not allowed ,spacingRate");

				int						width, maxWidth = (int)(spacingRate * sequence [0].getDocument().getRegion().getWidth());
				List<TextBlock>		subSequence = new ArrayList<TextBlock> ();
				TextBlock			seq;
				// Extraire l'indice du bloc central.
				int						startBlockIndex = Region.getIndex (startBlock, sequence);

				if (startBlockIndex <0 )
					throw new IllegalArgumentException ("Start bloc must included in sequance blocks");
				// Partie gauche.
				for (int i = startBlockIndex - 1; i > -1; i --)
				{
					seq = sequence [i];
					width = sequence [i + 1].getRegion().getLeft() - seq.getRegion().getLeft();

					if (width > maxWidth)
						break;
					else
						subSequence.add (seq);
				}

				Collections.reverse (subSequence);
				// Partie centrale.
				subSequence.add (sequence [startBlockIndex]);
				// Partie droite.
				for (int i = startBlockIndex + 1; i < sequence.length; i ++)
				{
					seq = sequence [i];
					width = seq.getRegion().getLeft() - sequence [i - 1].getRegion().getLeft();

					if (width > maxWidth)
						break;
					else
						subSequence.add (seq);
				}

				return (TextBlock[]) subSequence.toArray ();
			}
			
			/// <summary>
			/// Retourne une sous séquence de la séquence passée en paramètre
			/// en utilisant le taux d'espacement passé en paramètre.
			/// </summary>
			/// <param name="sequence">Séquence objet de la recherche.</param>
			/// <param name="startBlock">Bloc central de la séquence.</param>
			/// <returns>Liste des blocs textes qui représentent la sous séquence.</returns>
			public static TextBlock [] GetSubSequence (TextBlock [] sequence, TextBlock startBlock){
				
				return TextBlock.GetSubSequence (sequence, startBlock, TextBlock.SEQ_SPACING_RATE);
			}
			
			/// <summary>
			/// Retourne la séquence marqué d'une séquence.
			/// </summary>
			/// <param name="sequence">Séquence.</param>
			/// <param name="startBlock">Bloc de début.</param>
			/// <returns>Tableau qui contient la sous séquence.</returns>
			public static TextBlock [] GetUnmarkedSequence (TextBlock [] sequence, TextBlock startBlock)
			{
				List<TextBlock>		subSequence = new ArrayList<TextBlock> ();
				TextBlock			seq;
				// Extraire l'indice du bloc central.
				int						startBlockIndex = Region.getIndex(startBlock, sequence);

				if (startBlockIndex <0 )
					throw new IllegalArgumentException ("Start bloc must included in sequance blocks");
				// Partie gauche.
				for (int i = startBlockIndex - 1; i > -1; i --)
				{
					seq = sequence [i];

					if (seq.is_isSeqMarked() || seq.is_isKeyMarked())
						break;
					else
						subSequence.add (seq);
				}

				Collections.reverse (subSequence);
				// Partie centrale.
				subSequence.add (sequence [startBlockIndex]);
				// Partie droite.
				for (int i = startBlockIndex + 1; i < sequence.length; i ++)
				{
					seq = sequence [i];

					if (seq.is_isSeqMarked() || seq.is_isSeqMarked())
						break;
					else
						subSequence.add (seq);
				}

				return (TextBlock[]) subSequence.toArray ();
			}
			
			/// <summary>
			/// Marque une séquence en se servant de la propriété <see cref="IsSeqMarked" />.
			/// </summary>
			/// <param name="sequence">Séquence à marquer.</param>
			/// <param name="markValue">Valeur du marquage.</param>
			public static void markSequence (Block [] sequence, boolean markValue)
			{
				TextBlock		textBlock = null;

				for(Block seqBlock : sequence)
				{
					if(seqBlock instanceof TextBlock) {
						textBlock =(TextBlock) seqBlock;
						}
					 
	 
					if (textBlock != null)
						textBlock.set_isSeqMarked(markValue);
				}
			}
			

			
}
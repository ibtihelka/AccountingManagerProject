package geometry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import All.Block;
import All._Out;

public class GeometryHelper implements Serializable {
	
			/**
	 * 
	 */
	private static final long serialVersionUID = -8974022982602267747L;
	
			// <summary>
			/// Taux par défaut de la largeur de l'intervalle
			/// utilisée pour faire les recherches des blocs
			/// dans le cas d'un horizontal.
			/// </summary>
			public static float			HALIGN_RATE = 0.9f;
			/// <summary>
			/// Taux par défaut de la largeur de l'intervalle
			/// utilisée pour faire les recherches des blocs
			/// dans le cas d'un horizontal.
			/// </summary>
			public static float			VALIGN_RATE = 0.9f;

			/// <summary>
			/// Retourne les paramètres d'un bloc à gauche d'un bloc référence.
			/// </summary>
			/// <param name="refBlock">Bloc référence.</param>
			/// <param name="block">Bloc.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignement">Alignement du bloc par rapport au bloc référence.</param>
			/// <param name="distance">Distance du bloc par rapport au bloc référence.</param>
			/// <param name="shadow">Ombre du bloc par rapport au bloc référence.</param>
			public static void getLeftBlockParams (Block refBlock, Block block, Block [] allBlocks,  _Out<Integer> alignement,_Out<Integer>distance, _Out<Integer>shadow)
			{
				Region		refRegion = refBlock.getRegion();
				Region		region = block.getRegion();
				// Alignement.
				alignement.set((region.getTop() + region.getBottom()) / 2 - (refRegion.getTop() + refRegion.getBottom()) / 2);
				// Distance.
				int				x = (region.getLeft() + region.getRight()) / 2 - (refRegion.getLeft() + refRegion.getRight()) / 2;
				int				y = alignement.get();

				distance.set((int)Math.sqrt (x * x + y * y));
				// Ombre.
				shadow.set(0);
				Region		_region;
				int				_top, _bottom;

				for(Block _block : allBlocks)
				{
					_region = _block.getRegion();

					if (_region.getLeft() > region.getRight() &&
						_region.getRight() < refRegion.getLeft() &&
						_region.getBottom() >= region.getTop() &&
						_region.getTop() <= region.getBottom())
					{
						// Top.
						_top = _region.getTop();

						if (_top < region.getTop())
							_top = region.getTop();
						// Bottom.
						_bottom = _region.getBottom();

						if (_bottom > region.getBottom())
							_bottom = region.getBottom();
						// Ombre.
						shadow.set(shadow.get()+ _bottom - _top);
					}
				}
			}
			/// <summary>
			/// Retourne les paramètres d'un bloc en haut d'un bloc référence.
			/// </summary>
			/// <param name="refBlock">Bloc référence.</param>
			/// <param name="block">Bloc.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignement">Alignement du bloc par rapport au bloc référence.</param>
			/// <param name="distance">Distance du bloc par rapport au bloc référence.</param>
			/// <param name="shadow">Ombre du bloc par rapport au bloc référence.</param>
			public static void getUpBlockParams (Block refBlock, Block block, Block [] allBlocks,_Out<Integer> alignement,_Out<Integer>  distance,_Out<Integer> shadow)
			{
				Region		refRegion = refBlock.getRegion();
				Region		region = block.getRegion();
				// Alignement.
				alignement.set((region.getRight() + region.getLeft()) / 2 - (refRegion.getRight() + refRegion.getLeft()) / 2);
				// Distance.
				int				x = alignement.get();
				int				y = (region.getTop() + region.getBottom()) / 2 - (refRegion.getTop() + refRegion.getBottom()) / 2;

				distance.set( (int)Math.sqrt (x * x + y * y));
				// Ombre.
				shadow.set( 0);
				Region		_region;
				int				_left, _right;

				for(Block _block : allBlocks)
				{
					_region = _block.getRegion();

					if (_region.getTop() > region.getBottom() &&
						_region.getBottom() < refRegion.getTop() &&
						_region.getRight() >= region.getLeft() &&
						_region.getLeft() <= region.getRight())
					{
						// Left.
						_left = _region.getLeft();

						if (_left < region.getLeft())
							_left = region.getLeft();
						// Right.
						_right = _region.getRight();

						if (_right > region.getRight())
							_right = region.getRight();
						// Ombre.
						shadow.set(shadow.get()+ _right - _left);
					}
				}
			}
			/// <summary>
			/// Retourne les paramètres d'un bloc à droite d'un bloc référence.
			/// </summary>
			/// <param name="refBlock">Bloc référence.</param>
			/// <param name="block">Bloc.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignement">Alignement du bloc par rapport au bloc référence.</param>
			/// <param name="distance">Distance du bloc par rapport au bloc référence.</param>
			/// <param name="shadow">Ombre du bloc par rapport au bloc référence.</param>
			public static void getRightBlockParams (Block refBlock, Block block, Block [] allBlocks, _Out<Integer> alignement, _Out<Integer> distance, _Out<Integer> shadow)
			{
				Region		refRegion = refBlock.getRegion();
				Region		region = block.getRegion();
				// Alignement.
				alignement.set((region.getTop() + region.getBottom()) / 2 - (refRegion.getTop() + refRegion.getBottom()) / 2);
				// Distance.
				int				x = (region.getLeft() + region.getRight()) / 2 - (refRegion.getLeft() + refRegion.getRight()) / 2;
				int				y = alignement.get();

				distance.set((int)Math.sqrt (x * x + y * y));
				// Ombre.
				shadow.set(0);
				Region		_region;
				int				_top, _bottom;

				for(Block _block : allBlocks)
				{
					_region = _block.getRegion();

					if (_region.getLeft() > refRegion.getRight() &&
						_region.getRight() < region.getLeft() &&
						_region.getBottom() >= region.getTop() &&
						_region.getTop() <= region.getBottom())
					{
						// Top.
						_top = _region.getTop();

						if (_top < region.getTop())
							_top = region.getTop();
						// Bottom.
						_bottom = _region.getBottom();

						if (_bottom > region.getBottom())
							_bottom = region.getBottom();
						// Ombre.
						shadow.set(shadow.get() + _bottom - _top);
					}
				}
			}
			/// <summary>
			/// Retourne les paramètres d'un bloc en bas d'un bloc référence.
			/// </summary>
			/// <param name="refBlock">Bloc référence.</param>
			/// <param name="block">Bloc.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignement">Alignement du bloc par rapport au bloc référence.</param>
			/// <param name="distance">Distance du bloc par rapport au bloc référence.</param>
			/// <param name="shadow">Ombre du bloc par rapport au bloc référence.</param>
			public static void getDownBlockParams (Block refBlock, Block block, Block [] allBlocks, _Out<Integer> alignement, _Out<Integer> distance, _Out<Integer> shadow)
			{
				Region		refRegion = refBlock.getRegion();
				Region		region = block.	getRegion();
				// Alignement.
				alignement.set((region.getRight() + region.getLeft()) / 2 - (refRegion.getRight() + refRegion.getLeft()) / 2);
				// Distance.
				int				x = alignement.get();
				int				y = (region.getTop() + region.getBottom()) / 2 - (refRegion.getTop() + refRegion.getBottom()) / 2;

				distance.set( (int)Math.sqrt (x * x + y * y));
				// Ombre.
				shadow.set( 0);
				Region		_region;
				int				_left, _right;

				for(Block _block : allBlocks)
				{
					_region = _block.getRegion();

					if (_region.getTop() > refRegion.getBottom() &&
						_region.getBottom() < region.getTop() &&
						_region.getRight() >= region.getLeft() &&
						_region.getLeft() <= region.getRight())
					{
						// Left.
						_left = _region.getLeft();

						if (_left < region.getLeft())
							_left = region.getLeft();
						// Right.
						_right = _region.getRight();

						if (_right > region.getRight())
							_right = region.getRight();
						// Ombre.
						shadow.set(shadow.get() + _right - _left);
					}
				}
			}
			/// <summary>
			/// Retourne les blocs à gauche d'un bloc donné en utilisant
			/// un taux d'alignement <paramref name="alignRate" />.
			/// </summary>
			/// <param name="block">Bloc référence.</param>
			/// <param name="alignRate">Taux de l'alignement. Une valeur négative indique qu'il n'y a pas d'alignement.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignements">Tableau qui contient les alignements des blocs par rapport au bloc référence.</param>
			/// <param name="distances">Tableau qui contient les distances des blocs par rapport au bloc référence.</param>
			/// <param name="shadows">Tableau qui contient les ombres des blocs par rapport au bloc référence.</param>
			/// <returns>Liste des blocs qui ont été trouvés.</returns>
			@SuppressWarnings("unchecked")
			public static Block [] GetLeftBlocks (Block block, float alignRate, Block [] allBlocks, _Out<Integer>[]alignements,_Out<Integer>[]distances, _Out<Integer>[]shadows)
			{
				// Trier les blocs.
				Block []			_allBlocks = new Block [allBlocks.length + 1];
				
				_allBlocks= Arrays.copyOfRange(allBlocks, 0, allBlocks.length);
				_allBlocks [_allBlocks.length - 1] = block;

				int []				rights = new int [_allBlocks.length];

				for (int i = 0; i < _allBlocks.length; i ++)
				{rights [i] = _allBlocks [i].getRegion().getRight();}


				List<Block> _allBlocksList = Arrays.asList(_allBlocks);
				ArrayList<Block> sorted_allBlocksList = new ArrayList<Block>(_allBlocksList);
				Collections.sort(sorted_allBlocksList, Comparator.comparing(s -> rights[_allBlocksList.indexOf(s)]).reversed());
				
				// Déclarer les listes.
				List<Block>		selBlocks = new ArrayList<Block> ();
				List<Integer>			selAlignements = new ArrayList<Integer> ();
				List<Integer>			selDistances = new ArrayList<Integer> ();
				List<Integer>			selShadows = new ArrayList<Integer> ();
				// Commencer le filtrage.
				Region			region = block.getRegion();
				int					regionMiddle = (region.getTop() + region.getBottom()) / 2;
				int					align;

				Block			_block;
				Region			_region, __region;
				int					_regionMiddle;
				int					_align;

				int					blockIndex = -1;
				int					x, y, dist;
				int					shadow;
				int					__top, __bottom;

				for (int i = 0; i < _allBlocks.length; i ++)
				{
					 _block = _allBlocks [i];
					_region = _block.getRegion();
					// Bloc référence trouvé.
					if (block == _block)
						blockIndex = i;
					// Sélection zone droite.
					else if (!region.includes (_region) && _region.getRight() <= region.getLeft())
					{
						_regionMiddle = (_region.getTop() + _region.getBottom()) / 2;
						// Calculer l'alignement.
						_align = _regionMiddle - regionMiddle;
						align = (int)(alignRate * (_region.getHeight() > region.getHeight() ? _region.getHeight() : region.getHeight()));

						if (alignRate < 0 || Math.abs (_align) <= align)
						{
							selBlocks.add (_block);
							selAlignements.add (_align);
							// Calculer la distance.
							x = (region.getLeft() + region.getRight()) / 2 - (_region.getLeft() + _region.getRight()) / 2;
							y = _align;
							dist = (int)Math.sqrt (x * x + y * y);
							selDistances.add (dist);
							// Calculer l'ombre.
							shadow = 0;

							for (int j = blockIndex + 1; j < i; j ++)
							{
								__region = _allBlocks [j].getRegion();

								if (__region.getLeft() > _region.getRight() &&
									__region.getRight() < region.getLeft() &&
									__region.getBottom() >= _region.getTop() &&
									__region.getTop() <= _region.getBottom())
								{
									// Top.
									__top = __region.getTop();

									if (__top < _region.getTop())
										__top = _region.getTop();
									// Bottom.
									__bottom = __region.getBottom();

									if (__bottom > _region.getBottom())
										__bottom = _region.getBottom();
									// Ombre.
									shadow += __bottom - __top;
								}
							}

							selShadows.add (shadow);
						}
					}
				}

				alignements = (_Out<Integer>[]) selAlignements.toArray ();
				distances = (_Out<Integer>[]) selDistances.toArray ();
				shadows = (_Out<Integer>[]) selShadows.toArray ();

				return (Block[]) selBlocks.toArray ();
			}
			
			/// <summary>
			/// Retourne les blocs à gauche d'un bloc donné en utilisant
			/// un taux d'alignement <paramref name="alignRate" />.
			/// </summary>
			/// <param name="block">Bloc référence.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignements">Tableau qui contient les alignements des blocs par rapport au bloc référence.</param>
			/// <param name="distances">Tableau qui contient les distances des blocs par rapport au bloc référence.</param>
			/// <param name="shadows">Tableau qui contient les ombres des blocs par rapport au bloc référence.</param>
			/// <returns>Liste des blocs qui ont été trouvés.</returns>
			public static Block [] GetLeftBlocks (Block block, Block [] allBlocks, _Out<Integer>[] alignements,_Out<Integer>[] distances, _Out<Integer>[] shadows)
			{return GeometryHelper.GetLeftBlocks (block, GeometryHelper.HALIGN_RATE, allBlocks,  alignements,  distances,  shadows);}
			/// <summary>
			/// Retourne les blocs de haut d'un bloc donné en utilisant
			/// un taux d'alignement <paramref name="alignRate" />.
			/// </summary>
			/// <param name="block">Bloc référence.</param>
			/// <param name="alignRate">Taux de l'alignement. Une valeur négative indique qu'il n'y a pas d'alignement.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignements">Tableau qui contient les alignements des blocs par rapport au bloc référence.</param>
			/// <param name="distances">Tableau qui contient les distances des blocs par rapport au bloc référence.</param>
			/// <param name="shadows">Tableau qui contient les ombres des blocs par rapport au bloc référence.</param>
			/// <returns>Liste des blocs qui ont été trouvés.</returns>
			@SuppressWarnings("unchecked")
			public static Block [] GetUpBlocks (Block block, float alignRate, Block [] allBlocks, _Out<Integer>[] alignements, _Out<Integer>[] distances, _Out<Integer>[] shadows)
			{
				// Trier les blocs.
				Block []			_allBlocks = new Block [allBlocks.length + 1];
				
				_allBlocks= Arrays.copyOfRange(allBlocks, 0, allBlocks.length);
				_allBlocks [_allBlocks.length - 1] = block;

				int []				bottoms = new int [_allBlocks.length];

				for (int i = 0; i < _allBlocks.length; i ++)
				{bottoms [i] = _allBlocks [i].getRegion().getBottom();}

				
				List<Block> _allBlocksList = Arrays.asList(_allBlocks);
				ArrayList<Block> sorted_allBlocksList = new ArrayList<Block>(_allBlocksList);
				Collections.sort(sorted_allBlocksList, Comparator.comparing(s -> bottoms[_allBlocksList.indexOf(s)]).reversed());
				
				// Déclarer les listes.
				List<Block>		selBlocks = new ArrayList<Block> ();
				List<Integer>			selAlignements = new ArrayList<Integer> ();
				List<Integer>			selDistances = new ArrayList<Integer> ();
				List<Integer>			selShadows = new ArrayList<Integer> ();
				// Commencer le filtrage.
				Region			region = block.getRegion();
				int					regionMiddle = (region.getRight() + region.getLeft()) / 2;
				int					align;

				Block			_block;
				Region			_region, __region;
				int					_regionMiddle;
				int					_align;

				int					blockIndex = -1;
				int					x, y, dist;
				int					shadow;
				int					__left, __right;

				for (int i = 0; i < _allBlocks.length; i ++)
				{
					 _block = _allBlocks [i];
					_region = _block.getRegion();
					// Bloc référence trouvé.
					if (block == _block)
						blockIndex = i;
					// Sélection zone droite.
					else if (!region.includes (_region) && _region.getBottom() <= region.getTop())
					{
						_regionMiddle = (_region.getRight() + _region.getLeft()) / 2;
						// Calculer l'alignement.
						_align = _regionMiddle - regionMiddle;
						align = (int)(alignRate * (_region.getWidth() > region.getWidth() ? _region.getWidth() : region.getWidth()));

						if (alignRate < 0 || Math.abs (_align) <= align)
						{
							selBlocks.add (_block);
							selAlignements.add (_align);
							// Calculer la distance.
							x = _align;
							y = (region.getTop() + region.getBottom()) / 2 - (_region.getTop() + _region.getBottom()) / 2;
							dist = (int)Math.sqrt (x * x + y * y);
							selDistances.add (dist);
							// Calculer l'ombre.
							shadow = 0;

							for (int j = blockIndex + 1; j < i; j ++)
							{
								__region = _allBlocks [j].getRegion();

								if (__region.getTop() > _region.getBottom() &&
									__region.getBottom() < region.getTop() &&
									__region.getRight() >= _region.getLeft() &&
									__region.getLeft() <= _region.getRight())
								{
									// Left.
									__left = __region.getLeft();

									if (__left < _region.getLeft())
										__left = _region.getLeft();
									// Right.
									__right = __region.getRight();

									if (__right > _region.getRight())
										__right = _region.getRight();
									// Ombre.
									shadow += __right - __left;
								}
							}

							selShadows.add (shadow);
						}
					}
				}

				alignements = (_Out<Integer>[]) selAlignements.toArray ();
				distances = (_Out<Integer>[]) selDistances.toArray ();
				shadows = (_Out<Integer>[]) selShadows.toArray ();

				return (Block[]) selBlocks.toArray ();
			}
			/// <summary>
			/// Retourne les blocs de haut d'un bloc donné en utilisant
			/// un taux d'alignement <paramref name="alignRate" />.
			/// </summary>
			/// <param name="block">Bloc référence.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignements">Tableau qui contient les alignements des blocs par rapport au bloc référence.</param>
			/// <param name="distances">Tableau qui contient les distances des blocs par rapport au bloc référence.</param>
			/// <param name="shadows">Tableau qui contient les ombres des blocs par rapport au bloc référence.</param>
			/// <returns>Liste des blocs qui ont été trouvés.</returns>
			public static Block [] GetUpBlocks (Block block, Block [] allBlocks, _Out<Integer> [] alignements, _Out<Integer> [] distances, _Out<Integer> [] shadows)
			{return GeometryHelper.GetUpBlocks (block, GeometryHelper.VALIGN_RATE, allBlocks,  alignements,  distances,  shadows);}
			/// <summary>
			/// Retourne les blocs à droite d'un bloc donné en utilisant
			/// un taux d'alignement <paramref name="alignRate" />.
			/// </summary>
			/// <param name="block">Bloc référence.</param>
			/// <param name="alignRate">Taux de l'alignement. Une valeur négative indique qu'il n'y a pas d'alignement.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignements">Tableau qui contient les alignements des blocs par rapport au bloc référence.</param>
			/// <param name="distances">Tableau qui contient les distances des blocs par rapport au bloc référence.</param>
			/// <param name="shadows">Tableau qui contient les ombres des blocs par rapport au bloc référence.</param>
			/// <returns>Liste des blocs qui ont été trouvés.</returns>

			@SuppressWarnings("unchecked")

			public static Block [] GetRightBlocks (Block block, float alignRate, Block [] allBlocks, _Out<Integer>[] alignements,_Out<Integer>[] distances,_Out<Integer>[] shadows)
			{
				// Trier les blocs.
				Block []			_allBlocks = new Block [allBlocks.length + 1];
				_allBlocks= Arrays.copyOfRange(allBlocks, 0, allBlocks.length);
				_allBlocks [_allBlocks.length - 1] = block;

				int []				lefts = new int [_allBlocks.length];

				for (int i = 0; i < _allBlocks.length; i ++)
				{lefts [i] = _allBlocks [i].getRegion().getLeft();}

				
				List<Block> _allBlocksList = Arrays.asList(_allBlocks);
				ArrayList<Block> sorted_allBlocksList = new ArrayList<Block>(_allBlocksList);
				Collections.sort(sorted_allBlocksList, Comparator.comparing(s -> lefts[_allBlocksList.indexOf(s)]));
				
				// Déclarer les listes.
				List<Block>		selBlocks = new ArrayList<Block> ();
				List<Integer>			selAlignements = new ArrayList<Integer> ();
				List<Integer>			selDistances = new ArrayList<Integer> ();
				List<Integer>			selShadows = new ArrayList<Integer> ();
				// Commencer le filtrage.
				Region			region = block.getRegion();
				int					regionMiddle = (region.getTop() + region.getBottom()) / 2;
				int					align;

				Block			_block;
				Region			_region, __region;
				int					_regionMiddle;
				int					_align;

				int					blockIndex = -1;
				int					x, y, dist;
				int					shadow;
				int					__top, __bottom;

				for (int i = 0; i < _allBlocks.length; i ++)
				{
					 _block = _allBlocks [i];
					_region = _block.getRegion();
					// Bloc référence trouvé.
					if (block == _block)
						blockIndex = i;
					// Sélection zone droite.
					else if (!region.includes (_region) && _region.getLeft() >= region.getRight())
					{
						_regionMiddle = (_region.getTop() + _region.getBottom()) / 2;
						// Calculer l'alignement.
						_align = _regionMiddle - regionMiddle;
						align = (int)(alignRate * (_region.getHeight() > region.getHeight() ? _region.getHeight() : region.getHeight()));

						if (alignRate < 0 || Math.abs (_align) <= align)
						{
							selBlocks.add (_block);
							selAlignements.add (_align);
							// Calculer la distance.
							x = (region.getLeft() + region.getRight()) / 2 - (_region.getLeft() + _region.getRight()) / 2;
							y = _align;
							dist = (int)Math.sqrt (x * x + y * y);
							selDistances.add (dist);
							// Calculer l'ombre.
							shadow = 0;

							for (int j = blockIndex + 1; j < i; j ++)
							{
								__region = _allBlocks [j].getRegion();

								if (__region.getLeft() > region.getRight() &&
									__region.getRight() < _region.getLeft() &&
									__region.getBottom() >= _region.getTop() &&
									__region.getTop() <= _region.getBottom())
								{
									// Top.
									__top = __region.getTop();

									if (__top < _region.getTop())
										__top = _region.getTop();
									// Bottom.
									__bottom = __region.getBottom();

									if (__bottom > _region.getBottom())
										__bottom = _region.getBottom();
									// Ombre.
									shadow += __bottom - __top;
								}
							}

							selShadows.add (shadow);
						}
					}
				}

				alignements = (_Out<Integer>[]) selAlignements.toArray ();
				distances = (_Out<Integer>[]) selDistances.toArray ();
				shadows = (_Out<Integer>[]) selShadows.toArray ();

				return (Block[]) selBlocks.toArray ();
			}
			/// <summary>
			/// Retourne les blocs à droite d'un bloc donné en utilisant
			/// un taux d'alignement <paramref name="alignRate" />.
			/// </summary>
			/// <param name="block">Bloc référence.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignements">Tableau qui contient les alignements des blocs par rapport au bloc référence.</param>
			/// <param name="distances">Tableau qui contient les distances des blocs par rapport au bloc référence.</param>
			/// <param name="shadows">Tableau qui contient les ombres des blocs par rapport au bloc référence.</param>
			/// <returns>Liste des blocs qui ont été trouvés.</returns>
			public static Block [] GetRightBlocks (Block block, Block [] allBlocks, _Out<Integer>[] alignements, _Out<Integer>[] distances, _Out<Integer>[] shadows)
			{return GeometryHelper.GetRightBlocks (block, GeometryHelper.HALIGN_RATE, allBlocks,  alignements,  distances,  shadows);}
			/// <summary>
			/// Retourne les blocs de bas d'un bloc donné en utilisant
			/// un taux d'alignement <paramref name="alignRate" />.
			/// </summary>
			/// <param name="block">Bloc référence.</param>
			/// <param name="alignRate">Taux de l'alignement. Une valeur négative indique qu'il n'y a pas d'alignement.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignements">Tableau qui contient les alignements des blocs par rapport au bloc référence.</param>
			/// <param name="distances">Tableau qui contient les distances des blocs par rapport au bloc référence.</param>
			/// <param name="shadows">Tableau qui contient les ombres des blocs par rapport au bloc référence.</param>
			/// <returns>Liste des blocs qui ont été trouvés.</returns>
			@SuppressWarnings("unchecked")
			public static Block [] GetDownBlocks (Block block, float alignRate, Block [] allBlocks,_Out<Integer>[] alignements,_Out<Integer>[] distances, _Out<Integer>[]shadows)
			{
				// Trier les blocs.
				Block []			_allBlocks = new Block [allBlocks.length + 1];
				_allBlocks= Arrays.copyOfRange(allBlocks, 0, allBlocks.length);
				_allBlocks [_allBlocks.length - 1] = block;

				int []				tops = new int [_allBlocks.length];

				for (int i = 0; i < _allBlocks.length; i ++)
				{tops [i] = _allBlocks [i].getRegion().getTop();}

				
				List<Block> _allBlocksList = Arrays.asList(_allBlocks);
				ArrayList<Block> sorted_allBlocksList = new ArrayList<Block>(_allBlocksList);
				Collections.sort(sorted_allBlocksList, Comparator.comparing(s -> tops[_allBlocksList.indexOf(s)]));
				
				// Déclarer les listes.
				List<Block>		selBlocks = new ArrayList<Block> ();
				List<Integer>			selAlignements = new ArrayList<Integer> ();
				List<Integer>			selDistances = new ArrayList<Integer> ();
				List<Integer>			selShadows = new ArrayList<Integer> ();
				// Commencer le filtrage.
				Region			region = block.getRegion();
				int					regionMiddle = (region.getRight() + region.getLeft()) / 2;
				int					align;

				Block			_block;
				Region			_region, __region;
				int					_regionMiddle;
				int					_align;

				int					blockIndex = -1;
				int					x, y, dist;
				int					shadow;
				int					__left, __right;

				for (int i = 0; i < _allBlocks.length; i ++)
				{
					 _block = _allBlocks [i];
					_region = _block.getRegion();
					// Bloc référence trouvé.
					if (block == _block)
						blockIndex = i;
					// Sélection zone droite.
					else if (!region.includes (_region) && _region.getTop() >= region.getBottom())
					{
						_regionMiddle = (_region.getRight() + _region.getLeft()) / 2;
						// Calculer l'alignement.
						_align = _regionMiddle - regionMiddle;
						align = (int)(alignRate * (_region.getWidth() > region.getWidth() ? _region.getWidth() : region.getWidth()));

						if (alignRate < 0 || Math.abs (_align) <= align)
						{
							selBlocks.add (_block);
							selAlignements.add (_align);
							// Calculer la distance.
							x = _align;
							y = (region.getTop() + region.getBottom()) / 2 - (_region.getTop() + _region.getBottom()) / 2;
							dist = (int)Math.sqrt (x * x + y * y);
							selDistances.add (dist);
							// Calculer l'ombre.
							shadow = 0;

							for (int j = blockIndex + 1; j < i; j ++)
							{
								__region = _allBlocks [j].getRegion();

								if (__region.getTop() > region.getBottom() &&
									__region.getBottom() < _region.getTop() &&
									__region.getRight() >= _region.getLeft() &&
									__region.getLeft() <= _region.getRight())
								{
									// Left.
									__left = __region.getLeft();

									if (__left < _region.getLeft())
										__left = _region.getLeft();
									// Right.
									__right = __region.getRight();

									if (__right > _region.getRight())
										__right = _region.getRight();
									// Ombre.
									shadow += __right - __left;
								}
							}

							selShadows.add (shadow);
						}
					}
				}

				alignements = (_Out<Integer>[]) selAlignements.toArray ();
				distances = (_Out<Integer>[]) selDistances.toArray ();
				shadows = (_Out<Integer>[]) selShadows.toArray ();

				return (Block[]) selBlocks.toArray ();
			}
			
			/// <summary>
			/// Retourne les blocs de bas d'un bloc donné en utilisant
			/// un taux d'alignement <paramref name="alignRate" />.
			/// </summary>
			/// <param name="block">Bloc référence.</param>
			/// <param name="allBlocks">Liste de tous les blocs.</param>
			/// <param name="alignements">Tableau qui contient les alignements des blocs par rapport au bloc référence.</param>
			/// <param name="distances">Tableau qui contient les distances des blocs par rapport au bloc référence.</param>
			/// <param name="shadows">Tableau qui contient les ombres des blocs par rapport au bloc référence.</param>
			/// <returns>Liste des blocs qui ont été trouvés.</returns>
			public static Block [] GetDownBlocks (Block block, Block [] allBlocks, _Out<Integer>[] alignements, _Out<Integer>[] distances, _Out<Integer>[] shadows)
			{return GetDownBlocks (block, GeometryHelper.VALIGN_RATE, allBlocks,  alignements,  distances,  shadows);}}
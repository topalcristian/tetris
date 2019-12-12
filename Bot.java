import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

public class Bot extends HeapSort {
	
	// Board location
	public static int locW;
	public static int locH;
	
	// Data of current Piece used
	public static int pentID;
	public static int mutation;
	public static int[][] piece;
	
	// Board Sizes
	public static int width = 5;
	public static int height = 15;
	
	// Current Score
	public static int score = 0;
	
	// Field 1 to Save current State
	public static int[][] field = new int[height][width];
	// Field 2 to save colors
	public static int[][] field2 = new int[height][width];
	
	// Size of Selection Method
	public static int tournamentSize = 100;
	
	// How many Candidates are from new Population
	public static int newCandidatesLength = 300;
	
	// How many Candidates are from the old Population
	public static int oldCandidatesLength = 560;
	
	// Next Piece Data
	public static int NpentID;
	public static int Nmutation;
	public static int[][] Npiece;
	
	// Display Progres - true
	public static boolean yesUi = true;
	public static boolean ordered = false;
	
	// Shows UI
	public static UI ui;
	
	
	static int orderI = 0;
	static boolean end = false;
	// public static int ordered = 0;
	public static ArrayList<Integer> order = new ArrayList<Integer>();
	public static ArrayList<Integer> finOrder = new ArrayList<Integer>();


	
	public static void main(String[] args) {
		// Displays UI only if set true
		if (yesUi)
			ui = new UI(height, width, 30);
		
		// Initializes population from file
		int popSize = 860;
		Individual[] population = new Individual[popSize];
		File tmpDir = new File("saver.txt");
		boolean exists = tmpDir.exists();
		if (exists) {
			String[] tr = new String[6];
			BufferedReader reader;

			try {
				reader = new BufferedReader(new FileReader("saver.txt"));
				String line = reader.readLine();
				while (line != null) {
					tr = line.split(" ");
					double[] weights = new double[4];
					weights[0] = Double.parseDouble(tr[1]);
					weights[1] = Double.parseDouble(tr[2]);
					weights[2] = Double.parseDouble(tr[3]);
					weights[3] = Double.parseDouble(tr[4]);

					population[Integer.parseInt(tr[0])] = new Individual(weights);
					;
					population[Integer.parseInt(tr[0])].fitness = Double.parseDouble(tr[5]);
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			// initializing population randomly
			for (int i = 0; i < popSize; i++) {
				double[] weights = new double[4];
				for (int j = 0; j < weights.length; j++) {
					weights[j] = Math.abs((Math.random() - 0.5));
				}/*
				weights[0] = 0.510066;
				weights[1] = 0.760666;
				weights[2] = 0.35663;
				weights[3] = 0.184483;*/
				
				
				population[i] = normalize(new Individual(weights));
			}
		}

		
		// Generation Initializing
		int generation = 0;

		while (population[0].getFitness() != 18) {
			
			// Next generation
			generation++;
			
			// Create New Population out of the old one by crossover / mutation
			population = deleteNLastReplacement(population, mutatePopulation(crossover(population)));
			
			// Generates Fitness by playing game
			fitnessGenerator(population);
			
			// Shows the best fitness in each generation
			System.out.println("Generation " + generation + "\n Best Individual Fitness = " + population[0].getFitness());
			
			
			
		}
	}

	
	// Combines part of old population with new
	private static Individual[] deleteNLastReplacement(Individual[] candidates, Individual[] newCandidates) {
		for (int i = 0; i < newCandidates.length; i++) {
			candidates[oldCandidatesLength + i] = newCandidates[i];
		}
		sort(candidates);
		return candidates;
	}

	// Calculates current number of holes in the field
	private static int calculateHoles(int[][] fieldCalc) {
		boolean check = false;
		int holes = 0;
		for (int j = 0; j < fieldCalc[0].length; j++) {
			for (int i = 0; i < fieldCalc.length; i++) {
				if (fieldCalc[i][j] != -1) {
					check = true;
				}
				if (fieldCalc[i][j] == -1 && check)
					holes++;

			}
			check = false;
		}

		return holes;
	}

	// Calculates current number of completed lines in the field
	private static int calculateLines(int[][] fieldCalc) {
		int lines = 0;
		boolean lineIsFull = true;
		for (int i = 0; i < fieldCalc.length; i++) {
			lineIsFull = true;
			for (int j = 0; j < fieldCalc[0].length; j++) {
				if (fieldCalc[i][j] == -1) {
					lineIsFull = false;
				}
			}
			if (lineIsFull == true) {
				lines += 1;
			}
		}
		return lines;
	}

	// Calculates current number of squares above pentominoes 
	private static int calculateHeight(int[][] fieldCalc) {
		int h = 0;
		for (int j = 0; j < fieldCalc[0].length; j++) {
			for (int i = 0; i < fieldCalc.length; i++) {
				if (fieldCalc[i][j] == -1)
					h++;
				else
					i = fieldCalc.length;
			}

		}
		return (fieldCalc.length * 5) - h;
	}

	// Calculates height diference between lines
	private static int calculateBumpiness(int[][] fieldCalc) {
		int[] heights = new int[fieldCalc[0].length];

		int h = 0;
		for (int j = 0; j < fieldCalc[0].length; j++) {
			for (int i = 0; i < fieldCalc.length; i++) {
				if (fieldCalc[i][j] == -1)
					h++;
				else {
					i = fieldCalc.length;
					heights[j] = fieldCalc.length - h;
					h = 0;
				}

			}
		}
		int result = 0;
		for (int i = 1; i < heights.length; i++) {
			result += Math.abs(heights[i] - heights[i - 1]);
		}
		return result;

	}

	// Generates fitness and the sorts
	private static void fitnessGenerator(Individual[] individual) {

		for (int i = 0; i < individual.length; i++) {
			individual[i].setFitness(play(individual[i].chromosome));
			if (individual[i].getFitness() != 18) {
				finOrder.clear();
			} else {
				for (int z = 0; z < finOrder.size(); z++) {
					System.out.println(finOrder.get(z) + " ");
				}
				finOrder.clear();
			}
		}
		sort(individual);
		
	}

	// Starts the game with weights given
	
	static int play(double[] chromosome) {
		
		// Resets Score/Fitness
		int score2 = 0;
		
		// Generates empty field
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = -1;
			}
		}
		order.add(0);
		order.add(1);
		order.add(2);
		order.add(3);
		order.add(4);
		order.add(5);
		order.add(6);
		order.add(7);
		order.add(8);
		order.add(9);
		order.add(10);
		order.add(11);
		order.add(12);
		order.add(13);
		order.add(14);
		order.add(15);
		order.add(16);
		order.add(17);
		
		
		Random r = new Random();
		orderI = r.nextInt(order.size());
		//TODO
		//Generates current next Piece
		NpentID = order.get(orderI);
		finOrder.add(order.get(orderI));
		if (ordered)
			order.remove(orderI);
		Nmutation = 0;
		Npiece = PentominoDatabase.data[NpentID][Nmutation];

		// Calls Next Piece
		nextPiece();

		// Tries to fit the new piece in best position
		while (fitInMove(0, 0) && !end) {
			field = best(allOutcomesPossible(pentID), chromosome);
			if (yesUi) {
				ui.setState(field, field2);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			score2 += checkDelRows();
			if (yesUi) {
				ui.giveScore(score2);
				ui.setState(field, field2);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			nextPiece();
		}
		
		
		end = false;
		order.clear();
		return score2;
	}

	// Method to get the best possible move in current board position
	public static int[][] best(int[][][][] fields, double[] chromosome) {
		double score1 = 0;
		int z = 0;
		int x = 0;
		// TODO

		/*
		 * for(int j = 0; j < fields.length; j++) { for(int i = 0; i < fields[0].length;
		 * i++) { for(int jj = 0; jj < fields[0][0].length; jj++) { for(int ii = 0; ii <
		 * fields[0][0][0].length; ii++) { System.out.print(fields[j][i][jj][ii] + " ");
		 * } System.out.println(); } System.out.println(); System.out.println(); double
		 * score2 = -calculateHeight(fields[j][i])*chromosome[0] -
		 * calculateHoles(fields[j][i])*chromosome[1] +
		 * calculateLines(fields[j][i])*chromosome[2] -
		 * calculateBumpiness(fields[j][i])*chromosome[3]; System.out.println(score2);
		 * System.out.println(calculateHeight(fields[j][i])+" "+calculateHoles(fields[j]
		 * [i])+" "+calculateLines(fields[j][i])+" "+calculateBumpiness(fields[j][i]) );
		 * } }
		 */
		//Initializes first score of move
		double score2 = -calculateHeight(fields[0][0]) * chromosome[0] - calculateHoles(fields[0][0]) * chromosome[1]
				+ calculateLines(fields[0][0]) * chromosome[2] - calculateBumpiness(fields[0][0]) * chromosome[3];
		
		// Checks which move is better
		for (int j = 0; j < fields.length; j++) {
			for (int i = 1; i < fields[0].length; i++) {
				score1 = -calculateHeight(fields[j][i]) * chromosome[0] - calculateHoles(fields[j][i]) * chromosome[1]
						+ calculateLines(fields[j][i]) * chromosome[2]
						- calculateBumpiness(fields[j][i]) * chromosome[3];
				if (score1 > score2) {
					score2 = score1;
					z = i;
					x = j;
				}
			}
		}

		return fields[x][z];
	}

	// Returns All Outcomes possible with current board
	private static int[][][][] allOutcomesPossible(int ID) {

		int[][] field3 = new int[field.length][field[0].length];
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[0].length; j++) {
				field3[i][j] = field[i][j];
			}
		}

		int[][][][] outcome = new int[PentominoDatabase.data[ID].length][width][height][width];
		for (int j = 0; j < outcome.length; j++) {
			for (int i = 0; i < outcome[0].length; i++) {
				mutation = j;
				piece = PentominoDatabase.data[ID][mutation];
				locW = i;
				locH = 0;
				if (fitInMove(0, 0)) {
					while (fitInMove(0, 1)) {
						locH++;
					}
					addPiece();
					locH = 0;

					for (int ii = 0; ii < field.length; ii++) {
						for (int jj = 0; jj < field[0].length; jj++) {
							outcome[j][i][ii][jj] = field[ii][jj];
						}
					}

					for (int ii = 0; ii < field.length; ii++) {
						for (int jj = 0; jj < field[0].length; jj++) {
							field[ii][jj] = field3[ii][jj];
						}
					}

				} else {

					for (int ii = 0; ii < field.length; ii++) {
						for (int jj = 0; jj < field[0].length; jj++) {
							outcome[j][i][ii][jj] = outcome[0][0][ii][jj];

						}
					}
				}
			}
		}
		return outcome;
	}

	// Checks if the Pentomino fits from the selected start point
	public static boolean fitInMove(int x, int y) {
		int[][] pieceTemp = PentominoDatabase.data[pentID][mutation];

		for (int i = 0; i < pieceTemp.length; i++) {
			for (int j = 0; j < pieceTemp[0].length; j++) {
				if (pieceTemp[i][j] == 0) {
					continue;
				}
				int cx = i + locH + y;
				int cy = j + locW + x;
				if (cx < 0 || cy < 0 || cx >= field.length || cy >= field[0].length) {
					return false;
				}
				if (field[cx][cy] > -1) {
					return false;
				}
			}
		}
		return true;
	}

	// Checks if there are rows complete
	public static int checkDelRows() {
		boolean lineIsFull = true;
		int score2 = 0;
		for (int i = 0; i < height; i++) {
			lineIsFull = true;
			for (int j = 0; j < width; j++) {
				if (field[i][j] == -1) {
					lineIsFull = false;
				}
			}
			if (lineIsFull == true) {
				moveOneRow(i);
				score2 += 1;
			}
		}
		return score2;
	}

	// Moves the field by one row if there are rows to be removed
	public static void moveOneRow(int row) {
		for (int j = row - 1; j > 0; j--) {
			for (int i = 0; i < width; i++) {
				field[j + 1][i] = field[j][i];
				field2[j + 1][i] = field2[j][i];
			}
		}
	}

	// Generates the Next Piece
	public static void nextPiece() {

		pentID = NpentID;
		mutation = Nmutation;
		piece = Npiece;
		Random r = new Random();
		if (!order.isEmpty()) {
			orderI = r.nextInt(order.size());
			NpentID = order.get(orderI);
			finOrder.add(order.get(orderI));
			if (ordered)
				order.remove(orderI);
			Nmutation = 0;
			Npiece = PentominoDatabase.data[NpentID][Nmutation];
			if (yesUi)
				ui.giveNPiece(NpentID, Npiece);
			locW = 0;
			locH = 0;
		} else {
			end = true;
			order.clear();
		}
	}

	// Adds the piece to the table
	public static void addPiece() {

		for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
		{
			for (int j = 0; j < piece[0].length; j++) // loop over y position of pentomino
			{
				if (piece[i][j] == 0) {
					continue;
				}
				int cx = i + locH;
				int cy = j + locW;
				field[cx][cy] = pentID;
			}
		}
	}

	// Mutates the population with the Mutation Rate 20%
	private static Individual[] mutatePopulation(Individual[] population) {
		double mutationRate = 0.2;

		Random weightChromosome = new Random(System.currentTimeMillis());

		for (int i = 0; i < population.length; i++) {
			double roll = Math.random();
			if (roll <= mutationRate) {
				population[i].chromosome[weightChromosome.nextInt(4)] += (Math.random() * 0.4 - 0.2);
			}
		}
		fitnessGenerator(population);

		return population;
	}

	// Crossovers the Population via tournament selection
	private static Individual[] crossover(Individual[] parents) {

		Individual[] child = new Individual[newCandidatesLength];

		for (int i = 0; i < child.length; i++) {
			Individual parent1 = selectionMethod(parents)[0];
			Individual parent2 = selectionMethod(parents)[0];

			child[i] = crossoverMethod(parent1, parent2);
		}

		return child;
	}

	// Saves population to file with weights
	private static void show(Individual[] parent) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("saver.txt", "UTF-8");
			for (int i = 0; i < parent.length; i++) {
				writer.println(i + " " + parent[i].genoToPhenotype() + parent[i].getFitness());
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	// Tournament Selection
	private static Individual[] selectionMethod(Individual[] select) {

		Individual[] tournamentPopulation = new Individual[tournamentSize];

		for (int i = 0; i < tournamentSize; i++) {
			tournamentPopulation[i] = select[(int) (Math.random() * select.length)];
		}
		sort(tournamentPopulation);
		return tournamentPopulation;
	}

	// Crossover of two parents
	private static Individual crossoverMethod(Individual parent1, Individual parent2) {
		double[] chrom1 = new double[4];
		Individual child = new Individual(chrom1);

		for (int i = 0; i < parent1.getChromosome().length; i++) {
			child.chromosome[i] = parent1.getFitness() * parent1.chromosome[i]
					+ parent2.getFitness() * parent2.chromosome[i];

		}

		return normalize(child);
	}

	// Normalizes the population
	private static Individual normalize(Individual candidate) {
		double norm = Math.sqrt(candidate.chromosome[0] * candidate.chromosome[0]
				+ candidate.chromosome[1] * candidate.chromosome[1] + candidate.chromosome[2] * candidate.chromosome[2]
				+ candidate.chromosome[3] * candidate.chromosome[3]);
		candidate.chromosome[0] /= norm;
		candidate.chromosome[1] /= norm;
		candidate.chromosome[2] /= norm;
		candidate.chromosome[3] /= norm;

		return candidate;
	}

}
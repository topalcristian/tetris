
import java.util.Random;

public class Bot extends HeapSort {
	public static int locW;
	public static int locH;
	public static int pentID;
	public static int mutation;
	public static int[][] piece;
	public static int width = 5;
	public static int height = 15;
	public static int score = 0;
	public static int[][] field = new int[height][width];
	public static int[][] field2 = new int[height][width];
	
	//public static UI ui = new UI(height, width, 50);
	
	public static void main(String[] args) {

		int popSize = 500;
		
		Individual[] population = new Individual[popSize];
		
		//initializing world
		for (int i = 0; i < popSize; i++) {
			double[] weights = new double[4];
	        for (int j = 0; j < weights.length; j++){
	            weights[j] = (Math.random()*1);
	        }
			population[i] = new Individual(weights);
		}

		
		int generation = 0;

		while(population[0].getFitness() != 5) {
			
			fitnessGenerator(population);
			generation++;
			population = mutatePopulation(crossover(population));
			System.out.println("Generation "+ generation + "\n Best Individual Fitness = " + population[0].getFitness());
			if(population[0].getFitness() != 5)
				show(population);
			else
				System.out.println("Individual " + 1 + " : " + population[0].genoToPhenotype() + "\n" + " Fitness : " +  population[0].getFitness());
		}
	}
	
	//To think 
	private static int calculateHeight(int[][] fieldCalc){
		int h = 0;
		for(int j = 0; j < fieldCalc[0].length; j++) {
		for(int i = fieldCalc.length-1; i > 0 ; i--) {
			if (fieldCalc[i][j] == -1) h++;
			else i = 0;
		}
		
	}
		return fieldCalc.length - h;
	}
	private static int calculateHoles(int[][] fieldCalc){
		int holes = 0;
		for(int j = 0; j < fieldCalc[0].length; j++) {
		for(int i = fieldCalc.length - 1; i > 0 ; i--) {
			if (fieldCalc[i][j] != -1) 
				for (int x = i; i > 0 ; i--) {
					if (fieldCalc[i][j] == -1) holes++; 
				}
		}
		
	}
		return holes;
	}
	private static int calculateLines(int[][] fieldCalc){
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
	private static int calculateBumpiness(int[][] fieldCalc){
		int[] heights = new int[fieldCalc[0].length];
		int h = 0;
		for(int j = 0; j < fieldCalc[0].length; j++) {
			for(int i = fieldCalc.length -1; i > 0 ; i--) {
				if (fieldCalc[i][j] == -1) h++;
				else {
					i = 0;
					heights[j]=fieldCalc.length - h;
					}
			
			}
		}
		int result = 0;
		for(int i = 1; i < heights.length; i++) {
			result +=  Math. abs(heights[i] - heights[i-1]);
		}
		return result;
		
	}
	private static void fitnessGenerator(Individual[] individual){

		for (int i = 0; i < individual.length;i++) {
			individual[i].setFitness(play(individual[i].chromosome));
		}
		sort(individual);
	}

	private static int play(double[] chromosome) {
		int score = 0;
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = -1;
			}
		}
		nextPiece();
		while (fitInMove(0,0)) {
			
		field = best(allOutcomesPossible(pentID, field), chromosome);
		//addPiece();
		//ui.setState(field, field2);
		onTable();
		checkDelRows();
		nextPiece();
		}
		return score;
	}
	
	public static int[][] best(int[][][][] fields, double[] chromosome) {
		double score1 = 0;
		int z = 0;
		int x = 0;
		double score2 = calculateHeight(fields[0][0])*chromosome[0] + calculateHoles(fields[0][0])*chromosome[1] + calculateLines(fields[0][0])*chromosome[2] + calculateBumpiness(fields[0][0])*chromosome[3]; 
		for(int j = 0; j < fields.length; j++) {
		for(int i = 1; i < fields[0].length; i++) {
			score1 = calculateHeight(fields[j][i])*chromosome[0] + calculateHoles(fields[j][i])*chromosome[1] + calculateLines(fields[j][i])*chromosome[2] + calculateBumpiness(fields[j][i])*chromosome[3]; 
			if (score1 < score2) {score2 = score1; z = i;x = j;}
		}
		}

		
		return fields[x][z];
	}
	
	
	private static int[][][][] allOutcomesPossible(int ID, int[][] field){
		int[][] field2 = field;
		int[][][][] outcome = new int[PentominoDatabase.data[ID].length][width][height][width];
		for (int j = 0; j < outcome.length; j++) {
		for (int i = 0; i < outcome[0].length; i++) {
			locW=i;
			if(fitInMove(0,0)) {
			while(fitInMove(0,1)) {
				locH++;
			}
			addPiece();
			outcome[j][i] = field;
			field = field2;
			}
		}

		}
		return outcome;
	}
	
	

	public static void onTable() {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				if (field[i][j] == pentID) {
					field2[i][j] = pentID;
					field[i][j] = 18;
					}
			}
		}
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
				if (field[cx][cy] > -1 && field[cx][cy] != pentID) {
					return false;
				}
			}
		}
		return true;
	}

	public static void checkDelRows() {
		boolean lineIsFull = true;
		for (int i = 0; i < height; i++) {
			lineIsFull = true;
			for (int j = 0; j < width; j++) {
				if (field[i][j] == -1) {
					lineIsFull = false;
				}
			}
			if (lineIsFull == true) {
				moveOneRow(i);
				score += 1;
			}
		}
	}

	public static void reDraw() {
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[0].length; j++) {
				if (field[i][j] == pentID) {
 					field[i][j] = -1;
				}

			}}

	}

	public static void moveOneRow(int row) {
		for (int j = row - 1; j > 0; j--) {
			for (int i = 0; i < width; i++) {
				field[j + 1][i] = field[j][i];
				field2[j + 1][i] = field2[j][i];
			}
		}
	}

	public static void nextPiece() {

		Random r = new Random();
		pentID = r.nextInt(PentominoDatabase.data.length);
		mutation = r.nextInt(PentominoDatabase.data[pentID].length);
		piece = PentominoDatabase.data[pentID][mutation];
		locW = 0;
		locH = 0;
	}

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
	public static void removePiece() {

		for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
		{
			for (int j = 0; j < piece[0].length; j++) // loop over y position of pentomino
			{
				if (piece[i][j] == 0) {
					continue;
				}
				int cx = i + locH;
				int cy = j + locW;
				field[cx][cy] = -1;
			}
		}
	}
	


	private static Individual[] mutatePopulation(Individual[] population){
		double mutationRate = 0.1;
		double mutationRate2 = 0.17;
		Random weightChromosome = new Random(System.currentTimeMillis());
 
		for (int i = 0 ;i < population.length; i++) {
			 double roll = Math.random();
			if (roll <= mutationRate){
				 population[i].chromosome[weightChromosome.nextInt(4)] -= 0.2;
			}else 	if (roll <= mutationRate2){
				 population[i].chromosome[weightChromosome.nextInt(4)] += 0.2;
			}
		}
		fitnessGenerator(population);

		return population;
	 }

	private static Individual[] crossover(Individual[] parents) {

		Individual[] child = new Individual[parents.length];

		for(int i = 0; i < parents.length; i++)
		{
			Individual parent1 = selectionMethod(parents)[0];
			Individual parent2 = selectionMethod(parents)[0];

           child[i] = crossoverMethod(parent1,parent2);
		}

    return child;
	}

	private static void show(Individual[] parent){
		for (int i = 0; i < parent.length; i++) {
			System.out.println("Individual " + i + " : " + parent[i].genoToPhenotype() + "\n" + " Fitness : " + parent[i].getFitness());
		}
	}

	private static Individual[] selectionMethod(Individual[] select){
		int tournamentSize = 21;
		Individual[] tournamentPopulation = new Individual[tournamentSize];

		for(int i = 0; i < tournamentSize; i++){
			tournamentPopulation[i] = select[(int) (Math.random() * select.length)];
		}
		sort(tournamentPopulation);
		return tournamentPopulation;
	}

	private static Individual crossoverMethod(Individual parent1, Individual parent2) {
		double [] chrom1 = new double[4];
		Individual child = new Individual(chrom1);

		for (int i = 0; i < parent1.getChromosome().length; i++) {
			double check = Math.random();
			if (check < 0.5)
				child.chromosome[i] = parent1.chromosome[i];
			else
				child.chromosome[i] = parent2.chromosome[i];
		}
		return child;
	}

}



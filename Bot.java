
import java.util.Random;

public class Bot extends HeapSort {


	public static void main(String[] args) {
		int popSize = 500;
		

		Random generator = new Random(System.currentTimeMillis());
		Individual[] population = new Individual[popSize];
		
		//initializing word
		for (int i = 0; i < popSize; i++) {
			
			double[] weights = new double[4];
	        for (int j = 0; j < weights.length; j++){
	            weights[j] = (Math.random()*2-1);
	        }
			population[i] = new Individual(weights);
		}

		
		int generation = 0;

		while(population[0].getFitness() != 40) {
			fitnessGenerator(population);
			generation++;
			population = mutatePopulation(crossover(population));
			System.out.println("Generation "+ generation + "\n Best Individual Fitness = " + population[0].getFitness());
			if(population[0].getFitness() != 1.0)
				show(population);
			else
				System.out.println("Individual " + 1 + " : " + population[0].genoToPhenotype() + "\n" + " Fitness : " +  population[0].getFitness());
		}
	}


	private static int calculateHeight(int[][] fieldCalc){
		int height = 0;
		for(int j = 0; j < fieldCalc[0].length; j++) {

		for(int i = fieldCalc.length; i > 0 ; i--) {
			if (fieldCalc[i][j] == -1) height++;
			else i=0;
		}
		
	}
		return fieldCalc.length - height;
	}
	private static int calculateHoles(int[][] fieldCalc){
		int holes = 0;

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
		int[] heights = new int[fieldCalc[0].length-1];
		int height = 0;
		for(int j = 0; j < fieldCalc[0].length; j++) {

			for(int i = fieldCalc.length; i > 0 ; i--) {
				if (fieldCalc[i][j] == -1) height++;
				else {
					i = 0;
					heights[j]=fieldCalc.length - height;
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
		double fitness;
		double count;

		for (int i = 0; i < individual.length;i++) {
			
			individual[i].setFitness(play());
		}
		sort(individual);
	}
	
	private static int play() {
		
		int score = 0;
		return score;
	}
	
	
	
	private static int[][][] allOutcomesPossible(){
		
		return null;
	}

	
	

	private static Individual[] mutatePopulation(Individual[] population){
		double mutationRate = 0.07;
		Random weightChromosome = new Random(System.currentTimeMillis());
 
		for (int i = 0 ;i < population.length; i++) {
			 double roll = Math.random();
			if (roll <= mutationRate){
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



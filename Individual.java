
public class Individual {
	
	double[] chromosome;
	double fitness;
	
	public Individual(double[] chromosome) {
		this.chromosome = chromosome;
		this.fitness = 0;
	}


	public double[] getChromosome() {

		return chromosome;
	}

	public void setChromosome(double[] chromosome) {
		this.chromosome = chromosome;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public String genoToPhenotype() {
		StringBuilder builder = new StringBuilder();
		
		for (int j = 0; j < chromosome.length; j++){
		builder.append(chromosome[j]);
		builder.append(" ");
		}
		return builder.toString();
	}



	



}

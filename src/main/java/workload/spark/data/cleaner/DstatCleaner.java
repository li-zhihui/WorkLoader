package workload.spark.data.cleaner;

/**
 * Sum dstat.csv data from all slave nodes. <br>
 * Format data's unit(G memory, MB disk & network)
 * 
 */
public class DstatCleaner extends Cleaner {

	public DstatCleaner(Cleaner cleaner) {
		cleaner.clean();
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub

	}

}

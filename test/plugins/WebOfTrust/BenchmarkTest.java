package plugins.WebOfTrust;

import java.net.MalformedURLException;
import java.util.ArrayList;

import freenet.keys.FreenetURI;

import plugins.WebOfTrust.exceptions.InvalidParameterException;
import plugins.WebOfTrust.exceptions.NotTrustedException;


/**
 * This is NOT an actual unit test. It is a set of benchmarks to measure the performance of WOT.
 * 
 * Also, this is NOT run in the default test suite which is run by Ant when building.
 * To run it, put "benchmark=true" into the "override.properties" build confiugration file. If it does not exist, create it in the root of the project.
 * 
 * @author xor (xor@freenetproject.org)
 */
public class BenchmarkTest extends DatabaseBasedTest {

	/**
	 * Benchmarks {@link WebOfTrust.verifyAndCorrectStoredScores}.
	 * This function is a glue wrapper around the actual function which we benchmark: {@link WebOfTrust.computeAllScoresWithoutCommit}.
	 * It currently seems to be the major bottleneck in WOT: As of build0012, it takes ~100 seconds for the existing on-network identities.
	 */
	public void test_BenchmarkVerifyAndCorrectStoredScores() throws MalformedURLException, InvalidParameterException {		
		// Benchmark parameters...
		
		int identityCount = 100;
		int trustCount = (identityCount*identityCount) / 5; // A complete graph would be identityCount² trust values.
		int iterations = 100;
		
		// Random trust graph setup...
	
		ArrayList<Identity> identities = addRandomIdentities(identityCount);
		
		// At least one own identity needs to exist to ensure that scores are computed.
		identities.add(mWoT.createOwnIdentity(getRandomSSKPair()[0], "Test", true, "Test")); 
		
		addRandomTrustValues(identities, trustCount);
		
		// The actual benchmark
		
		long totalTime = 0;
		for(int i=0; i < iterations; ++i) {
			flushCaches();
			
			long startTime = System.nanoTime();
			mWoT.verifyAndCorrectStoredScores();
			long endTime = System.nanoTime();
			
			totalTime += endTime-startTime;
		}
		
		double seconds = (double)totalTime/(1000*1000*1000); 
		
		System.out.println("Benchmarked " + iterations + " iterations of verifyAndCorrectStoredScores: " + (seconds/iterations) + " seconds/iteration");
	}
}

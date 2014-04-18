/**
 *
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

/**
 * @author Nitin 
 * This class is responsible for assigning a partition to a given term. 
 * The static methods imply that all instances of this class should behave exactly the same. 
 * Given a term, irrespective of what instance is
 * called, the same partition number should be assigned to it.
 */
public class Partitioner {

    /**
     * Method to get the total number of partitions THis is a pure design choice
     * on how many partitions you need and also how they are assigned.
     *
     * @return: Total number of partitions
     */
    public static int getNumPartitions() {
        //TODO: Implement this method
        return 4;
    }

    /**
     * Method to fetch the partition number for the given term. The partition
     * numbers should be assigned from 0 to N-1 where N is the total number of
     * partitions.
     *
     * @param term: The term to be looked up
     * @return The assigned partition number for the given term
     */
    public static int getPartitionNumber(String term) {
        //TDOD: Implement this method
        char c = term.charAt(0);
        int i32Partition = 0;

        switch (c) {
            case 'a':
            case 'b':

                i32Partition = 1;

                break;
            case 'y':
            case 'z':
            case 'c':
            case 'd':
            case 'v':
            case 'x':
                i32Partition = 0;
                break;

            case 'e':
            case 'f':
            case 'o':
            case 'p':
            //i32Partition = 0;
            case 'g':
            case 'h':
            case 'm':
            case 'n':
                i32Partition = 2;
                break;
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            // i32Partition = 5;
            //break;

            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
                i32Partition = 3;
                break;
        }
        return i32Partition;
    }
}

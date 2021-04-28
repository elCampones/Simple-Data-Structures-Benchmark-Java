import java.util.*;
import java.util.function.Consumer;

/**
 * @author Pedro Campones
 */
public class DataStructuresBenchmark {

    /*
    * Elements to be placed in the collections during the benchmark phase
    */
    private static final int LOT_OF_STUFF = 3000000;

    /*
    * Number of random searches to be performed when using lists in the benchmark
    */
    private static final int SMALL_ELEMENTS_TO_FIND = 50;

    /*
    * Number of random searches to be performed when using only sets in the benchmark
    */
    private static final int LARGE_ELEMENTS_TO_FIND = 1000000;

    /*
    * Number of elements to be removed at the start of a collection
    */
    private static final int TO_BE_REMOVED_BEGINNING = 1000;

    /*
    * Number of elements to be removed at the end of a list
    */
    private static final int TO_BE_REMOVED_ENDING = 100000;

    public static void main(String[] argv) {
        createDataStructures();
        benchmark();
    }

    /*
    * Code excerpt demonstrating how to create the data structures used in POO.
    * Code also demonstrates a simple use of some operations.
    */
    private static void createDataStructures() {

        /*********Collections*********/
        System.out.println("--------------Array List Operations----------------");
        List<String> arrayListStr = new ArrayList<>();      //Size 10 by default
        addElementsToCollection(arrayListStr);
        iterateElementsInCollection(arrayListStr);

        System.out.println("--------------Linked List Operations----------------");
        List<String> linkedListStr = new LinkedList<>();    //Size unbounded
        addElementsToCollection(linkedListStr);
        iterateElementsInCollection(linkedListStr);

        System.out.println("--------------Hash Set Operations----------------");
        Set<String> hashSetStr = new HashSet<>();           //Size 10 by default
        addElementsToCollection(hashSetStr);
        iterateElementsInCollection(hashSetStr);            //Nao necessariamente por ordem de insersao

        System.out.println("--------------Sorted Set Operations----------------");
        SortedSet<String> sortedSetStr = new TreeSet<>();   //Size unbounded
        addElementsToCollection(sortedSetStr);
        System.out.println("Alphabetical order iteration:");
        iterateElementsInCollection(sortedSetStr);

        System.out.println("Reverse Alphabetical order iteration:");
        System.out.println("    Using Comparable:");
        SortedSet<ReverseElements<String>> reverseStringSortedSet = new TreeSet<>();
        for (String str : sortedSetStr)
            reverseStringSortedSet.add(new ReverseElements<>(str));
        for (ReverseElements<String> rev : reverseStringSortedSet)
            System.out.println(rev.elem);                    //Consegue-se aceder a variaveis privadas em classes internas

        //Alternative
        System.out.println("    Using List Iterator:");
        Iterator<String> iterator = new ReverseIterator<String>(arrayListStr.listIterator(arrayListStr.size()));
        while(iterator.hasNext())
            System.out.println(iterator.next());

        /*********Maps****************/
        System.out.println("--------------Hash Set Operations----------------");
        Map<Integer, String> mapIntToStr = new HashMap<>(); //Size 10 by default
        //addElementsToCollection(mapIntToStr);             Nao compila porque Map nao e subtipo de Collection
        addElementsToMap(mapIntToStr);
        iterateElementsInMap(mapIntToStr);                  //Nao necessariamente por ordem de insersao

        System.out.println("--------------Tree Map Operations----------------");
        SortedMap<Integer, String> sortedMapIntToString = new TreeMap<>();
        addElementsToMap(sortedMapIntToString);
        System.out.println("Alphabetical order iteration:");
        iterateElementsInMap(sortedMapIntToString);

        System.out.println("Reverse Alphabetical order iteration:");
        SortedMap<ReverseElements<Integer>, String> reverseStringSortedMap = new TreeMap<>();
        int i = 1;
        for (Map.Entry<Integer, String> entry : sortedMapIntToString.entrySet())
            reverseStringSortedMap.put(new ReverseElements<>(i++), entry.getValue());
        reverseStringSortedMap.values().forEach(System.out::println);
    }

    private static void addElementsToCollection(Collection<String> col) {
        col.add("First object in List. Heck yeah!");
        col.add("Second");
        col.add("Third");
    }

    private static void iterateElementsInCollection(Collection<String> col) {
        System.out.println("Using foreach:");
        for(String str : col)
            System.out.println(str);

        System.out.println("Using explicit Iterator:");
        Iterator<String> iterator = col.iterator();
        while (iterator.hasNext())
            System.out.println(iterator.next());
    }

    private static void addElementsToMap(Map<Integer, String> map) {
        map.put(1, "First object in List. Heck yeah!");
        map.put(2,"Second");
        map.put(3, "Third");
    }

    private static void iterateElementsInMap(Map<Integer, String> map) {
        System.out.println("Using foreach:");
        Set<Map.Entry<Integer, String>> entrySet = map.entrySet();
        for (Map.Entry<Integer, String> entry : entrySet)
            System.out.println(entry.getValue());
    }

    private static void benchmark() {

        List<String> unboundedArrayList = new ArrayList<>(); //10 ELEMS
        List<String> boundedArrayList = new ArrayList<>(LOT_OF_STUFF);
        List<String> linkedList = new LinkedList<>();
        Set<String> unboundedHashSet = new HashSet<>();
        Set<String> boundedHashSet = new HashSet<>(LOT_OF_STUFF);
        SortedSet<String> sortedSet = new TreeSet<>();


        String boundedArrayMessage = "Array List with Default Size: %dms\n";

        String unboundedArrayMessage = "Array List with Specified Size: %dms\n";

        String linkedListMessage = "Linked List: %dms\n";

        String unboundedHashSetMessage = "Hash Set with Default Size: %dms\n";

        String boundedHashSetMessage = "Hash Set with Specified Size: %dms\n";

        String sortedSetMessage = "Sorted Set: %dms\n";

        System.out.println("----------Insertions-----------");

        addALotOfStuff(unboundedArrayList, boundedArrayMessage);

        addALotOfStuff(boundedArrayList, unboundedArrayMessage);

        addALotOfStuff(linkedList, linkedListMessage);

        addALotOfStuff(unboundedHashSet, unboundedHashSetMessage);

        addALotOfStuff(boundedHashSet, boundedHashSetMessage);

        addALotOfStuff(sortedSet, sortedSetMessage);

        System.out.println("----------Convertions-----------");

        convertToArray(linkedList, "Converting from Linked List into Array List: %dms\n");

        convertToLinked(boundedArrayList, "Converting from Array List into Linked List: %dms\n");

        convertToHash(sortedSet, "Converting from Tree Set into Hash Set: %dms\n");

        convertToSorted(boundedHashSet, "Converting HashSet into Tree Set: %dms\n");

        //Conversoes de listas para sets e vice-versa tambem e possivel

        System.out.println("----------Contains (small)-----------");

        findRandomElement(unboundedArrayList, unboundedArrayMessage, SMALL_ELEMENTS_TO_FIND);

        findRandomElement(boundedArrayList, boundedArrayMessage, SMALL_ELEMENTS_TO_FIND);

        findRandomElement(linkedList, linkedListMessage, SMALL_ELEMENTS_TO_FIND);

        findRandomElement(unboundedHashSet, unboundedHashSetMessage, SMALL_ELEMENTS_TO_FIND);

        findRandomElement(boundedHashSet, boundedHashSetMessage, SMALL_ELEMENTS_TO_FIND);

        findRandomElement(sortedSet, sortedSetMessage, SMALL_ELEMENTS_TO_FIND);

        System.out.println("----------Contains (LARGE)-----------");

        findRandomElement(unboundedHashSet, unboundedHashSetMessage, LARGE_ELEMENTS_TO_FIND);

        findRandomElement(boundedHashSet, boundedHashSetMessage, LARGE_ELEMENTS_TO_FIND);

        findRandomElement(sortedSet, sortedSetMessage, LARGE_ELEMENTS_TO_FIND);

        System.out.println("----------Iterations-----------");

        iterateElements(unboundedArrayList, unboundedArrayMessage);

        iterateElements(boundedArrayList, boundedArrayMessage);

        iterateElements(linkedList, linkedListMessage);

        iterateElements(unboundedHashSet, unboundedHashSetMessage);

        iterateElements(boundedHashSet, boundedHashSetMessage);

        iterateElements(sortedSet, sortedSetMessage);

        System.out.println("----------Gets-----------");

        String elementRetrieved = "Element retrieved: %s\n";

        System.out.printf(elementRetrieved, getToPosition(unboundedArrayList, unboundedArrayMessage));

        System.out.printf(elementRetrieved, getToPosition(boundedArrayList, boundedArrayMessage));

        System.out.printf(elementRetrieved, getToPosition(linkedList, linkedListMessage));

        System.out.println("----------Removals From Beginning-----------");

        removeFirstElements(unboundedArrayList, unboundedArrayMessage);

        removeFirstElements(boundedArrayList, unboundedArrayMessage);

        removeFirstElements(linkedList, linkedListMessage);

        removeFirstElements(unboundedHashSet, unboundedHashSetMessage);

        removeFirstElements(boundedHashSet, boundedHashSetMessage);

        removeFirstElements(sortedSet, sortedSetMessage);

        System.out.println("----------Removals From End-----------");

        removeFromEnding(unboundedArrayList, unboundedArrayMessage);

        removeFromEnding(boundedArrayList, boundedArrayMessage);

        removeFromEnding(linkedList, linkedListMessage);

    }

    private static void addALotOfStuff(Collection<String> col, String message) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < LOT_OF_STUFF; i++)
            col.add("S" + i);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
    }

    private static <E> void convertToArray(Collection<E> col, String message) {
        long startTime = System.currentTimeMillis();
        new ArrayList<>(col);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
    }

    private static <E> void convertToLinked(Collection<E> col, String message) {
        long startTime = System.currentTimeMillis();
        new LinkedList<>(col);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
    }

    private static <E> void convertToHash(Collection<E> col, String message) {
        long startTime = System.currentTimeMillis();
        new HashSet<>(col);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
    }

    private static <E> void convertToSorted(Collection<E> col, String message) {
        long startTime = System.currentTimeMillis();
        new TreeSet<>(col);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
    }

    private static void findRandomElement(Collection<String> col, String message, int iterations) {
        float avgTime = 0;
        for (int i = 0; i < iterations; i++) {
            int toFind = new Random().nextInt(LOT_OF_STUFF);
            long startTime = System.currentTimeMillis();
            col.contains("S" + toFind);
            long end = System.currentTimeMillis();
            avgTime += end - startTime;
        }
        System.out.printf(message, Math.round(avgTime));
    }

    private static void removeFirstElements(Collection<String> col, String message) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TO_BE_REMOVED_BEGINNING; i++)
            col.remove("S" + i);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
    }

    private static <E> void removeFromEnding(List<E> list, String message) {
        long startTime = System.currentTimeMillis();
        int listSize = list.size();
        for (int i = listSize - 1; i > listSize - TO_BE_REMOVED_ENDING; i--)
            list.remove(i);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
    }

    private static void iterateElements(Collection<String> col, String message) {
        long startTime = System.currentTimeMillis();
        for(String str : col);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
    }

    private static String getToPosition(List<String> list, String message) {
        long startTime = System.currentTimeMillis();
        String str = list.get(LOT_OF_STUFF / 2);
        long end = System.currentTimeMillis();
        System.out.printf(message, end - startTime);
        return str;
    }

    private static class ReverseElements<E extends Comparable<E>> implements Comparable<ReverseElements<E>> {

        private E elem;

        private ReverseElements(E elem) {
            this.elem = elem;
        }

        @Override
        public int compareTo(ReverseElements reverse) {
            return -1 * elem.compareTo((E) reverse.elem);
        }
    }

    private static class ReverseIterator<E> implements Iterator<E> {

        ListIterator<E> listIterator;

        private ReverseIterator(ListIterator<E> listIterator) {
            this.listIterator = listIterator;
        }

        @Override
        public boolean hasNext() {
            return listIterator.hasPrevious();
        }

        @Override
        public E next() {
            return listIterator.previous();
        }

        @Override
        public void remove() {

        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {

        }
    }

}

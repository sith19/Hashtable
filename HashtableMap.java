import java.util.NoSuchElementException;
import java.util.LinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashtableMap<KeyType, ValueType>  implements MapADT<KeyType, ValueType>{
  
  protected class Pair {
    
    public KeyType key;
    public ValueType value;
    
    public Pair(KeyType key, ValueType value) {
        this.key = key;
        this.value = value;
    }

  }
  
  private LinkedList<Pair>[] table = null;
  //private array for the hashtable
  private int size;
  //size variable to keep track of size
  @SuppressWarnings("unchecked")
  public HashtableMap() {
    table = (LinkedList<Pair>[])new LinkedList[64];
    size = 0;
    //initialize private variables
  }
  
  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity){
    table = (LinkedList<Pair>[])new LinkedList[capacity];
    size = 0;
    //initialize private variables
  }
  
  /**
   * Adds a new key,value pair/mapping to this collection.It is ok that the value is null.
   * @param key the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException if key is null
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    if(key == null) {
      throw new NullPointerException("key cannot be null");
      //throw exception if key is null
    }else if(this.containsKey(key)) {
      throw new IllegalArgumentException("key is already in map");
      //throw exception if table contains key
    }
    int index = Math.abs(key.hashCode()) % table.length;
    //calculate index with hash function
    if(table[index] == null) {
      //check if there is no linked list in index
      table[index] = new LinkedList<Pair>();
      //Initialize linked list
      table[index].add(new Pair(key, value));
      //add Pair to linked list
      size++;
      //increment size
    }else {
      table[index].add(new Pair(key,value));
      //if linked list exists add 
      size++;
      //increment size
    }
    if(calculateLoadFactor() >= 80) {
      //check Load Factor after inserting new value
      this.doubleAndRehash();
      //if load factor passes threshold double capacity and rehash
    }
  }
  /**
   * Doubles the capacity of the table and rehashes the values
   */
  @SuppressWarnings("unchecked")
  private void doubleAndRehash() {
    LinkedList<Pair>[] newTable = (LinkedList<Pair>[]) new LinkedList[this.getCapacity() * 2];
    //Initialize new array with double the length
    for(int i = 0; i < this.table.length; i++) {
      //iterate over current table
      if(table[i] != null) {
        //if linked list exists 
        for(Pair p: this.table[i]) {
          //iterate over pairs in linked list
          int index = Math.abs(p.key.hashCode()) % newTable.length;
          //calculate index in new table
          if(newTable[index] == null) {
            //check if index in new table has a linked list
            newTable[index] = new LinkedList<Pair>();
            //if it doesn't create a new linked list
            newTable[index].add(new Pair(p.key, p.value));
            //insert Pair into linked list
          }else {
            newTable[index].add(new Pair(p.key,p.value));
            //if linked list exists just insert pair
          }
        }
      }
      
    }
    this.table = newTable;
    //set table to the new table
  }
  
  /**
   * Calculates the load factor of the table
   * @return the load factor in percent
   */
  private double calculateLoadFactor() {
    return (this.getSize()/(double)(this.getCapacity())) * 100;
    //divide size by capacity and multiply and by 100 to get percentage
    
  }
  /**
   * Checks whether a key maps to a value in this collection.
   * @param key the key to check
   * @return true if the key maps to a value, and false is the
   *         key doesn't map to a value
   */
  @Override
  public boolean containsKey(KeyType key) {
    if(key == null) {
      return false;
    }
    int index = Math.abs(key.hashCode()) % table.length;
    //get index of key with hash function
    if(table[index] == null) {
      //if index doesn't have a linked list return false
      return false;
    }else {
      for(Pair p: table[index]) {
        //iterate over pairs in linked list
        if(p.key.equals(key)) {
          //if key is found return true
          return true;
        }
      }
    }
    return false;
    //if key is not found in linked list return false
  }
  
  /**
   * Retrieves the specific value that a key maps to.
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    if(key == null) {
      throw new NoSuchElementException("key cannot be null");
    }
    int index = Math.abs(key.hashCode()) % table.length;
    //get index with hash function
    if(table[index] == null) {
      //if linked list is null throw exception
      throw new NoSuchElementException("key is not in table");
    }
    for(Pair p: table[index]) {
      //iterate over pair in linked list
      if(p.key.equals(key)) {
        //if key is found return value stored in pair
        return p.value;
      }
    }
   
    throw new NoSuchElementException("key is not in table");
    //if key is not in linked list throw exception
  }
  
  /**
   * Remove the mapping for a key from this collection.
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    if(key == null) {
      throw new NoSuchElementException("key cannot be null");
    }
    int index = Math.abs(key.hashCode()) % table.length;
    //get index with hash code
    if(table[index] == null) {
      //if there is no linked list in index throw exception
      throw new NoSuchElementException("key is not in table");
    }
    int i = 0;
    //Initialize i to keep track of index in list
    for(Pair p: table[index]) {
      //iterate over the list
      if(p.key.equals(key)) {
        //if key is found
        table[index].remove(i);
        //remove the Pair at tracked index i from list
        return p.value; 
        //return its value
      }
      i++;
      //increment index
    }
   
    throw new NoSuchElementException("key is not in table");
    //if key is not found throw exception
  }
  
  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    for(int i = 0; i < this.table.length; i++) {
      //iterate over lists
      if(table[i] != null) {
        //if list is not empty
        table[i]= null;
        //set it to empty
        
      }
    }
    this.size = 0;
  }
  
  
  /**
   * Retrieves the number of keys stored in this collection.
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    return size;
    //return private field that keeps track of size
  }
  
  
  /**
   * Retrieves this collection's capacity.
   * @return the size of the underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return table.length;
    //return length of the table
  }
  
  /**
   * tests that the put method and doubling capacity and rehashing
   */
  @Test
  public void testPut() {
    HashtableMap<Integer, Integer> table = new HashtableMap<Integer, Integer>();
    table.put(1, 1);
    table.put(2,2);
    table.put(3, 3);
    table.put(11, 3);
    table.put(12, 3);
    table.put(4, 4);
    table.put(64, 5);
    table.put(33, 5);
    table.put(6, 6);
    //initialize new table and insert values
    
    int found = 0;
    for(int i = 0; i < table.table.length; i ++) {
      //iterate over table and count the number of values
      if(table.table[i] != null) {
        for(HashtableMap<Integer, Integer>.Pair p: table.table[i]) {
          if(p.value == 1 ){
            found++;
          }
          if(p.value == 2 ){
            found++;
          }
          if(p.value == 3 ){
            found++;
          }
          if( p.value == 4 ){
            found++;
          }
          if(p.value == 5 ){
            found++;
          }
          if( p.value == 6 ){
            found++;
          }
        }
      }
    }
    if(found != 9) {
      //check that count lines up with number inserted
      Assertions.fail("put did not insert all values into the table");
    }
   Assertions.assertThrows(IllegalArgumentException.class, ()->table.put(6, 6));
   Assertions.assertThrows(NullPointerException.class, ()->table.put(null,null));
   //check assertions
   
   HashtableMap<Integer, Integer> table1 = new HashtableMap<Integer, Integer>(5);
   table1.put(1, 1);
   table1.put(2,2);
   table1.put(3, 3);
   if(table1.getCapacity() != 5) {
     Assertions.fail("size is not updated correctly");
   }
   table1.put(11, 3);
   table1.put(12, 3);
   if(table1.getCapacity() != 10) {
     Assertions.fail("size is not updated correctly");
   }
   found = 0;
   for(int i = 0; i < table.table.length; i ++) {
     //iterate over table and count the number of values
     if(table.table[i] != null) {
       for(HashtableMap<Integer, Integer>.Pair p: table.table[i]) {
         if(p.value == 1 ){
           found++;
         }
         if(p.value == 2 ){
           found++;
         }
         if(p.value == 3 ){
           found++;
         }
        
       }
     }
   }
   
   if(found != 5) {
     //check that all values were found during iteration
     Assertions.fail("Values were not rehashed correctly");
   }
  }
  
  /**
   * Tests the containsKey method
   */
  @Test
  public void testContainsKey() {
    HashtableMap<String, Integer> table = new HashtableMap<String, Integer>();
    table.put("Apple", 3);
    //initialize table
    
    if(!table.containsKey("Apple")) {
      //check that contains return the right value for existing key
      Assertions.fail("Hashtable doesn't return correct contains value for existant key");
      
    }
    if(table.containsKey("Pomelo")) {
      //check that contains returns the correct value for non existent key
      Assertions.fail("Hashtable doesn't return correct contains value for non existant key");
      
    }
    
  }
  /**
   * tests the get method
   */
  @Test
  public void testGet() {
    HashtableMap<String, Integer> table = new HashtableMap<String, Integer>();
    table.put("Apple", 1);
    table.put("Orange",2);
    table.put("Pomelo", 3);
    table.put("Banana", 3);
    table.put("Kiwi", 3);
    //initialize table
    
    Integer val = table.get("Apple");
    if(val != 1) {
      //check that correct value is return by get
      Assertions.fail("Did not return expected value");
      
    }
    
    Integer val1 = table.get("Pomelo");
    if(val1 != 3) {
      //check that correct value is return by get
      Assertions.fail("Did not return expected value");
      
    }
    Assertions.assertThrows(NoSuchElementException.class, ()->table.get("Shawarma"));
    //check that exception is thrown by get
    
  }
  /**
   * tests the remove method
   */
  @Test
  public void testRemove() {
    HashtableMap<Integer, Integer> table = new HashtableMap<Integer, Integer>();
    table.put(1, 1);
    table.put(2,2);
    table.put(3, 3);
    table.put(11, 3);
    table.put(12, 3);
    //initialize table
    
    table.remove(1);
    table.remove(2);
    //remove values
    
    for(int i = 0; i < table.table.length; i ++) {
      //iterate over table
      if(table.table[i] != null) {
      for(HashtableMap<Integer, Integer>.Pair p: table.table[i]) {
        if(p.key == 1){
          Assertions.fail("Key was not removed from table");
          //check if removed key are still in table
        }
        if(p.key == 2){
          Assertions.fail("Key was not removed from table");
          //check if removed key are still in table
        }
        
      }
      }
    Assertions.assertThrows(NoSuchElementException.class, ()->table.remove(1));
    //check that remove throws correct exception
    }
  }
  
  /**
   * tests the clear, getSize, and getCapacity 
   */
  @Test
  public void testExtraMethods() {
    HashtableMap<Integer, Integer> table = new HashtableMap<Integer, Integer>();
    table.put(1, 1);
    table.put(2,2);
    table.put(3, 3);
    table.put(11, 3);
    table.put(12, 3);
    //Initialize table
    if(table.getSize() != 5) {
      //make sure table returns correct size
      Assertions.fail("Table doesn't keep track of size");
    }
    if(table.getCapacity() != 64) {
      //make sure table returns correct capacity
      Assertions.fail("Table doesn't return correct capacity");
    }
    table.clear();
    for(int i = 0; i < table.table.length; i ++) {
      //iterate over table
      if(table.table[i] != null) {
      for(HashtableMap<Integer, Integer>.Pair p: table.table[i]) {
        if(p != null) {
          //if a pair is not null fail the test
          Assertions.fail("clear doesn't set pairs to null");
        }
      }
     } 
    }
  }
}

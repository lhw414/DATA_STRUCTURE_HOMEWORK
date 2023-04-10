import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	MyLinkedList<MovieList> movieDBList;

    public MovieDB() {
        movieDBList = new MyLinkedList<>();
    }

    public void insert(MovieDBItem item) {
        // Insert the given item to the MovieDB.
		Iterator<MovieList> movieListIterator = movieDBList.iterator();
		MyLinkedList<MovieDBItem> movieDBListOfGenre;
		Iterator<MovieDBItem> movieDBIteratorOfGenre;
		Node<MyLinkedList<MovieDBItem>> prevList;

		while (movieListIterator.hasNext()) {
			movieDBListOfGenre = movieDBIterator.next();
			movieDBIteratorOfGenre = movieDBListOfGenre.iterator();
			prevList = null;
			// Item's genre equal to list's genre 			
			if (movieDBListOfGenre.first().getGenre().compareTo(item.getGenre()) == 0) {
				Node<MovieDBItem> prevNode = null;
				Node<MovieDBItem> currNode = movieDBListOfGenre.head;
				int compareResult = -1;
				while (movieDBIteratorOfGenre.hasNext()) {
					compareResult = movieDBIteratorOfGenre.next().compareTo(item);
					if (compareResult >= 0) {
						break;
					}
					prevNode = currNode;
					currNode = currNode.getNext();
				}
				if (compareResult != 0) {
					Node<MovieDBItem> newNode = new Node<MovieDBItem>(item);
					currNode.setNext(newNode);
				}
			}
			// Can't find item's genre
			if (movieDBListOfGenre.first().getGenre().compareTo(item.getGenre()) > 0) {
				break;
			}
			prevList = movieDBListOfGenre;
		}

		prevList.setNext(prevList);

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        System.err.printf("[trace] MovieDB: INSERT [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public void delete(MovieDBItem item) {
        // FIXME implement this
        // Remove the given item from the MovieDB.
    	
    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        System.err.printf("[trace] MovieDB: DELETE [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
    	
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.
    	
        // This tracing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
    	System.err.printf("[trace] MovieDB: SEARCH [%s]\n", term);
    	
    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        System.err.printf("[trace] MovieDB: ITEMS\n");

    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
        
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	private String genre;
	private Node<String> next;

	public Genre(String genre) {
		super(genre);
	}
	
	@Override
	public int compareTo(Genre o) {
		return genre.compareTo(o.getItem());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
        int result = 1;
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        return result;
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

class MovieList implements ListInterface<String> {	
	//head is genre node
	Node<String> head;
	int numItems;

	public MovieList(String genre) {
		head = new Genre(genre);
	}

	@Override
	public Iterator<String> iterator() {
		return new MyLinkedListIterator<String>(this)
	}

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems; 
	}

	@Override
	public void add(String item) {
		Node<String> last = head;
		while(last.getNext() != null) {
			last = last.getNext();
		}
		last.insertNext(item);
		this.numItems += 1;
	}

	@Override
	public String first() {
		return head.getNext().getItem();
	}

	@Override
	public void removeAll() {
		head.setNext(null);
	}
}
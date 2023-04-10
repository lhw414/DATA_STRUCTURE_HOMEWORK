import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	MyLinkedList<MyLinkedList<MovieDBItem>> movieDBList;

    public MovieDB() {
        movieDBList = new MyLinkedList<>();
    }

    public void insert(MovieDBItem item) {
        // Insert the given item to the MovieDB.
		Iterator<MyLinkedList<MovieDBItem>> movieDBIterator = movieDBList.iterator();
		MyLinkedList<MovieDBItem> movieDBListOfGenre;
		Iterator<MovieDBItem> movieDBIteratorOfGenre;
		Node<MyLinkedList<MovieDBItem>> prevList;

		while (movieDBIterator.hasNext()) {
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
	public Genre(String name) {
		super(name);
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	@Override
	public int compareTo(Genre o) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

class MovieList implements ListInterface<String> {	
	public MovieList() {
	}

	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void add(String item) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public String first() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void removeAll() {
		throw new UnsupportedOperationException("not implemented yet");
	}
}
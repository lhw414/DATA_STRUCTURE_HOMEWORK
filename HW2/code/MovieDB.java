import java.util.Iterator;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {

	MyLinkedList<Genre> movieDBList;

    public MovieDB() {
        movieDBList = new MyLinkedList<Genre>();
    }

    public void insert(MovieDBItem item) {
        // Insert the given item to the MovieDB.
		Boolean canFindCorrectGenre = false;
		Node<Genre> prevGenre = null;
		Node<Genre> currGenre = movieDBList.head;

		for (Genre genre : movieDBList) {
			prevGenre = currGenre;
			currGenre = currGenre.getNext();
			if(genre.getItem() == item.getGenre()) {
				Boolean sameMovieInList = false;
				Node<String> prevMovie = null;
				Node<String> currMovie = genre.movielist.head;
				canFindCorrectGenre = true;

				for (String title : genre.movielist) {
					prevMovie = currMovie;
					currMovie = currMovie.getNext();
					if(title == item.getTitle()) {
						sameMovieInList = true;
						break;
					} else if (title.compareTo(item.getTitle()) > 0) {
						break;
					}
				}

				if(!sameMovieInList) {
					Node<String> newNode = new Node<>(item.getTitle());
					prevMovie.setNext(newNode);
					newNode.setNext(currMovie);
					genre.movielist.numItems += 1;
				}

				break;

			} else if (genre.getItem().compareTo(item.getGenre()) > 0) {
				break;
			}
		}
		
		//todo 글자순서에 맞게 데이터 삽입하도록 구현

		if(!canFindCorrectGenre) {
			Genre newGenre = new Genre(item.getGenre());
			Node<Genre> newNode = new Node<>(newGenre);
			newNode.getItem().movielist.add(item.getTitle());
			movieDBList.numItems += 1;

			if (prevGenre == null) {
				movieDBList.head.setNext(newNode);
				return;
			} else if (currGenre == null) {
				movieDBList.add(newGenre);
				movieDBList.numItems += 1;
			}
			movieDBList.numItems += 1;
			prevGenre.setNext(newNode);
			newNode.setNext(currGenre);
		}
    }

    public void delete(MovieDBItem item) {

		Node<Genre> prevGenre = null;
		Node<Genre> currGenre = movieDBList.head;

		for (Genre genre : movieDBList) {
			prevGenre = currGenre;
			currGenre = currGenre.getNext();
			if (genre.getItem() == item.getGenre()) {
				Node<String> prevMovie = null;
				Node<String> currNode = genre.movielist.head;

				for (String title : genre.movielist) {
					prevMovie = currNode;
					currNode = currNode.getNext();
					if (title == item.getTitle()) {
						prevMovie.removeNext();
						genre.movielist.numItems -= 1;
						break;
					}
				}

				if (genre.movielist.numItems == 0) {
					prevGenre.setNext(currGenre.getNext());
				}
			}

			break;
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {

		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

		for (Genre genre : movieDBList) {
			for (String title : genre.movielist) {
				if (title.contains(term)) {
					results.add(new MovieDBItem(genre.getItem(), title));
				}
			}
		}

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {

        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

		for (Genre genre : movieDBList) {
			for (String title : genre.movielist) {
				results.add(new MovieDBItem(genre.getItem(), title));
			}
		}

        return results;
    }
}

class Genre extends Node<String>{
	MovieList movielist;

	public Genre(String genre) {
		super(genre);
		movielist = new MovieList();
	}
}

class MovieList implements ListInterface<String> {	
	//dummy head
	Node<String> head;
	int numItems;

	public MovieList() {
		head = new Node<String>(null);
	}

	@Override
	public Iterator<String> iterator() {
		return new MovieListIterator(this);
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
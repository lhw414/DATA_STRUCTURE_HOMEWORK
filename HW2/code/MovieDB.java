import java.util.Iterator;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {

	MyLinkedList<String> movieDBList;

    public MovieDB() {
        movieDBList = new MyLinkedList<String>();
    }

    public void insert(MovieDBItem item) {
        // Insert the given item to the MovieDB.
		MyLinkedListIterator<String> genreIterator = (MyLinkedListIterator<String>) movieDBList.iterator();
		Genre currGenre = (Genre) movieDBList.head;
		String currGenreTitle;
		Boolean canFindCorrectGenre = false;

		//search genre
		while(genreIterator.hasNext()) {
			currGenreTitle = genreIterator.next();
			//find correct genre
			if(currGenreTitle == item.getGenre()) {
				currGenre = (Genre) genreIterator.getCurrNode();
				MovieList movieList = currGenre.movielist;
				MovieListIterator movieListIterator = (MovieListIterator) movieList.iterator();
				Node<String> prevMovie = movieList.head;
				String currMovieTitle;
				Boolean sameMovieInList = false;
				canFindCorrectGenre = true;

				while(movieListIterator.hasNext()) {
					currMovieTitle = movieListIterator.next();
					if (currMovieTitle == item.getTitle()) {
						sameMovieInList = true;
						break;
					} else if (currMovieTitle.compareTo(item.getTitle()) > 0) {
						prevMovie = movieListIterator.getprevNode();
					}
				}

				if (!sameMovieInList) {
					Node<String> newNode = new Node<>(item.getTitle());
					prevMovie.setNext(newNode);
					movieList.numItems += 1;
				}

			} else if (currGenre.getItem().compareTo(item.getGenre()) > 0) {
				currGenre = (Genre) genreIterator.getprevNode();
				break;
			}
		}

		if(!canFindCorrectGenre) {
			Genre newGenre = new Genre(item.getGenre());
			currGenre.setNext(newGenre);
			newGenre.movielist.add(item.getTitle());
		}
    }

    public void delete(MovieDBItem item) {

        MyLinkedListIterator<String> genreIterator = (MyLinkedListIterator<String>) movieDBList.iterator();
		Genre currGenre = (Genre) movieDBList.head;

		while(genreIterator.hasNext()) {
			if (genreIterator.next() == item.getGenre()) {
				currGenre = (Genre) genreIterator.getCurrNode();
				MovieList movieList = currGenre.movielist;
				MovieListIterator movieListIterator = (MovieListIterator) movieList.iterator();
				Node<String> prevMovie = movieList.head;

				while(movieListIterator.hasNext()) {
					if (movieListIterator.next() == item.getTitle()) {
						prevMovie.removeNext();
						movieList.numItems -= 1;
						break;
					}
				}

				break;
			}
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {

        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
    	MyLinkedListIterator<String> genreIterator = (MyLinkedListIterator<String>) movieDBList.iterator();
		Genre currGenre = (Genre) movieDBList.head;
		MovieListIterator movieListIterator;
		String movieTitle;
		
    	while(genreIterator.hasNext()) {
			currGenre = (Genre) genreIterator.getCurrNode();
			movieListIterator = (MovieListIterator) currGenre.movielist.iterator();

			while(movieListIterator.hasNext()) {
				movieTitle = movieListIterator.next();
				if(movieTitle.contains(term)) {
					results.add(new MovieDBItem(currGenre.getItem(), movieTitle));
				}
			}
		}

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {

        MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
    	MyLinkedListIterator<String> genreIterator = (MyLinkedListIterator<String>) movieDBList.iterator();
		Genre currGenre = (Genre) movieDBList.head;
		MovieListIterator movieListIterator;
		
    	while(genreIterator.hasNext()) {
			currGenre = (Genre) genreIterator.getCurrNode();
			movieListIterator = (MovieListIterator) currGenre.movielist.iterator();

			while(movieListIterator.hasNext()) {
				results.add(new MovieDBItem(currGenre.getItem(), movieListIterator.next()));
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
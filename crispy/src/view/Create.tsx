import { useEffect, useState } from "react"
import { Movie } from "../model/Movie";

export function Create() {
  const [title, setTitle] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState<Movie[]>([]);
  const [movies, setMovies] = useState<Movie[]>([]);

  useEffect(() => {
    if (!searchTerm) {
      setSearchResults([]);
    } else {
        fetch(`https://api.themoviedb.org/3/search/movie?api_key=068120d03377a9539115269490d93a54&query=${searchTerm}`)
        .then((response) => response.json())
        .then((json:{results: Movie[]}) => setSearchResults(json.results));
    }
    
  }, [searchTerm]);

  const addMovie = (movie: Movie) => {
    setMovies([...movies, movie]);
    setSearchTerm('');
  }

  return (
    <>
    Just regular text on the create page
    <div>
      Title: <input onChange={e => setTitle(e.target.value)}/>
    </div>
    <div>
      Add movie: <input value={searchTerm} onChange={e => setSearchTerm(e.target.value)}/>
    </div>
    <ul>{searchResults.map(movie => <li><button onClick={() => addMovie(movie)}>{movie.title}</button></li>)}</ul>
    <ul>{movies.map(movie => <li>{movie.title}</li>)}</ul>
    </>
  )
 
}
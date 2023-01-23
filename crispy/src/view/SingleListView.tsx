import { useEffect, useState } from "react";
import { promises } from "stream";
import { Collection } from "../model/Collection";
import { Movie } from "../model/Movie";

type SingleListViewProps = Collection;

export function SingleListView ({ collectionTitle: name, type, movieIds: content }: SingleListViewProps) {
    const [movies, setMovies] = useState<(Movie | undefined)[]>();

    useEffect(() => {
        Promise.all(
            content.map(id => 
                fetch(`http://localhost:8080/movies/${id}`) 
                .then((response) => response.json())
                .catch(error => console.log(error))
                )
            ).then(setMovies)
            
    }, [content, setMovies]);
    
    if(!movies) {
        return <>Loading</>;
    }
    return (
        <>
            <div>{name}</div> 
            {/* <div>{type}</div> */}
            <ul>{movies.map(movie => <li>{movie?.title} ({movie?.release_date.substring(0, 4)})</li>)}</ul>
        </>

    )
}
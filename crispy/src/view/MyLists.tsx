import { Collection } from "../model/Collection";
import { ListView } from "./ListView";

// TODO: Don't hardcode My Lists
const myLists: Collection[] = [
    {
        collectionTitle: "Mystical Moon Movies",
        type: "movies",
        movieIds: ["moon", "laser-moon", "moonfall"],
    },

    {
        collectionTitle: "World's Greatest Octopi Movies or Something. Yeah.",
        type: "movies",
        movieIds: ["mega-shark-vs-giant-octopus", "sharktopus", "octopussy"],
    },
]


export function MyLists() {
    return (
        <>

        <ListView title="My Lists" lists={myLists}/>

        </>
    );
}


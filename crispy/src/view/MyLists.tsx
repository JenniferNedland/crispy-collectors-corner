import { List } from "../model/List";
import { ListView } from "./ListView";

// TODO: Don't hardcode My Lists
const myLists: List[] = [
    {
        name: "Mystical Moon Movies",
        type: "movies",
        content: ["moon", "laser-moon", "moonfall"],
    },

    {
        name: "World's Greatest Octopi Movies or Something. Yeah.",
        type: "movies",
        content: ["mega-shark-vs-giant-octopus", "sharktopus", "octopussy"],
    },
]


export function MyLists() {
    return (
        <>

        <ListView title="My Lists" lists={myLists}/>

        </>
    );
}


import { List } from "../model/List";
import { ListView } from "./ListView";

// TODO: Don't hardcode public lists
const publicLists: List[] = [
    {
        name: "Kickass Laser Movies",
        type: "movies",
        content: ["laser-fart", "laser-moon", "laser-wolf"],
    },

    {
        name: "World's Greatest Shark Movies or Something. Yeah.",
        type: "movies",
        content: ["jaws", "5-headed-shark-attack", "sharknado", "sharkboy-and-lavagirl", "sharktopus", "mega-shark-vs-giant-octopus"],
    },
]


export function Home() {
    return (
        <>
        Welcome to the home page

        <ListView title="Public Lists" lists={publicLists}/>

        </>
    );
}


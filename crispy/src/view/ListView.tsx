import { Collection } from "../model/Collection";
import { SingleListView } from "./SingleListView";

type ListViewProps = {
    title: string;
    lists: Collection[];
};

export function ListView ({ title, lists }: ListViewProps) {
    console.log(lists);
    return (
        <>
            <h1>{title}</h1>
            <ol>
                {lists.map(( list ) => <li><SingleListView {...list} /></li>)}
            </ol>
        </>

    )
}
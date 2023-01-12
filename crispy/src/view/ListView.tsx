import { List } from "../model/List";
import { SingleListView } from "./SingleListView";

type ListViewProps = {
    title: string;
    lists: List[];
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
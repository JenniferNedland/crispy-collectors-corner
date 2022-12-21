import { List } from "../model/List";

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

        {lists.map(({ name, type, content }) => {
            return (
            <li>
                <div>{name}</div> 
                {/* TODO: add little icon for list type */}
                {/* <div>{type}</div> */}
                <ul>{content.map(id => <li>{id}</li>)}</ul>
            </li>
        );
        })}
    </ol>
        </>

    )
}
import { useEffect, useState } from "react";
import { Collection } from "../model/Collection";
import { ListView } from "./ListView";


export function MyLists() {
    const [myLists, setMyLists] = useState<Collection[]>();

    useEffect(() => {
        const requestInit = { headers: { Authorization: `Bearer ${localStorage.getItem('credential')}` } };
        fetch(`http://localhost:8080/user/lists`, requestInit)
        .then((response) => response.json())
        .then((json) => setMyLists(json));
    }, []);
  
    if (!myLists) {
      return <>Loading</>;
    }
    return (
      <>
        <ListView title="My Lists" lists={myLists} />
      </>
    );
}


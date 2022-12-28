import { useEffect, useState } from "react";
import { json } from "stream/consumers";
import { List } from "../model/List";
import { ListView } from "./ListView";

export function Home() {
  const [publicLists, setPublicLists] = useState<List[]>();

  useEffect(() => {
    fetch(`http://localhost:8080/public/lists`)
      .then((response) => response.json())
      .then((json) => setPublicLists(json));
  }, []);

  if (!publicLists) {
    return <>Loading</>;
  }
  return (
    <>
      Welcome to the home page
      <ListView title="Public Lists" lists={publicLists} />
    </>
  );
}

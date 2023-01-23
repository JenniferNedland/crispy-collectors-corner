import { useEffect, useState } from "react";
import { Collection } from "../model/Collection";
import { ListView } from "./ListView";

export function Home() {
  const [publicLists, setPublicLists] = useState<Collection[]>();

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

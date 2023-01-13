import React, { useState } from 'react';
import './App.css';
import { Create } from './view/Create';
import { Header, Page } from "./view/Header";
import { Home } from "./view/Home";
import { MyLists } from './view/MyLists';

const pages: {[Property in Page]: JSX.Element} = {
  HOME: <Home />,
  MY_LISTS: <MyLists />,
  //create faves which will eventually lead to friends possibly? 
  FAVES: <div />,
  CREATE: <Create />,
}

function App() {
const [currentPage, setCurrentPage] = useState<Page>("HOME");

  return (
    <div className="App">
      <header className="App-header">
        <Header setCurrentPage={setCurrentPage} />
        
        
      </header>

      <body>{pages[currentPage]}</body>
      

    </div>
  );
}

export default App;

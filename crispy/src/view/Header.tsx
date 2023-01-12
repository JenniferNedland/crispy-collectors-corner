import { GoogleLogin } from '@react-oauth/google';
import jwt_decode from "jwt-decode";
import { useState } from 'react';

export type Page = "HOME" | "MY_LISTS" | "FAVES" | "CREATE";

type UserObject = {
    email: string;
    name: string;
    picture: string;
}

type HeaderProps = {
    setCurrentPage: (page: Page) => void;
};

export function Header({ setCurrentPage }: HeaderProps) {
    const [userObject, setUserObject] = useState<UserObject>();
    const userInfo = userObject? (
        <div>
            <img src={userObject.picture} />
            {userObject.name}
        </div>
    
    ): (
    <GoogleLogin
        onSuccess={response => {
          if (response.credential) {
              const userObject = jwt_decode(response.credential) as UserObject;
              setUserObject(userObject);
          }

        }}
        onError={()=>console.log("failure")}
      />);
    return (
        <>
        {userInfo}
        Crispy Collectors Corner 
        <div className="tab">
            <button onClick={() => setCurrentPage("HOME")}>Home</button>
            <button onClick={() => setCurrentPage("MY_LISTS")}>My Lists</button>
            <button onClick={() => setCurrentPage("FAVES")}>Faves</button>
            <button onClick={() => setCurrentPage("CREATE")}>Create</button>
        </div>
        </>

    )
}
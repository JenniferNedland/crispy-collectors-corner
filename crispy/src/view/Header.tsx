import { GoogleLogin } from '@react-oauth/google';
import jwt_decode from "jwt-decode";
import { useState } from 'react';

export type Page = "HOME" | "MY_LISTS" | "CREATE" | "FAVES";

type UserObject = {
    email: string;
    name: string;
    picture: string;
}

type HeaderProps = {
    setCurrentPage: (page: Page) => void;
};

export function Header({ setCurrentPage }: HeaderProps) {
    const credential = localStorage.getItem("credential");
    const [userObject, setUserObject] = useState<UserObject | undefined>(credential ? jwt_decode(credential) as UserObject: undefined);
    const userInfo = userObject ? (
        <div>
            <img className="avatar" src={userObject.picture} referrerPolicy="no-referrer" />
            {userObject.name}
        </div>
    ) : (
    <GoogleLogin
        onSuccess={response => {
          if (response.credential) {
              const userObject = jwt_decode(response.credential) as UserObject;
              setUserObject(userObject);
              localStorage.setItem("credential", response.credential);
              fetch(`http://localhost:8080/login?idtoken=${response.credential}`, { method: "POST"});
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
            <button onClick={() => setCurrentPage("CREATE")}>Create</button>
            <button onClick={() => setCurrentPage("FAVES")}>Faves</button>
        </div>
        </>

    )
}
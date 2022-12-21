export type Page = "HOME" | "MY_LISTS" | "FAVES";

type HeaderProps = {
    setCurrentPage: (page: Page) => void;
};

export function Header({ setCurrentPage }: HeaderProps) {
    return (
        <>
        Crispy Collectors Corner 
        <div className="tab">
            <button onClick={() => setCurrentPage("HOME")}>Home</button>
            <button onClick={() => setCurrentPage("MY_LISTS")}>My Lists</button>
            <button onClick={() => setCurrentPage("FAVES")}>Faves</button>
        </div>
        </>

    )
}
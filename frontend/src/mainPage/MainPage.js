import React, {useEffect, useState} from 'react';
import './MainPage.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFolderClosed} from "@fortawesome/free-solid-svg-icons";

const MainPage = () => {
    const [folders, setFolders] = useState([]);

    const handleLogout = () => {
        window.localStorage.removeItem('token');
        window.location = '/registration-form'
    }
    const fetchFolders = async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('Токен не найден');
                return;
            }
            const response = await fetch('http://localhost:8080/folders/user', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            if (!response.ok) {
                console.log("Ошибка")
                throw new Error('Ошибка при получении папок');
            }

            const data = await response.json();
            console.log('Полученные данные:', data)
            setFolders(data);
        } catch (error) {
            console.log('Ошибка при загрузке папок');
        }
    };

    useEffect(() => {
        fetchFolders();
    }, []);

    return (
        <div className="MainPage">
            <button className="logout" onClick={handleLogout}>Выход</button>
            <ul className="folders">
                {folders.map((folder, index) => (
                    <div className="folder" key={index}>{folder.name}
                <FontAwesomeIcon icon={faFolderClosed} className="folderIcon"/>
                    </div>
            ))}
            </ul>
        </div>
    );
}
export default MainPage;
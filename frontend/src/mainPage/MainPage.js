import React, {useEffect, useState} from 'react';
import './MainPage.css';


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
                //throw new Error('Ошибка при получении папок');
            }

            const data = await response.json();
            setFolders(data); // Сохраняем папки в состоянии
        } catch (error) {
            console.log('Ошибка при загрузке папок');
        }
    };

    useEffect(() => {
        fetchFolders();
    }, []);

    const MainPage = () => {
        return (
            <div className="MainPage">
                <button className="logout" onClick={handleLogout}>Выход</button>
                <ul>
                    {folders.map((folder, index) => (
                        <li key={index}>{folder.name}</li>
                    ))}
                </ul>
            </div>
        );
    }
}
export default MainPage;
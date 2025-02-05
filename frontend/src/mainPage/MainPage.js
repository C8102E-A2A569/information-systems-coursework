import React, {useEffect, useState} from 'react';
import './MainPage.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFolderClosed} from "@fortawesome/free-solid-svg-icons";

const MainPage = () => {
    const [folders, setFolders] = useState([]);
    const [tests, setTests] = useState([]);
    const [currentPath, setCurrentPath] = useState([]);
    const [selectedFolder, setSelectedFolder] = useState(null);
    const [subFolders, setSubFolders] = useState([]);
    const [folderTests, setFolderTests] = useState([]);
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

    const fetchTests = async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('Токен не найден');
                return;
            }
            const response = await fetch('http://localhost:8080/tests/user', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            if (!response.ok) {
                console.log("Ошибка")
                throw new Error('Ошибка при получении тестов');
            }

            const data = await response.json();
            console.log('Полученные данные:', data)
            setTests(data);
        } catch (error) {
            console.log('Ошибка при загрузке тестов');
        }
    };

    const handleFolderClick = async (folderId) => {
        try {
            setCurrentPath(prevPath => [...prevPath, folderId]);

            const subFoldersData = await fetchFolders(folderId);
            setFolders(subFoldersData);

            const folderTestsData = await fetchTests(folderId);
            setFolderTests(folderTestsData);
        } catch (error) {
            console.error('Ошибка при загрузке данных для папки:', error.message);
        }
    };

    const handleBack = () => {
        if (currentPath.length === 0) return;

        const newPath = [...currentPath];
        newPath.pop();
        setCurrentPath(newPath);

        if (newPath.length === 0) {
            fetchFolders().then(setFolders);
            fetchTests().then(setTests);
            setFolderTests([]);
        } else {
            const parentFolderId = newPath[newPath.length - 1];
            fetchFolders(parentFolderId).then(setFolders);
            fetchTests(parentFolderId).then(setFolderTests);
        }
    };

    useEffect(() => {
        fetchFolders();
        fetchTests();
    }, []);

    return (
        <div className="MainPage">
            <button className="logout" onClick={handleLogout}>Выход</button>

            <ul className="folders">
                {(selectedFolder ? subFolders : folders).length > 0 ? (
                    (selectedFolder ? subFolders : folders).map((folder, index) => (
                        <div
                            className="folder"
                            key={index}
                            onClick={() => handleFolderClick(folder.id)}
                        >
                            {folder.name}
                            <FontAwesomeIcon icon={faFolderClosed} className="folderIcon" />
                        </div>
                    ))
                ) : (
                    <p>Нет папок</p> // Отображаем сообщение, если папок нет
                )}
            </ul>
            {currentPath.length > 0 && (
                <div className="tests">
                    {folderTests && folderTests.length > 0 ? (
                        folderTests.map((test, index) => (
                            <div className="test" key={index}>
                                {test.name} ({test.points} баллов)
                                <br/>
                                <button className="start">Пройти</button>
                            </div>
                        ))
                    ) : (
                        <p>В папке нет тестов</p>
                    )}
                </div>
            )}

            {currentPath.length === 0 && tests.length > 0 && (
                    <div className="tests">
                        {tests && tests.length > 0 ? ( // Проверяем, что tests существует и не пуст
                            tests.map((test, index) => (
                                <div className="test" key={index}>
                                    {test.name} ({test.points} баллов)
                                    <br/>
                                    <button className="start">Пройти</button>
                                </div>
                            ))
                        ) : (
                            <p>У вас пока нет тестов</p> // Отображаем сообщение, если тестов нет
                        )}
                    </div>
                )}
        </div>
    )};
    export default MainPage;